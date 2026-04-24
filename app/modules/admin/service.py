"""
Module 7 – System Administration
Service: audit logs, config, integrations, notifications
"""

from __future__ import annotations

from datetime import datetime, timezone
from typing import Dict, List, Optional

from app.modules.admin.models import (
    AuditAction,
    AuditLogCreate,
    AuditLogEntry,
    Integration,
    IntegrationCreate,
    IntegrationStatus,
    Notification,
    NotificationCreate,
    SystemConfig,
    SystemConfigCreate,
    SystemConfigUpdate,
)

_audit_logs: Dict[int, dict] = {}
_configs: Dict[str, dict] = {}
_integrations: Dict[int, dict] = {}
_notifications: Dict[int, dict] = {}
_audit_id = 1
_config_id = 1
_integ_id = 1
_notif_id = 1


# ── Audit Logs ────────────────────────────────────────────────────────────────

def log_action(data: AuditLogCreate) -> AuditLogEntry:
    global _audit_id
    rec = {**data.model_dump(), "id": _audit_id, "timestamp": datetime.now(timezone.utc)}
    _audit_logs[_audit_id] = rec
    _audit_id += 1
    return AuditLogEntry(**rec)


def list_audit_logs(
    module: Optional[str] = None,
    action: Optional[AuditAction] = None,
    username: Optional[str] = None,
) -> List[AuditLogEntry]:
    result = list(_audit_logs.values())
    if module is not None:
        result = [r for r in result if r["module"] == module]
    if action is not None:
        result = [r for r in result if r["action"] == action]
    if username is not None:
        result = [r for r in result if r["username"] == username]
    return [AuditLogEntry(**r) for r in result]


# ── System Config ─────────────────────────────────────────────────────────────

def set_config(data: SystemConfigCreate) -> SystemConfig:
    global _config_id
    now = datetime.now(timezone.utc)
    existing = _configs.get(data.key)
    if existing:
        existing["value"] = data.value
        existing["description"] = data.description
        existing["updated_at"] = now
        return SystemConfig(**existing)
    rec = {**data.model_dump(), "id": _config_id, "updated_at": now}
    _configs[data.key] = rec
    _config_id += 1
    return SystemConfig(**rec)


def get_config(key: str) -> Optional[SystemConfig]:
    rec = _configs.get(key)
    return SystemConfig(**rec) if rec else None


def list_configs() -> List[SystemConfig]:
    return [SystemConfig(**r) for r in _configs.values()]


def update_config(key: str, data: SystemConfigUpdate) -> Optional[SystemConfig]:
    rec = _configs.get(key)
    if not rec:
        return None
    for field, value in data.model_dump(exclude_none=True).items():
        rec[field] = value
    rec["updated_at"] = datetime.now(timezone.utc)
    return SystemConfig(**rec)


# ── Integration ───────────────────────────────────────────────────────────────

def create_integration(data: IntegrationCreate) -> Integration:
    global _integ_id
    rec = {**data.model_dump(), "id": _integ_id,
           "created_at": datetime.now(timezone.utc), "last_synced_at": None}
    _integrations[_integ_id] = rec
    _integ_id += 1
    return Integration(**rec)


def list_integrations(status: Optional[IntegrationStatus] = None) -> List[Integration]:
    result = list(_integrations.values())
    if status is not None:
        result = [r for r in result if r["status"] == status]
    return [Integration(**r) for r in result]


def sync_integration(integ_id: int) -> Optional[Integration]:
    rec = _integrations.get(integ_id)
    if not rec:
        return None
    rec["status"] = IntegrationStatus.active
    rec["last_synced_at"] = datetime.now(timezone.utc)
    return Integration(**rec)


# ── Notifications ─────────────────────────────────────────────────────────────

def create_notification(data: NotificationCreate) -> Notification:
    global _notif_id
    rec = {**data.model_dump(), "id": _notif_id,
           "is_read": False,
           "created_at": datetime.now(timezone.utc)}
    _notifications[_notif_id] = rec
    _notif_id += 1
    return Notification(**rec)


def list_notifications(is_read: Optional[bool] = None) -> List[Notification]:
    result = list(_notifications.values())
    if is_read is not None:
        result = [r for r in result if r["is_read"] == is_read]
    return [Notification(**r) for r in result]


def mark_notification_read(notif_id: int) -> Optional[Notification]:
    rec = _notifications.get(notif_id)
    if not rec:
        return None
    rec["is_read"] = True
    return Notification(**rec)
