"""
Module 5 – Asset & Resource Management
Models: Asset, InventoryItem, ResourceAllocation
"""

from __future__ import annotations

from datetime import date, datetime
from enum import Enum
from typing import Optional

from pydantic import BaseModel, Field


class AssetStatus(str, Enum):
    available = "available"
    in_use = "in_use"
    under_maintenance = "under_maintenance"
    retired = "retired"


class AssetCategory(str, Enum):
    hardware = "hardware"
    software = "software"
    furniture = "furniture"
    vehicle = "vehicle"
    equipment = "equipment"
    other = "other"


class AllocationStatus(str, Enum):
    active = "active"
    returned = "returned"


# ── Asset ─────────────────────────────────────────────────────────────────────

class AssetBase(BaseModel):
    name: str
    serial_number: Optional[str] = None
    category: AssetCategory
    purchase_date: date
    purchase_cost: float = Field(..., ge=0)
    current_value: float = Field(..., ge=0)
    status: AssetStatus = AssetStatus.available
    location: Optional[str] = None


class AssetCreate(AssetBase):
    pass


class AssetUpdate(BaseModel):
    name: Optional[str] = None
    current_value: Optional[float] = Field(None, ge=0)
    status: Optional[AssetStatus] = None
    location: Optional[str] = None


class Asset(AssetBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True


# ── Inventory Item ────────────────────────────────────────────────────────────

class InventoryItemBase(BaseModel):
    name: str
    sku: str
    quantity: int = Field(..., ge=0)
    unit_cost: float = Field(..., ge=0)
    reorder_level: int = Field(0, ge=0)
    warehouse_location: Optional[str] = None


class InventoryItemCreate(InventoryItemBase):
    pass


class InventoryItemUpdate(BaseModel):
    quantity: Optional[int] = Field(None, ge=0)
    unit_cost: Optional[float] = Field(None, ge=0)
    reorder_level: Optional[int] = Field(None, ge=0)
    warehouse_location: Optional[str] = None


class InventoryItem(InventoryItemBase):
    id: int
    needs_reorder: bool = False
    created_at: datetime

    class Config:
        from_attributes = True


# ── Resource Allocation ───────────────────────────────────────────────────────

class ResourceAllocationBase(BaseModel):
    asset_id: int
    employee_id: int
    project_id: Optional[int] = None
    allocated_from: date
    allocated_until: Optional[date] = None
    notes: Optional[str] = None


class ResourceAllocationCreate(ResourceAllocationBase):
    pass


class ResourceAllocation(ResourceAllocationBase):
    id: int
    status: AllocationStatus = AllocationStatus.active
    created_at: datetime

    class Config:
        from_attributes = True
