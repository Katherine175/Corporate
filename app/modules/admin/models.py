"""
Module 7 – System Administration
Models: AuditLog, SystemConfig, Integration, Notification
"""

from __future__ import annotations

from datetime import datetime
from enum import Enum
from typing import Any, Dict, Optional

from pydantic import BaseModel


class AuditAction(str, Enum):
    create = "create"
    read = "read"
    update = "update"
    delete = "delete"
    login = "login"
    logout = "logout"
    export = "export"


class IntegrationStatus(str, Enum):
    active = "active"
    inactive = "inactive"
    error = "error"


class NotificationSeverity(str, Enum):
    info = "info"
    warning = "warning"
    error = "error"
    critical = "critical"


# ── Audit Log ─────────────────────────────────────────────────────────────────

class AuditLogEntry(BaseModel):
    id: int
    username: str
    action: AuditAction
    module: str
    resource_id: Optional[str] = None
    details: Optional[str] = None
    ip_address: Optional[str] = None
    timestamp: datetime

    class Config:
        from_attributes = True


class AuditLogCreate(BaseModel):
    username: str
    action: AuditAction
    module: str
    resource_id: Optional[str] = None
    details: Optional[str] = None
    ip_address: Optional[str] = None


# ── System Config ─────────────────────────────────────────────────────────────

class SystemConfigBase(BaseModel):
    key: str
    value: str
    description: Optional[str] = None
    is_sensitive: bool = False


class SystemConfigCreate(SystemConfigBase):
    pass


class SystemConfigUpdate(BaseModel):
    value: Optional[str] = None
    description: Optional[str] = None


class SystemConfig(SystemConfigBase):
    id: int
    updated_at: datetime

    class Config:
        from_attributes = True


# ── Integration ───────────────────────────────────────────────────────────────

class IntegrationBase(BaseModel):
    name: str
    provider: str
    endpoint_url: str
    status: IntegrationStatus = IntegrationStatus.inactive
    config: Dict[str, Any] = {}


class IntegrationCreate(IntegrationBase):
    pass


class Integration(IntegrationBase):
    id: int
    created_at: datetime
    last_synced_at: Optional[datetime] = None

    class Config:
        from_attributes = True


# ── Notification ──────────────────────────────────────────────────────────────

class NotificationBase(BaseModel):
    title: str
    message: str
    severity: NotificationSeverity = NotificationSeverity.info
    module: Optional[str] = None
    recipient: Optional[str] = None


class NotificationCreate(NotificationBase):
    pass


class Notification(NotificationBase):
    id: int
    is_read: bool = False
    created_at: datetime

    class Config:
        from_attributes = True
