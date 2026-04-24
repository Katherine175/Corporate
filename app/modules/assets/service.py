"""
Module 5 – Asset & Resource Management
Service: assets, inventory, resource allocation
"""

from __future__ import annotations

from datetime import datetime, timezone
from typing import Dict, List, Optional

from app.modules.assets.models import (
    AllocationStatus,
    Asset,
    AssetCreate,
    AssetStatus,
    AssetUpdate,
    InventoryItem,
    InventoryItemCreate,
    InventoryItemUpdate,
    ResourceAllocation,
    ResourceAllocationCreate,
)

_assets: Dict[int, dict] = {}
_inventory: Dict[int, dict] = {}
_allocations: Dict[int, dict] = {}
_asset_id = 1
_inv_id = 1
_alloc_id = 1


# ── Asset ─────────────────────────────────────────────────────────────────────

def create_asset(data: AssetCreate) -> Asset:
    global _asset_id
    rec = {**data.model_dump(), "id": _asset_id, "created_at": datetime.now(timezone.utc)}
    _assets[_asset_id] = rec
    _asset_id += 1
    return Asset(**rec)


def get_asset(asset_id: int) -> Optional[Asset]:
    rec = _assets.get(asset_id)
    return Asset(**rec) if rec else None


def list_assets(status: Optional[AssetStatus] = None) -> List[Asset]:
    result = list(_assets.values())
    if status is not None:
        result = [r for r in result if r["status"] == status]
    return [Asset(**r) for r in result]


def update_asset(asset_id: int, data: AssetUpdate) -> Optional[Asset]:
    rec = _assets.get(asset_id)
    if not rec:
        return None
    for field, value in data.model_dump(exclude_none=True).items():
        rec[field] = value
    _assets[asset_id] = rec
    return Asset(**rec)


# ── Inventory ─────────────────────────────────────────────────────────────────

def create_inventory_item(data: InventoryItemCreate) -> InventoryItem:
    global _inv_id
    rec = {**data.model_dump(), "id": _inv_id,
           "needs_reorder": data.quantity <= data.reorder_level,
           "created_at": datetime.now(timezone.utc)}
    _inventory[_inv_id] = rec
    _inv_id += 1
    return InventoryItem(**rec)


def list_inventory_items(needs_reorder: Optional[bool] = None) -> List[InventoryItem]:
    result = list(_inventory.values())
    if needs_reorder is not None:
        result = [r for r in result if r["needs_reorder"] == needs_reorder]
    return [InventoryItem(**r) for r in result]


def update_inventory_item(item_id: int, data: InventoryItemUpdate) -> Optional[InventoryItem]:
    rec = _inventory.get(item_id)
    if not rec:
        return None
    for field, value in data.model_dump(exclude_none=True).items():
        rec[field] = value
    rec["needs_reorder"] = rec["quantity"] <= rec["reorder_level"]
    _inventory[item_id] = rec
    return InventoryItem(**rec)


# ── Resource Allocation ───────────────────────────────────────────────────────

def allocate_resource(data: ResourceAllocationCreate) -> ResourceAllocation:
    global _alloc_id
    # Mark asset as in_use
    asset = _assets.get(data.asset_id)
    if asset:
        asset["status"] = AssetStatus.in_use
    rec = {**data.model_dump(), "id": _alloc_id,
           "status": AllocationStatus.active,
           "created_at": datetime.now(timezone.utc)}
    _allocations[_alloc_id] = rec
    _alloc_id += 1
    return ResourceAllocation(**rec)


def list_allocations(employee_id: Optional[int] = None) -> List[ResourceAllocation]:
    result = list(_allocations.values())
    if employee_id is not None:
        result = [r for r in result if r["employee_id"] == employee_id]
    return [ResourceAllocation(**r) for r in result]


def return_resource(allocation_id: int) -> Optional[ResourceAllocation]:
    rec = _allocations.get(allocation_id)
    if not rec:
        return None
    rec["status"] = AllocationStatus.returned
    # Mark asset as available
    asset = _assets.get(rec["asset_id"])
    if asset:
        asset["status"] = AssetStatus.available
    return ResourceAllocation(**rec)
