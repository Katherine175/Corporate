"""
Module 3 – Human Resources
Routes: /hr
"""

from typing import List, Optional

from fastapi import APIRouter, HTTPException, status

from app.modules.hr.models import (
    Department,
    DepartmentCreate,
    Employee,
    EmployeeCreate,
    EmployeeUpdate,
    LeaveRequest,
    LeaveRequestCreate,
    Payroll,
    PayrollCreate,
)
from app.modules.hr.service import (
    approve_leave,
    create_department,
    create_employee,
    create_leave_request,
    create_payroll,
    get_department,
    get_employee,
    list_departments,
    list_employees,
    list_leave_requests,
    list_payrolls,
    process_payroll,
    reject_leave,
    update_employee,
)

router = APIRouter(prefix="/hr", tags=["Module 3 – Human Resources"])


# ── Departments ───────────────────────────────────────────────────────────────

@router.post("/departments", response_model=Department, status_code=status.HTTP_201_CREATED)
def create_dept(data: DepartmentCreate):
    return create_department(data)


@router.get("/departments", response_model=List[Department])
def list_depts():
    return list_departments()


@router.get("/departments/{dept_id}", response_model=Department)
def get_dept(dept_id: int):
    dept = get_department(dept_id)
    if not dept:
        raise HTTPException(status_code=404, detail="Department not found")
    return dept


# ── Employees ─────────────────────────────────────────────────────────────────

@router.post("/employees", response_model=Employee, status_code=status.HTTP_201_CREATED)
def create_emp(data: EmployeeCreate):
    return create_employee(data)


@router.get("/employees", response_model=List[Employee])
def list_emps(department_id: Optional[int] = None):
    return list_employees(department_id)


@router.get("/employees/{emp_id}", response_model=Employee)
def get_emp(emp_id: int):
    emp = get_employee(emp_id)
    if not emp:
        raise HTTPException(status_code=404, detail="Employee not found")
    return emp


@router.patch("/employees/{emp_id}", response_model=Employee)
def update_emp(emp_id: int, data: EmployeeUpdate):
    emp = update_employee(emp_id, data)
    if not emp:
        raise HTTPException(status_code=404, detail="Employee not found")
    return emp


# ── Payroll ───────────────────────────────────────────────────────────────────

@router.post("/payroll", response_model=Payroll, status_code=status.HTTP_201_CREATED)
def create_pay(data: PayrollCreate):
    return create_payroll(data)


@router.get("/payroll", response_model=List[Payroll])
def list_pay(employee_id: Optional[int] = None):
    return list_payrolls(employee_id)


@router.post("/payroll/{payroll_id}/process", response_model=Payroll)
def process_pay(payroll_id: int):
    payroll = process_payroll(payroll_id)
    if not payroll:
        raise HTTPException(status_code=404, detail="Payroll record not found")
    return payroll


# ── Leave ─────────────────────────────────────────────────────────────────────

@router.post("/leave", response_model=LeaveRequest, status_code=status.HTTP_201_CREATED)
def request_leave(data: LeaveRequestCreate):
    return create_leave_request(data)


@router.get("/leave", response_model=List[LeaveRequest])
def list_leave(employee_id: Optional[int] = None):
    return list_leave_requests(employee_id)


@router.post("/leave/{leave_id}/approve", response_model=LeaveRequest)
def approve(leave_id: int):
    leave = approve_leave(leave_id)
    if not leave:
        raise HTTPException(status_code=404, detail="Leave request not found")
    return leave


@router.post("/leave/{leave_id}/reject", response_model=LeaveRequest)
def reject(leave_id: int):
    leave = reject_leave(leave_id)
    if not leave:
        raise HTTPException(status_code=404, detail="Leave request not found")
    return leave
