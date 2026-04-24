"""
Module 7 – System Administration
Routes: /admin
"""

from typing import List, Optional

from fastapi import APIRouter, HTTPException, status

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
from app.modules.admin.service import (
    create_integration,
    create_notification,
    get_config,
    list_audit_logs,
    list_configs,
    list_integrations,
    list_notifications,
    log_action,
    mark_notification_read,
    set_config,
    sync_integration,
    update_config,
)

router = APIRouter(prefix="/admin", tags=["Module 7 – System Administration"])


# ── Audit Logs ────────────────────────────────────────────────────────────────

@router.post("/audit-logs", response_model=AuditLogEntry, status_code=status.HTTP_201_CREATED)
def create_audit_log(data: AuditLogCreate):
    return log_action(data)


@router.get("/audit-logs", response_model=List[AuditLogEntry])
def get_audit_logs(
    module: Optional[str] = None,
    action: Optional[AuditAction] = None,
    username: Optional[str] = None,
):
    return list_audit_logs(module, action, username)


# ── System Config ─────────────────────────────────────────────────────────────

@router.post("/config", response_model=SystemConfig, status_code=status.HTTP_201_CREATED)
def create_config(data: SystemConfigCreate):
    return set_config(data)


@router.get("/config", response_model=List[SystemConfig])
def get_configs():
    return list_configs()


@router.get("/config/{key}", response_model=SystemConfig)
def get_config_route(key: str):
    config = get_config(key)
    if not config:
        raise HTTPException(status_code=404, detail="Config key not found")
    return config


@router.patch("/config/{key}", response_model=SystemConfig)
def update_config_route(key: str, data: SystemConfigUpdate):
    config = update_config(key, data)
    if not config:
        raise HTTPException(status_code=404, detail="Config key not found")
    return config


# ── Integrations ──────────────────────────────────────────────────────────────

@router.post("/integrations", response_model=Integration, status_code=status.HTTP_201_CREATED)
def create_integ(data: IntegrationCreate):
    return create_integration(data)


@router.get("/integrations", response_model=List[Integration])
def list_integ(integ_status: Optional[IntegrationStatus] = None):
    return list_integrations(integ_status)


@router.post("/integrations/{integ_id}/sync", response_model=Integration)
def sync_integ(integ_id: int):
    integ = sync_integration(integ_id)
    if not integ:
        raise HTTPException(status_code=404, detail="Integration not found")
    return integ


# ── Notifications ─────────────────────────────────────────────────────────────

@router.post("/notifications", response_model=Notification, status_code=status.HTTP_201_CREATED)
def create_notif(data: NotificationCreate):
    return create_notification(data)


@router.get("/notifications", response_model=List[Notification])
def list_notif(is_read: Optional[bool] = None):
    return list_notifications(is_read)


@router.post("/notifications/{notif_id}/read", response_model=Notification)
def read_notif(notif_id: int):
    notif = mark_notification_read(notif_id)
    if not notif:
        raise HTTPException(status_code=404, detail="Notification not found")
    return notif
