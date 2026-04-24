"""
Module 6 – Analytics & Reporting
Routes: /analytics
"""

from typing import List, Optional

from fastapi import APIRouter, HTTPException

from app.modules.analytics.models import (
    Dashboard,
    KPI,
    KPICategory,
    KPICreate,
    Report,
    ReportRequest,
)
from app.modules.analytics.service import (
    generate_report,
    get_dashboard,
    get_report,
    list_kpis,
    list_reports,
    record_kpi,
)

router = APIRouter(prefix="/analytics", tags=["Module 6 – Analytics & Reporting"])


# ── KPIs ──────────────────────────────────────────────────────────────────────

@router.post("/kpis", response_model=KPI, status_code=201)
def create_kpi(data: KPICreate):
    return record_kpi(data)


@router.get("/kpis", response_model=List[KPI])
def list_kpis_route(category: Optional[KPICategory] = None):
    return list_kpis(category)


# ── Reports ───────────────────────────────────────────────────────────────────

@router.post("/reports", response_model=Report, status_code=201)
def create_report(request: ReportRequest):
    return generate_report(request)


@router.get("/reports", response_model=List[Report])
def list_reports_route():
    return list_reports()


@router.get("/reports/{report_id}", response_model=Report)
def get_report_route(report_id: int):
    report = get_report(report_id)
    if not report:
        raise HTTPException(status_code=404, detail="Report not found")
    return report


# ── Dashboard ─────────────────────────────────────────────────────────────────

@router.get("/dashboard", response_model=Dashboard)
def dashboard():
    return get_dashboard()
