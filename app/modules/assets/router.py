"""
Module 5 – Asset & Resource Management
Routes: /assets
"""

from typing import List, Optional

from fastapi import APIRouter, HTTPException, status

from app.modules.assets.models import (
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
from app.modules.assets.service import (
    allocate_resource,
    create_asset,
    create_inventory_item,
    get_asset,
    list_allocations,
    list_assets,
    list_inventory_items,
    return_resource,
    update_asset,
    update_inventory_item,
)

router = APIRouter(prefix="/assets", tags=["Module 5 – Asset & Resource Management"])


# ── Assets ────────────────────────────────────────────────────────────────────

@router.post("", response_model=Asset, status_code=status.HTTP_201_CREATED)
def create_asset_route(data: AssetCreate):
    return create_asset(data)


@router.get("", response_model=List[Asset])
def list_assets_route(asset_status: Optional[AssetStatus] = None):
    return list_assets(asset_status)


@router.get("/{asset_id}", response_model=Asset)
def get_asset_route(asset_id: int):
    asset = get_asset(asset_id)
    if not asset:
        raise HTTPException(status_code=404, detail="Asset not found")
    return asset


@router.patch("/{asset_id}", response_model=Asset)
def update_asset_route(asset_id: int, data: AssetUpdate):
    asset = update_asset(asset_id, data)
    if not asset:
        raise HTTPException(status_code=404, detail="Asset not found")
    return asset


# ── Inventory ─────────────────────────────────────────────────────────────────

@router.post("/inventory", response_model=InventoryItem, status_code=status.HTTP_201_CREATED)
def create_inv_route(data: InventoryItemCreate):
    return create_inventory_item(data)


@router.get("/inventory", response_model=List[InventoryItem])
def list_inv_route(needs_reorder: Optional[bool] = None):
    return list_inventory_items(needs_reorder)


@router.patch("/inventory/{item_id}", response_model=InventoryItem)
def update_inv_route(item_id: int, data: InventoryItemUpdate):
    item = update_inventory_item(item_id, data)
    if not item:
        raise HTTPException(status_code=404, detail="Inventory item not found")
    return item


# ── Allocations ───────────────────────────────────────────────────────────────

@router.post("/allocations", response_model=ResourceAllocation, status_code=status.HTTP_201_CREATED)
def allocate_route(data: ResourceAllocationCreate):
    return allocate_resource(data)


@router.get("/allocations", response_model=List[ResourceAllocation])
def list_alloc_route(employee_id: Optional[int] = None):
    return list_allocations(employee_id)


@router.post("/allocations/{allocation_id}/return", response_model=ResourceAllocation)
def return_route(allocation_id: int):
    alloc = return_resource(allocation_id)
    if not alloc:
        raise HTTPException(status_code=404, detail="Allocation not found")
    return alloc
