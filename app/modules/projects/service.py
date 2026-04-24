"""
Module 4 – Project Management
Service: projects, tasks, milestones
"""

from __future__ import annotations

from datetime import datetime, timezone
from typing import Dict, List, Optional

from app.modules.projects.models import (
    Milestone,
    MilestoneCreate,
    Project,
    ProjectCreate,
    ProjectUpdate,
    Task,
    TaskCreate,
    TaskUpdate,
)

_projects: Dict[int, dict] = {}
_tasks: Dict[int, dict] = {}
_milestones: Dict[int, dict] = {}
_proj_id = 1
_task_id = 1
_mile_id = 1


# ── Project ───────────────────────────────────────────────────────────────────

def create_project(data: ProjectCreate) -> Project:
    global _proj_id
    rec = {**data.model_dump(), "id": _proj_id, "created_at": datetime.now(timezone.utc)}
    _projects[_proj_id] = rec
    _proj_id += 1
    return Project(**rec)


def get_project(project_id: int) -> Optional[Project]:
    rec = _projects.get(project_id)
    return Project(**rec) if rec else None


def list_projects(status=None) -> List[Project]:
    result = list(_projects.values())
    if status is not None:
        result = [r for r in result if r["status"] == status]
    return [Project(**r) for r in result]


def update_project(project_id: int, data: ProjectUpdate) -> Optional[Project]:
    rec = _projects.get(project_id)
    if not rec:
        return None
    for field, value in data.model_dump(exclude_none=True).items():
        rec[field] = value
    _projects[project_id] = rec
    return Project(**rec)


# ── Milestone ─────────────────────────────────────────────────────────────────

def create_milestone(data: MilestoneCreate) -> Milestone:
    global _mile_id
    rec = {**data.model_dump(), "id": _mile_id, "created_at": datetime.now(timezone.utc)}
    _milestones[_mile_id] = rec
    _mile_id += 1
    return Milestone(**rec)


def list_milestones(project_id: int) -> List[Milestone]:
    return [Milestone(**r) for r in _milestones.values() if r["project_id"] == project_id]


def complete_milestone(milestone_id: int) -> Optional[Milestone]:
    rec = _milestones.get(milestone_id)
    if not rec:
        return None
    rec["is_completed"] = True
    return Milestone(**rec)


# ── Task ──────────────────────────────────────────────────────────────────────

def create_task(data: TaskCreate) -> Task:
    global _task_id
    rec = {**data.model_dump(), "id": _task_id, "created_at": datetime.now(timezone.utc)}
    _tasks[_task_id] = rec
    _task_id += 1
    return Task(**rec)


def get_task(task_id: int) -> Optional[Task]:
    rec = _tasks.get(task_id)
    return Task(**rec) if rec else None


def list_tasks(project_id: Optional[int] = None, assignee_id: Optional[int] = None) -> List[Task]:
    result = list(_tasks.values())
    if project_id is not None:
        result = [r for r in result if r["project_id"] == project_id]
    if assignee_id is not None:
        result = [r for r in result if r.get("assignee_id") == assignee_id]
    return [Task(**r) for r in result]


def update_task(task_id: int, data: TaskUpdate) -> Optional[Task]:
    rec = _tasks.get(task_id)
    if not rec:
        return None
    for field, value in data.model_dump(exclude_none=True).items():
        rec[field] = value
    _tasks[task_id] = rec
    return Task(**rec)
