"""
Module 1 – Authentication & Authorization
Routes: /auth
"""

from fastapi import APIRouter, HTTPException, status

from app.modules.auth.models import LoginRequest, Token, User, UserCreate
from app.modules.auth.service import login, register_user, get_all_users

router = APIRouter(prefix="/auth", tags=["Module 1 – Auth & Authorization"])


@router.post("/register", response_model=User, status_code=status.HTTP_201_CREATED)
def register(data: UserCreate):
    """Register a new user."""
    return register_user(data)


@router.post("/login", response_model=Token)
def user_login(credentials: LoginRequest):
    """Authenticate and obtain a JWT token."""
    token = login(credentials.username, credentials.password)
    if not token:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid username or password",
        )
    return token


@router.get("/users", response_model=list[User])
def list_users():
    """List all registered users (admin use)."""
    return get_all_users()
