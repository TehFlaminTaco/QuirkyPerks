package co.ata.quirkyperks.gui;

import co.ata.quirkyperks.tiles.TileEnderCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerEnderCharger extends Container {

	public static final int GUI_ID = 3;
	
	public TileEnderCharger charger;

	IItemHandlerModifiable iHandler = new IItemHandlerModifiable(){

        @Override
        public int getSlots() {
            return 8;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return charger.getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			return charger.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return charger.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            charger.setStackInSlot(slot, stack);
        }

    };


	public int filterSlot = 0;
    public ContainerEnderCharger(InventoryPlayer playerInv, TileEnderCharger charger){
        this.charger = charger;

		addSlotToContainer(new SlotItemHandler(iHandler, 0, 62, 19));
		addSlotToContainer(new SlotItemHandler(iHandler, 1, 80, 19));
		addSlotToContainer(new SlotItemHandler(iHandler, 2, 98, 19));
		addSlotToContainer(new SlotItemHandler(iHandler, 3, 98, 37));
		addSlotToContainer(new SlotItemHandler(iHandler, 4, 98, 55));
		addSlotToContainer(new SlotItemHandler(iHandler, 5, 80, 55));
		addSlotToContainer(new SlotItemHandler(iHandler, 6, 62, 55));
		addSlotToContainer(new SlotItemHandler(iHandler, 7, 62, 37));

        for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 91 + i * 18));
			}
		}
	
		for (int k = 0; k < 9; k++) {
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 149));
		}
    }


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
    
    @Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
	
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
	
            int containerSlots = 8;
            if(index < containerSlots){
				if(!mergeItemStack(itemstack1, containerSlots + 9, inventorySlots.size(), true)){
					return ItemStack.EMPTY;
				}
			}else if(this.mergeItemStack(itemstack1, 0, containerSlots, false)){
				while(!itemstack1.isEmpty()&&this.mergeItemStack(itemstack1, 0, containerSlots, false));
				return ItemStack.EMPTY;
			}else if (index < containerSlots + 9) {
				if (!this.mergeItemStack(itemstack1, containerSlots + 9, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, containerSlots, containerSlots + 9, false)) {
				return ItemStack.EMPTY;
			}
	
			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
	
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
	
			slot.onTake(player, itemstack1);
		}
	
		return itemstack;
	}
    
}