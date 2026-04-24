"""
Module 3 – Human Resources
Models: Employee, Department, Payroll, LeaveRequest
"""

from __future__ import annotations

from datetime import date, datetime
from enum import Enum
from typing import Optional

from pydantic import BaseModel, EmailStr, Field


class EmploymentStatus(str, Enum):
    active = "active"
    on_leave = "on_leave"
    terminated = "terminated"


class LeaveType(str, Enum):
    vacation = "vacation"
    sick = "sick"
    personal = "personal"
    maternity_paternity = "maternity_paternity"
    unpaid = "unpaid"


class LeaveStatus(str, Enum):
    pending = "pending"
    approved = "approved"
    rejected = "rejected"


class PayrollStatus(str, Enum):
    draft = "draft"
    processed = "processed"
    paid = "paid"


# ── Department ────────────────────────────────────────────────────────────────

class DepartmentBase(BaseModel):
    name: str
    cost_center: str
    manager_id: Optional[int] = None


class DepartmentCreate(DepartmentBase):
    pass


class Department(DepartmentBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True


# ── Employee ──────────────────────────────────────────────────────────────────

class EmployeeBase(BaseModel):
    full_name: str
    email: EmailStr
    job_title: str
    department_id: int
    hire_date: date
    salary: float = Field(..., gt=0)
    status: EmploymentStatus = EmploymentStatus.active


class EmployeeCreate(EmployeeBase):
    pass


class EmployeeUpdate(BaseModel):
    job_title: Optional[str] = None
    department_id: Optional[int] = None
    salary: Optional[float] = Field(None, gt=0)
    status: Optional[EmploymentStatus] = None


class Employee(EmployeeBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True


# ── Payroll ───────────────────────────────────────────────────────────────────

class PayrollBase(BaseModel):
    employee_id: int
    period_start: date
    period_end: date
    gross_salary: float = Field(..., gt=0)
    deductions: float = 0.0
    net_salary: float = 0.0
    status: PayrollStatus = PayrollStatus.draft


class PayrollCreate(PayrollBase):
    pass


class Payroll(PayrollBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True


# ── Leave Request ─────────────────────────────────────────────────────────────

class LeaveRequestBase(BaseModel):
    employee_id: int
    leave_type: LeaveType
    start_date: date
    end_date: date
    reason: Optional[str] = None


class LeaveRequestCreate(LeaveRequestBase):
    pass


class LeaveRequest(LeaveRequestBase):
    id: int
    status: LeaveStatus = LeaveStatus.pending
    created_at: datetime

    class Config:
        from_attributes = True
