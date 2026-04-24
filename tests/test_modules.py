"""
Tests for all seven functional modules of the
Smart Corporate Resource & Financial Optimizer (SaaS)
"""

from datetime import date

import pytest
from fastapi.testclient import TestClient

from app.main import app

client = TestClient(app)


# ── Health ────────────────────────────────────────────────────────────────────

def test_root_lists_seven_modules():
    resp = client.get("/")
    assert resp.status_code == 200
    body = resp.json()
    assert body["status"] == "running"
    assert len(body["modules"]) == 7
    assert set(body["modules"]) == {
        "auth", "finance", "hr", "projects", "assets", "analytics", "admin"
    }


def test_health_endpoint():
    resp = client.get("/health")
    assert resp.status_code == 200
    assert resp.json() == {"status": "ok"}


# ── Module 1: Auth ────────────────────────────────────────────────────────────

def test_auth_register_and_login():
    payload = {
        "username": "jdoe",
        "email": "jdoe@example.com",
        "full_name": "John Doe",
        "role": "manager",
        "is_active": True,
        "password": "securepass123",
    }
    resp = client.post("/auth/register", json=payload)
    assert resp.status_code == 201
    user = resp.json()
    assert user["username"] == "jdoe"
    assert user["role"] == "manager"
    assert "password" not in user


def test_auth_login_valid():
    client.post("/auth/register", json={
        "username": "logintest",
        "email": "logintest@example.com",
        "full_name": "Login Test",
        "password": "mypassword1",
    })
    resp = client.post("/auth/login", json={"username": "logintest", "password": "mypassword1"})
    assert resp.status_code == 200
    body = resp.json()
    assert "access_token" in body
    assert body["token_type"] == "bearer"


def test_auth_login_invalid():
    resp = client.post("/auth/login", json={"username": "nobody", "password": "wrong"})
    assert resp.status_code == 401


def test_auth_list_users():
    resp = client.get("/auth/users")
    assert resp.status_code == 200
    assert isinstance(resp.json(), list)


# ── Module 2: Finance ─────────────────────────────────────────────────────────

def test_finance_budget_lifecycle():
    budget_payload = {
        "name": "Q1 Budget",
        "fiscal_year": 2026,
        "total_amount": 100000.0,
        "department": "Engineering",
        "status": "draft",
    }
    resp = client.post("/finance/budgets", json=budget_payload)
    assert resp.status_code == 201
    budget = resp.json()
    budget_id = budget["id"]
    assert budget["name"] == "Q1 Budget"
    assert budget["remaining_amount"] == 100000.0

    # Update status
    resp = client.patch(f"/finance/budgets/{budget_id}", json={"status": "approved"})
    assert resp.status_code == 200
    assert resp.json()["status"] == "approved"

    # List
    resp = client.get("/finance/budgets")
    assert resp.status_code == 200
    assert len(resp.json()) >= 1


def test_finance_expense():
    # Create budget first
    budget = client.post("/finance/budgets", json={
        "name": "Expense Budget",
        "fiscal_year": 2026,
        "total_amount": 50000.0,
        "department": "HR",
        "status": "active",
    }).json()

    expense_payload = {
        "description": "Office supplies",
        "amount": 500.0,
        "category": "operations",
        "budget_id": budget["id"],
        "expense_date": str(date.today()),
        "submitted_by": "jdoe",
    }
    resp = client.post("/finance/expenses", json=expense_payload)
    assert resp.status_code == 201
    assert resp.json()["amount"] == 500.0


def test_finance_invoice_lifecycle():
    payload = {
        "invoice_number": "INV-001",
        "vendor": "Acme Corp",
        "amount": 2500.0,
        "due_date": "2026-12-31",
        "status": "pending",
    }
    resp = client.post("/finance/invoices", json=payload)
    assert resp.status_code == 201
    inv_id = resp.json()["id"]

    resp = client.post(f"/finance/invoices/{inv_id}/pay")
    assert resp.status_code == 200
    assert resp.json()["status"] == "paid"


def test_finance_summary():
    resp = client.get("/finance/summary/2026")
    assert resp.status_code == 200
    body = resp.json()
    assert body["fiscal_year"] == 2026
    assert "total_budget" in body
    assert "budget_utilization_pct" in body


# ── Module 3: HR ──────────────────────────────────────────────────────────────

