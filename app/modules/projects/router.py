"""
Module 4 – Project Management
Routes: /projects
"""

from typing import List, Optional

from fastapi import APIRouter, HTTPException, status

from app.modules.projects.models import (
    Milestone,
    MilestoneCreate,
    Project,
    ProjectCreate,
    ProjectStatus,
    ProjectUpdate,
    Task,
    TaskCreate,
    TaskUpdate,
)
from app.modules.projects.service import (
    complete_milestone,
    create_milestone,
    create_project,
    create_task,
    get_project,
    get_task,
    list_milestones,
    list_projects,
    list_tasks,
    update_project,
    update_task,
)

router = APIRouter(prefix="/projects", tags=["Module 4 – Project Management"])


# ── Projects ──────────────────────────────────────────────────────────────────

@router.post("", response_model=Project, status_code=status.HTTP_201_CREATED)
def create_proj(data: ProjectCreate):
    return create_project(data)


@router.get("", response_model=List[Project])
def list_proj(project_status: Optional[ProjectStatus] = None):
    return list_projects(project_status)


@router.get("/{project_id}", response_model=Project)
def get_proj(project_id: int):
    proj = get_project(project_id)
    if not proj:
        raise HTTPException(status_code=404, detail="Project not found")
    return proj


@router.patch("/{project_id}", response_model=Project)
def update_proj(project_id: int, data: ProjectUpdate):
    proj = update_project(project_id, data)
    if not proj:
        raise HTTPException(status_code=404, detail="Project not found")
    return proj


# ── Milestones ────────────────────────────────────────────────────────────────

@router.post("/{project_id}/milestones", response_model=Milestone, status_code=status.HTTP_201_CREATED)
def create_mile(project_id: int, data: MilestoneCreate):
    data.project_id = project_id
    return create_milestone(data)


@router.get("/{project_id}/milestones", response_model=List[Milestone])
def list_mile(project_id: int):
    return list_milestones(project_id)


@router.post("/milestones/{milestone_id}/complete", response_model=Milestone)
def complete_mile(milestone_id: int):
    mile = complete_milestone(milestone_id)
    if not mile:
        raise HTTPException(status_code=404, detail="Milestone not found")
    return mile


# ── Tasks ─────────────────────────────────────────────────────────────────────

@router.post("/tasks", response_model=Task, status_code=status.HTTP_201_CREATED)
def create_tsk(data: TaskCreate):
    return create_task(data)


@router.get("/tasks", response_model=List[Task])
def list_tsk(project_id: Optional[int] = None, assignee_id: Optional[int] = None):
    return list_tasks(project_id, assignee_id)


@router.get("/tasks/{task_id}", response_model=Task)
def get_tsk(task_id: int):
    task = get_task(task_id)
    if not task:
        raise HTTPException(status_code=404, detail="Task not found")
    return task


@router.patch("/tasks/{task_id}", response_model=Task)
def update_tsk(task_id: int, data: TaskUpdate):
    task = update_task(task_id, data)
    if not task:
        raise HTTPException(status_code=404, detail="Task not found")
    return task
