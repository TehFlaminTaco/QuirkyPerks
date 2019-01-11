package co.ata.quirkyperks.gui;

import co.ata.quirkyperks.items.ItemFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCard extends Container {

    public static final int GUI_ID = 1;
	public final ItemStack card;
	
	// 8, one for each interface.
	NonNullList<ItemStack> filters = NonNullList.<ItemStack>withSize(8, ItemStack.EMPTY);
	NBTTagCompound filterNBT = new NBTTagCompound();

	IItemHandlerModifiable iHandler = new IItemHandlerModifiable(){

        @Override
        public int getSlots() {
            return 17;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return filters.get(filterSlot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if(stack.isEmpty())
				return stack; // Just incase.
			if(stack.getItem() != ItemFilter.INSTANCE)
				return stack; // Cannont insert something that isn't a filter.
			if(!filters.get(filterSlot).isEmpty())
				return stack; // Already contains a filter.
			
			if(!simulate){
				filters.set(filterSlot, stack);
				ItemStackHelper.saveAllItems(filterNBT, filters);
			}
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if(amount == 0)
				return ItemStack.EMPTY; // Cannot extract nothing.
			
			ItemStack held = filters.get(filterSlot);
			if(held.isEmpty())
				return ItemStack.EMPTY; // Ditto.
			

			if(!simulate){
				filters.set(filterSlot, ItemStack.EMPTY);
				ItemStackHelper.saveAllItems(filterNBT, filters);
			}

            return held;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            // Last time I trusted this it hurt me. But I think I'm ready to love again.
            filters.set(filterSlot, stack);
			ItemStackHelper.saveAllItems(filterNBT, filters);
        }

    };


	public int filterSlot = 0;
    public ContainerCard(InventoryPlayer playerInv, ItemStack card){
        this.card = card;

		NBTTagCompound allTags = (card.hasTagCompound() ? card.getTagCompound() : new NBTTagCompound());
		filterNBT = card.getOrCreateSubCompound("filters");
		allTags.setTag("filters", filterNBT);
        card.setTagCompound(allTags);

		ItemStackHelper.loadAllItems(filterNBT, filters);

		addSlotToContainer(new SlotItemHandler(iHandler, 0, 26, 84));

        for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 116 + i * 18));
			}
		}
	
		for (int k = 0; k < 9; k++) {
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 174));
		}
    }


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getHeldItemMainhand() == card;
    }
    
    @Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
	
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
	
            int containerSlots = 1;
            if(index < containerSlots){
				if(!mergeItemStack(itemstack1, containerSlots + 9, inventorySlots.size(), true)){
					return ItemStack.EMPTY;
				}
			}else if(itemstack1.getItem() == ItemFilter.INSTANCE && this.mergeItemStack(itemstack1, 0, 1, true)){

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