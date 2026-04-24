"""
Module 6 – Analytics & Reporting
Models: KPI, Report, Dashboard
"""

from __future__ import annotations

from datetime import datetime
from enum import Enum
from typing import Any, Dict, List, Optional

from pydantic import BaseModel


class KPICategory(str, Enum):
    financial = "financial"
    hr = "hr"
    projects = "projects"
    assets = "assets"
    operational = "operational"


class ReportFormat(str, Enum):
    summary = "summary"
    detailed = "detailed"
    chart_data = "chart_data"


class ReportStatus(str, Enum):
    pending = "pending"
    generated = "generated"
    failed = "failed"


# ── KPI ───────────────────────────────────────────────────────────────────────

class KPIBase(BaseModel):
    name: str
    category: KPICategory
    value: float
    unit: str
    target: Optional[float] = None
    description: Optional[str] = None


class KPICreate(KPIBase):
    pass


class KPI(KPIBase):
    id: int
    achieved: bool = False
    recorded_at: datetime

    class Config:
        from_attributes = True


# ── Report ────────────────────────────────────────────────────────────────────

class ReportRequest(BaseModel):
    title: str
    module: str
    format: ReportFormat = ReportFormat.summary
    filters: Dict[str, Any] = {}


class Report(BaseModel):
    id: int
    title: str
    module: str
    format: ReportFormat
    status: ReportStatus
    data: Dict[str, Any] = {}
    generated_at: Optional[datetime] = None
    created_at: datetime

    class Config:
        from_attributes = True


# ── Dashboard ─────────────────────────────────────────────────────────────────

class DashboardWidget(BaseModel):
    title: str
    metric: str
    value: Any
    unit: Optional[str] = None
    trend: Optional[str] = None


class Dashboard(BaseModel):
    title: str
    widgets: List[DashboardWidget]
    generated_at: datetime
