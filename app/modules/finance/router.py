"""
Module 2 – Financial Management
Routes: /finance
"""

from typing import List, Optional

from fastapi import APIRouter, HTTPException, status

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
from app.modules.finance.service import (
    create_budget,
    create_expense,
    create_invoice,
    get_budget,
    get_financial_summary,
    get_invoice,
    list_budgets,
    list_expenses,
    list_invoices,
    mark_invoice_paid,
    update_budget,
)

router = APIRouter(prefix="/finance", tags=["Module 2 – Financial Management"])


# ── Budgets ───────────────────────────────────────────────────────────────────

@router.post("/budgets", response_model=Budget, status_code=status.HTTP_201_CREATED)
def create_budget_route(data: BudgetCreate):
    return create_budget(data)


@router.get("/budgets", response_model=List[Budget])
def list_budgets_route():
    return list_budgets()


@router.get("/budgets/{budget_id}", response_model=Budget)
def get_budget_route(budget_id: int):
    budget = get_budget(budget_id)
    if not budget:
        raise HTTPException(status_code=404, detail="Budget not found")
    return budget


@router.patch("/budgets/{budget_id}", response_model=Budget)
def update_budget_route(budget_id: int, data: BudgetUpdate):
    budget = update_budget(budget_id, data)
    if not budget:
        raise HTTPException(status_code=404, detail="Budget not found")
    return budget


# ── Expenses ──────────────────────────────────────────────────────────────────

@router.post("/expenses", response_model=Expense, status_code=status.HTTP_201_CREATED)
def create_expense_route(data: ExpenseCreate):
    return create_expense(data)


@router.get("/expenses", response_model=List[Expense])
def list_expenses_route(budget_id: Optional[int] = None):
    return list_expenses(budget_id)


# ── Invoices ──────────────────────────────────────────────────────────────────

@router.post("/invoices", response_model=Invoice, status_code=status.HTTP_201_CREATED)
def create_invoice_route(data: InvoiceCreate):
    return create_invoice(data)


@router.get("/invoices", response_model=List[Invoice])
def list_invoices_route(status: Optional[InvoiceStatus] = None):
    return list_invoices(status)


@router.get("/invoices/{invoice_id}", response_model=Invoice)
def get_invoice_route(invoice_id: int):
    invoice = get_invoice(invoice_id)
    if not invoice:
        raise HTTPException(status_code=404, detail="Invoice not found")
    return invoice


@router.post("/invoices/{invoice_id}/pay", response_model=Invoice)
def pay_invoice_route(invoice_id: int):
    invoice = mark_invoice_paid(invoice_id)
    if not invoice:
        raise HTTPException(status_code=404, detail="Invoice not found")
    return invoice


# ── Summary ───────────────────────────────────────────────────────────────────

@router.get("/summary/{fiscal_year}", response_model=FinancialSummary)
def financial_summary_route(fiscal_year: int):
    return get_financial_summary(fiscal_year)
