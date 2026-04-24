"""
Module 2 – Financial Management
Service: budgets, expenses, invoices
"""

from __future__ import annotations

from datetime import date, datetime, timezone
from typing import Dict, List, Optional

from app.modules.finance.models import (
    Budget,
    BudgetCreate,
    BudgetUpdate,
    Expense,
    ExpenseCreate,
    FinancialSummary,
    Invoice,
    InvoiceCreate,
    InvoiceStatus,
)

_budgets: Dict[int, dict] = {}
_expenses: Dict[int, dict] = {}
_invoices: Dict[int, dict] = {}
_budget_id = 1
_expense_id = 1
_invoice_id = 1


# ── Budget ────────────────────────────────────────────────────────────────────

def create_budget(data: BudgetCreate) -> Budget:
    global _budget_id
    now = datetime.now(timezone.utc)
    rec = {**data.model_dump(), "id": _budget_id, "spent_amount": 0.0,
           "remaining_amount": data.total_amount, "created_at": now}
    _budgets[_budget_id] = rec
    _budget_id += 1
    return Budget(**rec)


def get_budget(budget_id: int) -> Optional[Budget]:
    rec = _budgets.get(budget_id)
    return Budget(**rec) if rec else None


def list_budgets() -> List[Budget]:
    return [Budget(**r) for r in _budgets.values()]


def update_budget(budget_id: int, data: BudgetUpdate) -> Optional[Budget]:
    rec = _budgets.get(budget_id)
    if not rec:
        return None
    for field, value in data.model_dump(exclude_none=True).items():
        rec[field] = value
    if "total_amount" in data.model_dump(exclude_none=True):
        rec["remaining_amount"] = rec["total_amount"] - rec["spent_amount"]
    _budgets[budget_id] = rec
    return Budget(**rec)


# ── Expense ───────────────────────────────────────────────────────────────────

def create_expense(data: ExpenseCreate) -> Expense:
    global _expense_id
    now = datetime.now(timezone.utc)
    rec = {**data.model_dump(), "id": _expense_id, "created_at": now}
    _expenses[_expense_id] = rec
    _expense_id += 1
    # Update budget spent
    budget = _budgets.get(data.budget_id)
    if budget:
        budget["spent_amount"] += data.amount
        budget["remaining_amount"] = budget["total_amount"] - budget["spent_amount"]
    return Expense(**rec)


def list_expenses(budget_id: Optional[int] = None) -> List[Expense]:
    result = list(_expenses.values())
    if budget_id is not None:
        result = [r for r in result if r["budget_id"] == budget_id]
    return [Expense(**r) for r in result]


# ── Invoice ───────────────────────────────────────────────────────────────────

def create_invoice(data: InvoiceCreate) -> Invoice:
    global _invoice_id
    now = datetime.now(timezone.utc)
    rec = {**data.model_dump(), "id": _invoice_id, "created_at": now}
    _invoices[_invoice_id] = rec
    _invoice_id += 1
    return Invoice(**rec)


def get_invoice(invoice_id: int) -> Optional[Invoice]:
    rec = _invoices.get(invoice_id)
    return Invoice(**rec) if rec else None


def list_invoices(status: Optional[InvoiceStatus] = None) -> List[Invoice]:
    result = list(_invoices.values())
    if status is not None:
        result = [r for r in result if r["status"] == status]
    return [Invoice(**r) for r in result]


def mark_invoice_paid(invoice_id: int) -> Optional[Invoice]:
    rec = _invoices.get(invoice_id)
    if not rec:
        return None
    rec["status"] = InvoiceStatus.paid
    return Invoice(**rec)


# ── Summary ───────────────────────────────────────────────────────────────────

def get_financial_summary(fiscal_year: int) -> FinancialSummary:
    year_budgets = [b for b in _budgets.values() if b["fiscal_year"] == fiscal_year]
    total_budget = sum(b["total_amount"] for b in year_budgets)
    total_spent = sum(b["spent_amount"] for b in year_budgets)
    invoices_all = list(_invoices.values())
    total_invoiced = sum(i["amount"] for i in invoices_all)
    pending = sum(1 for i in invoices_all if i["status"] == InvoiceStatus.pending)
    overdue = sum(1 for i in invoices_all if i["status"] == InvoiceStatus.overdue)
    utilization = (total_spent / total_budget * 100) if total_budget > 0 else 0.0
    return FinancialSummary(
        fiscal_year=fiscal_year,
        total_budget=total_budget,
        total_spent=total_spent,
        total_invoiced=total_invoiced,
        pending_invoices=pending,
        overdue_invoices=overdue,
        budget_utilization_pct=round(utilization, 2),
    )