def test_hr_department_and_employee():
    dept = client.post("/hr/departments", json={
        "name": "Engineering",
        "cost_center": "CC-001",
    }).json()
    assert dept["name"] == "Engineering"

    emp = client.post("/hr/employees", json={
        "full_name": "Alice Smith",
        "email": "alice@example.com",
        "job_title": "Engineer",
        "department_id": dept["id"],
        "hire_date": "2024-01-15",
        "salary": 75000.0,
    }).json()
    assert emp["full_name"] == "Alice Smith"

    # Update
    resp = client.patch(f"/hr/employees/{emp['id']}", json={"salary": 80000.0})
    assert resp.status_code == 200
    assert resp.json()["salary"] == 80000.0


def test_hr_payroll():
    dept = client.post("/hr/departments", json={"name": "Finance", "cost_center": "CC-002"}).json()
    emp = client.post("/hr/employees", json={
        "full_name": "Bob Jones",
        "email": "bob@example.com",
        "job_title": "Accountant",
        "department_id": dept["id"],
        "hire_date": "2023-06-01",
        "salary": 60000.0,
    }).json()

    payroll = client.post("/hr/payroll", json={
        "employee_id": emp["id"],
        "period_start": "2026-01-01",
        "period_end": "2026-01-31",
        "gross_salary": 5000.0,
        "deductions": 500.0,
        "net_salary": 4500.0,
        "status": "draft",
    }).json()
    assert payroll["net_salary"] == 4500.0

    resp = client.post(f"/hr/payroll/{payroll['id']}/process")
    assert resp.status_code == 200
    assert resp.json()["status"] == "processed"


def test_hr_leave_request():
    dept = client.post("/hr/departments", json={"name": "Marketing", "cost_center": "CC-003"}).json()
    emp = client.post("/hr/employees", json={
        "full_name": "Carol Lee",
        "email": "carol@example.com",
        "job_title": "Marketer",
        "department_id": dept["id"],
        "hire_date": "2022-03-01",
        "salary": 55000.0,
    }).json()

    leave = client.post("/hr/leave", json={
        "employee_id": emp["id"],
        "leave_type": "vacation",
        "start_date": "2026-07-01",
        "end_date": "2026-07-10",
    }).json()
    assert leave["status"] == "pending"

    resp = client.post(f"/hr/leave/{leave['id']}/approve")
    assert resp.status_code == 200
    assert resp.json()["status"] == "approved"


# ── Module 4: Projects ────────────────────────────────────────────────────────

def test_projects_lifecycle():
    proj = client.post("/projects", json={
        "name": "Platform Rebuild",
        "owner_id": 1,
        "start_date": "2026-01-01",
        "status": "planning",
    }).json()
    assert proj["name"] == "Platform Rebuild"
    proj_id = proj["id"]

    # Update status
    resp = client.patch(f"/projects/{proj_id}", json={"status": "active"})
    assert resp.status_code == 200
    assert resp.json()["status"] == "active"

    # Milestone
    mile = client.post(f"/projects/{proj_id}/milestones", json={
        "project_id": proj_id,
        "name": "MVP Release",
        "due_date": "2026-06-30",
    }).json()
    assert mile["name"] == "MVP Release"
    assert mile["is_completed"] is False

    resp = client.post(f"/projects/milestones/{mile['id']}/complete")
    assert resp.status_code == 200
    assert resp.json()["is_completed"] is True


def test_projects_tasks():
    proj = client.post("/projects", json={
        "name": "Task Project",
        "owner_id": 2,
        "start_date": "2026-02-01",
    }).json()

    task = client.post("/projects/tasks", json={
        "project_id": proj["id"],
        "title": "Design API",
        "priority": "high",
        "status": "todo",
    }).json()
    assert task["title"] == "Design API"

    resp = client.patch(f"/projects/tasks/{task['id']}", json={"status": "in_progress"})
    assert resp.status_code == 200
    assert resp.json()["status"] == "in_progress"


# ── Module 5: Assets ──────────────────────────────────────────────────────────

def test_assets_lifecycle():
    asset = client.post("/assets", json={
        "name": "MacBook Pro",
        "category": "hardware",
        "purchase_date": "2024-01-10",
        "purchase_cost": 2499.0,
        "current_value": 1800.0,
        "status": "available",
    }).json()
    assert asset["name"] == "MacBook Pro"
    asset_id = asset["id"]

    resp = client.patch(f"/assets/{asset_id}", json={"status": "under_maintenance"})
    assert resp.status_code == 200
    assert resp.json()["status"] == "under_maintenance"


