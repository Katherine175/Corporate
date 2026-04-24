"""
Module 1 – Authentication & Authorization
Models: User, Role, Permission, Token
"""

from __future__ import annotations

from datetime import datetime
from enum import Enum
from typing import List, Optional

from pydantic import BaseModel, EmailStr, Field


class RoleEnum(str, Enum):
    admin = "admin"
    manager = "manager"
    employee = "employee"
    auditor = "auditor"


class Permission(BaseModel):
    id: int
    name: str
    description: Optional[str] = None


class Role(BaseModel):
    id: int
    name: RoleEnum
    permissions: List[Permission] = []


class UserBase(BaseModel):
    username: str = Field(..., min_length=3, max_length=50)
    email: EmailStr
    full_name: str
    role: RoleEnum = RoleEnum.employee
    is_active: bool = True


class UserCreate(UserBase):
    password: str = Field(..., min_length=8)


class UserUpdate(BaseModel):
    full_name: Optional[str] = None
    email: Optional[EmailStr] = None
    role: Optional[RoleEnum] = None
    is_active: Optional[bool] = None


class User(UserBase):
    id: int
    created_at: datetime
    updated_at: datetime

    class Config:
        from_attributes = True


class TokenData(BaseModel):
    username: Optional[str] = None
    role: Optional[RoleEnum] = None


class Token(BaseModel):
    access_token: str
    token_type: str = "bearer"
    expires_in: int


class LoginRequest(BaseModel):
    username: str
    password: str
