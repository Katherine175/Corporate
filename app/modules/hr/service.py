"""
Module 3 – Human Resources
Service: employees, departments, payroll, leave
"""

from __future__ import annotations

from datetime import datetime, timezone
from typing import Dict, List, Optional

from app.modules.hr.models import (
    Department,
    DepartmentCreate,
    Employee,
    EmployeeCreate,
    EmployeeUpdate,
    LeaveRequest,
    LeaveRequestCreate,
    LeaveStatus,
    Payroll,
    PayrollCreate,
    PayrollStatus,
)

_departments: Dict[int, dict] = {}
_employees: Dict[int, dict] = {}
_payrolls: Dict[int, dict] = {}
_leaves: Dict[int, dict] = {}
_dept_id = 1
_emp_id = 1
_pay_id = 1
_leave_id = 1


# ── Department ────────────────────────────────────────────────────────────────

def create_department(data: DepartmentCreate) -> Department:
    global _dept_id
    rec = {**data.model_dump(), "id": _dept_id, "created_at": datetime.now(timezone.utc)}
    _departments[_dept_id] = rec
    _dept_id += 1
    return Department(**rec)


def list_departments() -> List[Department]:
    return [Department(**r) for r in _departments.values()]


def get_department(dept_id: int) -> Optional[Department]:
    rec = _departments.get(dept_id)
    return Department(**rec) if rec else None


# ── Employee ──────────────────────────────────────────────────────────────────

def create_employee(data: EmployeeCreate) -> Employee:
    global _emp_id
    rec = {**data.model_dump(), "id": _emp_id, "created_at": datetime.now(timezone.utc)}
    _employees[_emp_id] = rec
    _emp_id += 1
    return Employee(**rec)


def get_employee(emp_id: int) -> Optional[Employee]:
    rec = _employees.get(emp_id)
    return Employee(**rec) if rec else None


def list_employees(department_id: Optional[int] = None) -> List[Employee]:
    result = list(_employees.values())
    if department_id is not None:
        result = [r for r in result if r["department_id"] == department_id]
    return [Employee(**r) for r in result]


def update_employee(emp_id: int, data: EmployeeUpdate) -> Optional[Employee]:
    rec = _employees.get(emp_id)
    if not rec:
        return None
    for field, value in data.model_dump(exclude_none=True).items():
        rec[field] = value
    _employees[emp_id] = rec
    return Employee(**rec)


# ── Payroll ───────────────────────────────────────────────────────────────────

def create_payroll(data: PayrollCreate) -> Payroll:
    global _pay_id
    rec = {**data.model_dump(), "id": _pay_id,
           "net_salary": data.gross_salary - data.deductions,
           "created_at": datetime.now(timezone.utc)}
    _payrolls[_pay_id] = rec
    _pay_id += 1
    return Payroll(**rec)


def list_payrolls(employee_id: Optional[int] = None) -> List[Payroll]:
    result = list(_payrolls.values())
    if employee_id is not None:
        result = [r for r in result if r["employee_id"] == employee_id]
    return [Payroll(**r) for r in result]


def process_payroll(payroll_id: int) -> Optional[Payroll]:
    rec = _payrolls.get(payroll_id)
    if not rec:
        return None
    rec["status"] = PayrollStatus.processed
    return Payroll(**rec)


# ── Leave Request ─────────────────────────────────────────────────────────────

def create_leave_request(data: LeaveRequestCreate) -> LeaveRequest:
    global _leave_id
    rec = {**data.model_dump(), "id": _leave_id,
           "status": LeaveStatus.pending,
           "created_at": datetime.now(timezone.utc)}
    _leaves[_leave_id] = rec
    _leave_id += 1
    return LeaveRequest(**rec)


def list_leave_requests(employee_id: Optional[int] = None) -> List[LeaveRequest]:
    result = list(_leaves.values())
    if employee_id is not None:
        result = [r for r in result if r["employee_id"] == employee_id]
    return [LeaveRequest(**r) for r in result]


def approve_leave(leave_id: int) -> Optional[LeaveRequest]:
    rec = _leaves.get(leave_id)
    if not rec:
        return None
    rec["status"] = LeaveStatus.approved
    return LeaveRequest(**rec)


def reject_leave(leave_id: int) -> Optional[LeaveRequest]:
    rec = _leaves.get(leave_id)
    if not rec:
        return None
    rec["status"] = LeaveStatus.rejected
    return LeaveRequest(**rec)
