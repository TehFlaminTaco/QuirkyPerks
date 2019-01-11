package co.ata.quirkyperks.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerFilter extends Container {

    public static final int GUI_ID = 2;
    public final ItemStack filter;

    NBTTagCompound nbt;
    NonNullList<ItemStack> filterItems = NonNullList.<ItemStack>withSize(17, ItemStack.EMPTY);

    IItemHandler iHandler = new IItemHandlerModifiable(){

        @Override
        public int getSlots() {
            return 17;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return filterItems.get(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ItemStack filterStack = stack.copy();
            filterStack.setCount(1);
            filterItems.set(slot, filterStack);
            ItemStackHelper.saveAllItems(nbt, filterItems);
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            //System.out.println(String.format("EXTRACT %s, %s, %s", slot, amount, simulate));
            //filterItems.set(slot, ItemStack.EMPTY);
            //ItemStackHelper.saveAllItems(nbt, filterItems);
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            //System.out.println(String.format("SET IN FILTER: %s, %s", slot, stack));
            //filterItems.set(slot, stack);
            //ItemStackHelper.saveAllItems(nbt, filterItems);
        }

    };

    public void clear(){
        filterItems.clear();
        ItemStackHelper.saveAllItems(nbt, filterItems);
    }

    public ContainerFilter(InventoryPlayer playerInv, ItemStack filter){
        this.filter = filter;
        NBTTagCompound allTags = (filter.hasTagCompound() ? filter.getTagCompound() : new NBTTagCompound());
        nbt = allTags.hasKey("filters") ? allTags.getCompoundTag("filters") : new NBTTagCompound();
        allTags.setTag("filters", nbt);
        filter.setTagCompound(allTags);

        ItemStackHelper.loadAllItems(nbt, filterItems);

        for(int i=0; i < 9; i++){
            addSlotToContainer(new SlotItemHandler(iHandler, i, 8 + i * 18, 19));
        }
        for(int i=0; i < 8; i++){
            addSlotToContainer(new SlotItemHandler(iHandler, 9 + i, 8 + i * 18, 37));
        }

        for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 73 + i * 18));
			}
		}
	
		for (int k = 0; k < 9; k++) {
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 131));
		}
    }


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getHeldItemMainhand() == filter;
    }
    
    @Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
	
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
	
            int containerSlots = 17;
            if(index < containerSlots){
                filterItems.set(index, ItemStack.EMPTY); // Zero out the slot.
                return ItemStack.EMPTY;
            }
	
			if (index < containerSlots + 9) {
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