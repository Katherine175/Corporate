"""
Module 2 – Financial Management
Models: Budget, Expense, Invoice, FinancialSummary
"""

from __future__ import annotations

from datetime import date, datetime
from enum import Enum
from typing import List, Optional

from pydantic import BaseModel, Field


class BudgetStatus(str, Enum):
    draft = "draft"
    approved = "approved"
    active = "active"
    closed = "closed"


class ExpenseCategory(str, Enum):
    personnel = "personnel"
    infrastructure = "infrastructure"
    marketing = "marketing"
    operations = "operations"
    technology = "technology"
    other = "other"


class InvoiceStatus(str, Enum):
    pending = "pending"
    paid = "paid"
    overdue = "overdue"
    cancelled = "cancelled"


# ── Budget ────────────────────────────────────────────────────────────────────

class BudgetBase(BaseModel):
    name: str
    fiscal_year: int
    total_amount: float = Field(..., gt=0)
    department: str
    status: BudgetStatus = BudgetStatus.draft


class BudgetCreate(BudgetBase):
    pass


class BudgetUpdate(BaseModel):
    name: Optional[str] = None
    total_amount: Optional[float] = Field(None, gt=0)
    status: Optional[BudgetStatus] = None


class Budget(BudgetBase):
    id: int
    spent_amount: float = 0.0
    remaining_amount: float = 0.0
    created_at: datetime

    class Config:
        from_attributes = True


# ── Expense ───────────────────────────────────────────────────────────────────

class ExpenseBase(BaseModel):
    description: str
    amount: float = Field(..., gt=0)
    category: ExpenseCategory
    budget_id: int
    expense_date: date
    submitted_by: str


class ExpenseCreate(ExpenseBase):
    pass


class Expense(ExpenseBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True


# ── Invoice ───────────────────────────────────────────────────────────────────

class InvoiceBase(BaseModel):
    invoice_number: str
    vendor: str
    amount: float = Field(..., gt=0)
    due_date: date
    status: InvoiceStatus = InvoiceStatus.pending
    description: Optional[str] = None


class InvoiceCreate(InvoiceBase):
    pass


class Invoice(InvoiceBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True


# ── Summary ───────────────────────────────────────────────────────────────────

class FinancialSummary(BaseModel):
    fiscal_year: int
    total_budget: float
    total_spent: float
    total_invoiced: float
    pending_invoices: int
    overdue_invoices: int
    budget_utilization_pct: float
