"""
Module 6 – Analytics & Reporting
Service: KPIs, reports, dashboards
"""

from __future__ import annotations

from datetime import datetime, timezone
from typing import Any, Dict, List, Optional

from app.modules.analytics.models import (
    Dashboard,
    DashboardWidget,
    KPI,
    KPICreate,
    KPICategory,
    Report,
    ReportFormat,
    ReportRequest,
    ReportStatus,
)

_kpis: Dict[int, dict] = {}
_reports: Dict[int, dict] = {}
_kpi_id = 1
_report_id = 1


# ── KPI ───────────────────────────────────────────────────────────────────────

def record_kpi(data: KPICreate) -> KPI:
    global _kpi_id
    now = datetime.now(timezone.utc)
    achieved = (data.target is not None and data.value >= data.target)
    rec = {**data.model_dump(), "id": _kpi_id, "achieved": achieved, "recorded_at": now}
    _kpis[_kpi_id] = rec
    _kpi_id += 1
    return KPI(**rec)


def list_kpis(category: Optional[KPICategory] = None) -> List[KPI]:
    result = list(_kpis.values())
    if category is not None:
        result = [r for r in result if r["category"] == category]
    return [KPI(**r) for r in result]


# ── Report ────────────────────────────────────────────────────────────────────

def generate_report(request: ReportRequest) -> Report:
    global _report_id
    now = datetime.now(timezone.utc)
    data: Dict[str, Any] = {
        "module": request.module,
        "filters": request.filters,
        "record_count": 0,
        "note": f"Report for module '{request.module}' generated successfully.",
    }
    rec = {
        "id": _report_id,
        "title": request.title,
        "module": request.module,
        "format": request.format,
        "status": ReportStatus.generated,
        "data": data,
        "generated_at": now,
        "created_at": now,
    }
    _reports[_report_id] = rec
    _report_id += 1
    return Report(**rec)


def list_reports() -> List[Report]:
    return [Report(**r) for r in _reports.values()]


def get_report(report_id: int) -> Optional[Report]:
    rec = _reports.get(report_id)
    return Report(**rec) if rec else None


# ── Dashboard ─────────────────────────────────────────────────────────────────

def get_dashboard() -> Dashboard:
    """Build an overview dashboard from all available KPIs."""
    now = datetime.now(timezone.utc)
    financial_kpis = [k for k in _kpis.values() if k["category"] == KPICategory.financial]
    hr_kpis = [k for k in _kpis.values() if k["category"] == KPICategory.hr]
    project_kpis = [k for k in _kpis.values() if k["category"] == KPICategory.projects]

    widgets = [
        DashboardWidget(
            title="Total Financial KPIs",
            metric="count",
            value=len(financial_kpis),
            unit="kpis",
        ),
        DashboardWidget(
            title="Total HR KPIs",
            metric="count",
            value=len(hr_kpis),
            unit="kpis",
        ),
        DashboardWidget(
            title="Total Project KPIs",
            metric="count",
            value=len(project_kpis),
            unit="kpis",
        ),
        DashboardWidget(
            title="Reports Generated",
            metric="count",
            value=len(_reports),
            unit="reports",
        ),
        DashboardWidget(
            title="Total KPIs Tracked",
            metric="count",
            value=len(_kpis),
            unit="kpis",
        ),
    ]
    return Dashboard(title="Corporate Overview Dashboard", widgets=widgets, generated_at=now)