def test_inventory():
    item = client.post("/assets/inventory", json={
        "name": "USB-C Cable",
        "sku": "USB-C-001",
        "quantity": 50,
        "unit_cost": 9.99,
        "reorder_level": 10,
    }).json()
    assert item["needs_reorder"] is False

    resp = client.patch(f"/assets/inventory/{item['id']}", json={"quantity": 5})
    assert resp.status_code == 200
    assert resp.json()["needs_reorder"] is True


def test_resource_allocation():
    asset = client.post("/assets", json={
        "name": "Projector",
        "category": "equipment",
        "purchase_date": "2023-05-01",
        "purchase_cost": 800.0,
        "current_value": 600.0,
        "status": "available",
    }).json()

    alloc = client.post("/assets/allocations", json={
        "asset_id": asset["id"],
        "employee_id": 1,
        "allocated_from": "2026-04-01",
    }).json()
    assert alloc["status"] == "active"

    resp = client.post(f"/assets/allocations/{alloc['id']}/return")
    assert resp.status_code == 200
    assert resp.json()["status"] == "returned"


# ── Module 6: Analytics ───────────────────────────────────────────────────────

def test_analytics_kpis():
    kpi = client.post("/analytics/kpis", json={
        "name": "Revenue Growth",
        "category": "financial",
        "value": 15.5,
        "unit": "%",
        "target": 10.0,
    }).json()
    assert kpi["achieved"] is True

    resp = client.get("/analytics/kpis?category=financial")
    assert resp.status_code == 200
    assert len(resp.json()) >= 1


def test_analytics_reports():
    report = client.post("/analytics/reports", json={
        "title": "Q1 Financial Report",
        "module": "finance",
        "format": "summary",
        "filters": {"fiscal_year": 2026},
    }).json()
    assert report["status"] == "generated"
    report_id = report["id"]

    resp = client.get(f"/analytics/reports/{report_id}")
    assert resp.status_code == 200


def test_analytics_dashboard():
    resp = client.get("/analytics/dashboard")
    assert resp.status_code == 200
    body = resp.json()
    assert "widgets" in body
    assert len(body["widgets"]) > 0


# ── Module 7: Admin ───────────────────────────────────────────────────────────

def test_admin_audit_log():
    resp = client.post("/admin/audit-logs", json={
        "username": "admin",
        "action": "login",
        "module": "auth",
        "ip_address": "127.0.0.1",
    })
    assert resp.status_code == 201
    assert resp.json()["action"] == "login"

    resp = client.get("/admin/audit-logs?module=auth")
    assert resp.status_code == 200
    assert len(resp.json()) >= 1


def test_admin_system_config():
    resp = client.post("/admin/config", json={
        "key": "max_users",
        "value": "500",
        "description": "Maximum allowed users",
    })
    assert resp.status_code == 201
    assert resp.json()["key"] == "max_users"

    resp = client.get("/admin/config/max_users")
    assert resp.status_code == 200
    assert resp.json()["value"] == "500"

    resp = client.patch("/admin/config/max_users", json={"value": "1000"})
    assert resp.status_code == 200
    assert resp.json()["value"] == "1000"


def test_admin_integration():
    integ = client.post("/admin/integrations", json={
        "name": "Slack",
        "provider": "slack",
        "endpoint_url": "https://hooks.slack.com/services/xxx",
        "status": "inactive",
    }).json()
    integ_id = integ["id"]
    assert integ["status"] == "inactive"

    resp = client.post(f"/admin/integrations/{integ_id}/sync")
    assert resp.status_code == 200
    assert resp.json()["status"] == "active"


def test_admin_notifications():
    notif = client.post("/admin/notifications", json={
        "title": "System Update",
        "message": "The system will be updated tonight.",
        "severity": "info",
    }).json()
    notif_id = notif["id"]
    assert notif["is_read"] is False

    resp = client.post(f"/admin/notifications/{notif_id}/read")
    assert resp.status_code == 200
    assert resp.json()["is_read"] is True

    resp = client.get("/admin/notifications?is_read=false")
    assert resp.status_code == 200
