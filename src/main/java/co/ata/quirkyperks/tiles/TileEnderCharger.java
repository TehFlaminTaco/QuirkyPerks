package co.ata.quirkyperks.tiles;

import co.ata.quirkyperks.QuirkyEventHandler;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEnderCharger extends TileEntity implements IItemHandler {

    NonNullList<ItemStack> storage = NonNullList.<ItemStack>withSize(8, ItemStack.EMPTY);

    @Override
    public int getSlots() {
        return 8;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return storage.get(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if(!storage.get(slot).isEmpty()) return stack;
        if(stack.getItem() != Items.REDSTONE) return stack; // Only redstone allowed.
        
        ItemStack stackVal = stack.copy();
        ItemStack removed = stackVal.splitStack(1);
        if(!simulate)
            storage.set(slot, removed);
        return stackVal;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(storage.get(slot).isEmpty()) return ItemStack.EMPTY;
        if(amount <= 0) return ItemStack.EMPTY;
        ItemStack removed = storage.get(slot).copy();
        if(!simulate)
            storage.set(slot, ItemStack.EMPTY);
        return removed;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) this;
        return super.getCapability(capability, facing);
    }

    private static ResourceLocation chargeSound = new ResourceLocation("minecraft", "entity.endermen.teleport");
    public void Charge(){
        int upgradeSlot = -1;
        for(int i=0; i < 8; i++){
            if(storage.get(i).getItem() == Items.REDSTONE){
                upgradeSlot = i;
                break;
            }
        }
        // Do nothing if there's nothing to charge.
        if(upgradeSlot == -1)
            return;
        storage.set(upgradeSlot, new ItemStack(QuirkyEventHandler.itemFauxenderdust, 1));

        BlockPos p = getPos();
        world.playSound(null, p, SoundEvent.REGISTRY.getObject(chargeSound), SoundCategory.BLOCKS, 1.0f, 1.5f);
    }

    public void setStackInSlot(int slot, ItemStack stack){
        storage.set(slot, stack);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        ItemStackHelper.loadAllItems(compound, storage);
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, storage);
        return compound;
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        ItemStackHelper.saveAllItems(nbt, storage);
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound compound) {
        super.handleUpdateTag(compound);
        ItemStackHelper.loadAllItems(compound, storage);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        NBTTagCompound nbtTag = new NBTTagCompound();
        ItemStackHelper.saveAllItems(nbtTag, storage);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
        NBTTagCompound tag = pkt.getNbtCompound();
        ItemStackHelper.loadAllItems(tag, storage);
    }

}