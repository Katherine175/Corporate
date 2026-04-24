"""
Module 4 – Project Management
Models: Project, Task, Milestone
"""

from __future__ import annotations

from datetime import date, datetime
from enum import Enum
from typing import List, Optional

from pydantic import BaseModel, Field


class ProjectStatus(str, Enum):
    planning = "planning"
    active = "active"
    on_hold = "on_hold"
    completed = "completed"
    cancelled = "cancelled"


class TaskStatus(str, Enum):
    todo = "todo"
    in_progress = "in_progress"
    review = "review"
    done = "done"


class TaskPriority(str, Enum):
    low = "low"
    medium = "medium"
    high = "high"
    critical = "critical"


# ── Project ───────────────────────────────────────────────────────────────────

class ProjectBase(BaseModel):
    name: str
    description: Optional[str] = None
    owner_id: int
    budget_id: Optional[int] = None
    start_date: date
    end_date: Optional[date] = None
    status: ProjectStatus = ProjectStatus.planning


class ProjectCreate(ProjectBase):
    pass


class ProjectUpdate(BaseModel):
    name: Optional[str] = None
    description: Optional[str] = None
    end_date: Optional[date] = None
    status: Optional[ProjectStatus] = None


class Project(ProjectBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True


# ── Milestone ─────────────────────────────────────────────────────────────────

class MilestoneBase(BaseModel):
    project_id: int
    name: str
    due_date: date
    is_completed: bool = False


class MilestoneCreate(MilestoneBase):
    pass


class Milestone(MilestoneBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True


# ── Task ──────────────────────────────────────────────────────────────────────

class TaskBase(BaseModel):
    project_id: int
    title: str
    description: Optional[str] = None
    assignee_id: Optional[int] = None
    status: TaskStatus = TaskStatus.todo
    priority: TaskPriority = TaskPriority.medium
    due_date: Optional[date] = None
    estimated_hours: float = Field(0.0, ge=0)


class TaskCreate(TaskBase):
    pass


class TaskUpdate(BaseModel):
    title: Optional[str] = None
    description: Optional[str] = None
    assignee_id: Optional[int] = None
    status: Optional[TaskStatus] = None
    priority: Optional[TaskPriority] = None
    due_date: Optional[date] = None
    estimated_hours: Optional[float] = Field(None, ge=0)


class Task(TaskBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True
