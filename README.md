# Smart Corporate Resource & Financial Optimizer (SaaS)

A modular SaaS platform whose organic structure is composed of **seven independent functional modules** organised hierarchically and complementarily to ensure the correct functioning of the system.

---

## Architecture – Seven Functional Modules

```
app/
├── main.py                    # Application entry point – registers all 7 modules
└── modules/
    ├── auth/                  # Module 1 – Authentication & Authorization
    ├── finance/               # Module 2 – Financial Management
    ├── hr/                    # Module 3 – Human Resources
    ├── projects/              # Module 4 – Project Management
    ├── assets/                # Module 5 – Asset & Resource Management
    ├── analytics/             # Module 6 – Analytics & Reporting
    └── admin/                 # Module 7 – System Administration
```

Each module is fully self-contained and exposes three layers: **models**, **service**, and **router**.

---

### Module 1 – Authentication & Authorization (`/auth`)
Manages user identity, roles, and access tokens.

| Feature | Description |
|---------|-------------|
| User registration | Create accounts with role assignment (admin / manager / employee / auditor) |
| Login | Authenticate and receive a JWT bearer token |
| Role-based access | `RoleEnum` with four predefined roles |
| Token validation | `jose` JWT encode/decode with configurable expiry |

---

### Module 2 – Financial Management (`/finance`)
Controls the organisation's financial resources end-to-end.

| Feature | Description |
|---------|-------------|
| Budgets | Create, approve, and track departmental budgets by fiscal year |
| Expenses | Record expenses against a budget; remaining balance updated automatically |
| Invoices | Manage vendor invoices through pending → paid lifecycle |
| Financial summary | Aggregated KPIs: total budget, spent, utilisation %, overdue invoices |

---

### Module 3 – Human Resources (`/hr`)
Central registry for all people-related operations.

| Feature | Description |
|---------|-------------|
| Departments | Organisational units with cost-centre codes |
| Employees | Full employee lifecycle (hire, update, terminate) |
| Payroll | Draft → processed → paid payroll records with automatic net-salary calculation |
| Leave management | Submit, approve, or reject leave requests by type |

---

### Module 4 – Project Management (`/projects`)
Tracks the delivery of initiatives from planning to completion.

| Feature | Description |
|---------|-------------|
| Projects | Create and manage projects with status, owner, and optional budget linkage |
| Milestones | Track key delivery checkpoints and mark them complete |
| Tasks | Full task board (todo → in_progress → review → done) with priority and assignee |

---

### Module 5 – Asset & Resource Management (`/assets`)
Governs physical and digital corporate resources.

| Feature | Description |
|---------|-------------|
| Assets | Register hardware, software, vehicles, and equipment; track status and value |
| Inventory | SKU-level stock management with automatic reorder-level alerts |
| Resource allocation | Assign assets to employees or projects; return flow updates asset status |

---

### Module 6 – Analytics & Reporting (`/analytics`)
Provides business intelligence across all modules.

| Feature | Description |
|---------|-------------|
| KPIs | Record key performance indicators by category; auto-flag achieved vs. missed |
| Reports | Generate summary or detailed reports per module with filter parameters |
| Dashboard | Real-time aggregated overview widget panel |

---

### Module 7 – System Administration (`/admin`)
Cross-cutting operational concerns for platform management.

| Feature | Description |
|---------|-------------|
| Audit logs | Immutable action trail (create / read / update / delete / login …) |
| System config | Key-value configuration store (upsert + patch) |
| Integrations | Register third-party endpoints; trigger sync and track status |
| Notifications | Create system notifications; mark as read; filter by read status |

---

## Quick Start

```bash
# Install dependencies
pip install -r requirements.txt

# Run the application
uvicorn app.main:app --reload

# Interactive API docs
open http://127.0.0.1:8000/docs
```

## Running Tests

```bash
pytest tests/test_modules.py -v
```

All **25 tests** cover every module's CRUD operations and key workflows.

---

## Technology Stack

| Layer | Technology |
|-------|-----------|
| Framework | [FastAPI](https://fastapi.tiangolo.com/) |
| Data validation | [Pydantic v2](https://docs.pydantic.dev/) |
| Authentication | JWT via `python-jose`, passwords via `passlib[bcrypt]` |
| Testing | `pytest` + `httpx` TestClient |
