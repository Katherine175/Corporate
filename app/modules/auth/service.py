"""
Module 1 – Authentication & Authorization
Service: token generation, password hashing, user management
"""

from __future__ import annotations

from datetime import datetime, timedelta, timezone
from typing import Dict, Optional

from jose import JWTError, jwt
from passlib.context import CryptContext

from app.config import settings
from app.modules.auth.models import RoleEnum, Token, TokenData, User, UserCreate

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

# In-memory user store (replace with DB in production)
_users: Dict[str, dict] = {}
_next_id: int = 1


def hash_password(password: str) -> str:
    return pwd_context.hash(password)


def verify_password(plain: str, hashed: str) -> bool:
    return pwd_context.verify(plain, hashed)


def create_access_token(data: dict, expires_delta: Optional[timedelta] = None) -> str:
    to_encode = data.copy()
    expire = datetime.now(timezone.utc) + (
        expires_delta or timedelta(minutes=settings.access_token_expire_minutes)
    )
    to_encode["exp"] = expire
    return jwt.encode(to_encode, settings.secret_key, algorithm=settings.algorithm)


def decode_token(token: str) -> Optional[TokenData]:
    try:
        payload = jwt.decode(
            token, settings.secret_key, algorithms=[settings.algorithm]
        )
        username: Optional[str] = payload.get("sub")
        role: Optional[str] = payload.get("role")
        if username is None:
            return None
        return TokenData(username=username, role=RoleEnum(role) if role else None)
    except JWTError:
        return None


def register_user(data: UserCreate) -> User:
    global _next_id
    now = datetime.now(timezone.utc)
    user_dict = {
        "id": _next_id,
        "username": data.username,
        "email": data.email,
        "full_name": data.full_name,
        "role": data.role,
        "is_active": data.is_active,
        "hashed_password": hash_password(data.password),
        "created_at": now,
        "updated_at": now,
    }
    _users[data.username] = user_dict
    _next_id += 1
    return User(**{k: v for k, v in user_dict.items() if k != "hashed_password"})


def authenticate_user(username: str, password: str) -> Optional[User]:
    user_dict = _users.get(username)
    if not user_dict:
        return None
    if not verify_password(password, user_dict["hashed_password"]):
        return None
    return User(**{k: v for k, v in user_dict.items() if k != "hashed_password"})


def login(username: str, password: str) -> Optional[Token]:
    user = authenticate_user(username, password)
    if not user:
        return None
    token_str = create_access_token(
        {"sub": user.username, "role": user.role.value}
    )
    return Token(
        access_token=token_str,
        expires_in=settings.access_token_expire_minutes * 60,
    )


def get_all_users() -> list:
    return [
        User(**{k: v for k, v in u.items() if k != "hashed_password"})
        for u in _users.values()
    ]
