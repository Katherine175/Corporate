"""
Smart Corporate Resource & Financial Optimizer – Main Application
Seven independent functional modules registered hierarchically.
"""

from fastapi import FastAPI

from app.config import settings
from app.modules.admin.router import router as admin_router
from app.modules.analytics.router import router as analytics_router
from app.modules.assets.router import router as assets_router
from app.modules.auth.router import router as auth_router
from app.modules.finance.router import router as finance_router
from app.modules.hr.router import router as hr_router
from app.modules.projects.router import router as projects_router

app = FastAPI(
    title=settings.app_name,
    version=settings.app_version,
    description=(
        "SaaS platform composed of seven independent functional modules "
        "organised hierarchically and complementarily to ensure the correct "
        "functioning of the system.\n\n"
        "| # | Module | Prefix |\n"
        "|---|--------|--------|\n"
        "| 1 | Authentication & Authorization | `/auth` |\n"
        "| 2 | Financial Management | `/finance` |\n"
        "| 3 | Human Resources | `/hr` |\n"
        "| 4 | Project Management | `/projects` |\n"
        "| 5 | Asset & Resource Management | `/assets` |\n"
        "| 6 | Analytics & Reporting | `/analytics` |\n"
        "| 7 | System Administration | `/admin` |\n"
    ),
)

# ── Register the seven modules ────────────────────────────────────────────────
app.include_router(auth_router)       # Module 1
app.include_router(finance_router)    # Module 2
app.include_router(hr_router)         # Module 3
app.include_router(projects_router)   # Module 4
app.include_router(assets_router)     # Module 5
app.include_router(analytics_router)  # Module 6
app.include_router(admin_router)      # Module 7


@app.get("/", tags=["Health"])
def root():
    return {
        "app": settings.app_name,
        "version": settings.app_version,
        "modules": [
            "auth",
            "finance",
            "hr",
            "projects",
            "assets",
            "analytics",
            "admin",
        ],
        "status": "running",
    }


@app.get("/health", tags=["Health"])
def health():
    return {"status": "ok"}
