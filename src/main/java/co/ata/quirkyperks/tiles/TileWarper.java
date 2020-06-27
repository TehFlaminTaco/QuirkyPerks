package co.ata.quirkyperks.tiles;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.QuirkyPerks;
import co.ata.quirkyperks.QuirkyProxy;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.blocks.BlockWarpController;
import co.ata.quirkyperks.items.IWarpCardBase;
import co.ata.quirkyperks.items.ItemWarpCard;
import co.ata.quirkyperks.packet.energy.PacketCanExtract;
import co.ata.quirkyperks.packet.energy.PacketCanReceive;
import co.ata.quirkyperks.packet.energy.PacketEnergyStored;
import co.ata.quirkyperks.packet.energy.PacketExtractEnergy;
import co.ata.quirkyperks.packet.energy.PacketMaxEnergyStored;
import co.ata.quirkyperks.packet.energy.PacketReceiveEnergy;
import co.ata.quirkyperks.packet.fluid.PacketDrainAmount;
import co.ata.quirkyperks.packet.fluid.PacketDrainStack;
import co.ata.quirkyperks.packet.fluid.PacketFill;
import co.ata.quirkyperks.packet.fluid.PacketTankProperties;
import co.ata.quirkyperks.packet.inventory.PacketExtract;
import co.ata.quirkyperks.packet.inventory.PacketGetStackSlot;
import co.ata.quirkyperks.packet.inventory.PacketInsert;
import co.ata.quirkyperks.packet.inventory.PacketInvSize;
import co.ata.quirkyperks.packet.inventory.PacketSlotLimit;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileWarper extends TileEntity implements ITickable{

    public NonNullList<ItemStack> storedCard = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        ItemStackHelper.loadAllItems(compound, storedCard);
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, storedCard);
        return compound;
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        ItemStackHelper.saveAllItems(nbt, storedCard);
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound compound) {
        super.handleUpdateTag(compound);
        ItemStackHelper.loadAllItems(compound, storedCard);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        NBTTagCompound nbtTag = new NBTTagCompound();
        ItemStackHelper.saveAllItems(nbtTag, storedCard);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
        NBTTagCompound tag = pkt.getNbtCompound();
        ItemStackHelper.loadAllItems(tag, storedCard);
        //Handle your Data
    }

    @Deprecated
    public boolean hasInterface(EnumWarpInterface i, EnumFacing f, EnumInterfaceDirection d){
        return false;
    }

    
    public TileWarpController getController(){
        if(controllerID() < 0)
            return null;
        NBTTagCompound nbt = card().getTagCompound();
        World worl = QuirkyProxy.getWorldFromID(nbt.hasKey("dimension") ? nbt.getInteger("dimension") : null, world);
        if(worl == null)
            return null;
        if(!BlockWarpController.isController(worl, targetPos(), controllerID()))
            return null;
        return (TileWarpController)worl.getTileEntity(targetPos());
    }

    private BlockPos targetPos() {
        ItemStack iStack = storedCard.get(0);
        if(iStack.isEmpty())
            return null;
        if(iStack.getItem() != ItemWarpCard.INSTANCE)
            return null;
        if(!iStack.hasTagCompound())
            return null;
        return new BlockPos(
            iStack.getTagCompound().getDouble("targetX"),
            iStack.getTagCompound().getDouble("targetY"),
            iStack.getTagCompound().getDouble("targetZ")
        );
    }

    private int controllerID() {
        ItemStack iStack = storedCard.get(0);
        if(iStack.isEmpty())
            return -1;
        if(iStack.getItem() != ItemWarpCard.INSTANCE)
            return -1;
        if(!iStack.hasTagCompound())
            return -1;
        return iStack.getTagCompound().getInteger("controllerID");
    }

    public ItemStack card(){
        ItemStack iStack = storedCard.get(0);
        if(iStack.isEmpty())
            return ItemStack.EMPTY;
        if(iStack.getItem() != ItemWarpCard.INSTANCE)
            return ItemStack.EMPTY;
        if(!iStack.hasTagCompound())
            return ItemStack.EMPTY;
        return iStack;
    }

	public void DropCard() {
        if(!storedCard.get(0).isEmpty())
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 0.5f, pos.getZ(), storedCard.get(0));
        storedCard.set(0, ItemStack.EMPTY);
        markDirty();
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
	}

    int wakeCheck = 0;
    @Override
    public void update() {
        TileWarpController tc = getController();
        wakeCheck++;
        if(wakeCheck > 10 && tc != null){
            wakeCheck = 0;
            tc.wake(this);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return WarpInterface.canInterface(IWarpCardBase.getInterfacesFromItem(card(), EnumWarpInterface.Item), facing, EnumInterfaceDirection.Both);
        if(capability == CapabilityEnergy.ENERGY)
            return WarpInterface.canInterface(IWarpCardBase.getInterfacesFromItem(card(), EnumWarpInterface.Energy), facing, EnumInterfaceDirection.Both);
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return WarpInterface.canInterface(IWarpCardBase.getInterfacesFromItem(card(), EnumWarpInterface.Fluid), facing, EnumInterfaceDirection.Both);
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) new SidedWarper(this, facing);
        return super.getCapability(capability, facing);
    }

}

class SidedWarper implements IItemHandler, IEnergyStorage, IFluidHandler {
    TileWarper warper;
    EnumFacing facing;

    public SidedWarper(TileWarper warper, EnumFacing facing){
        this.warper = warper;
        this.facing = facing;
    }

////////////////////////////////
// ITEM BOX
////////////////////////////////

    @Override
    public int getSlots() {
        TileWarpController c = warper.getController();
        if(c == null)
            return 0;
        if(!WarpInterface.canInterface(IWarpCardBase.getInterfacesFromItem(warper.card(), EnumWarpInterface.Item), facing, EnumInterfaceDirection.Both))
            return 0;
        PacketInvSize size = c.touch(new PacketInvSize(warper, facing));
        return size.size;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        TileWarpController c = warper.getController();
        if(c == null)
            return stack;
        if(!WarpInterface.canInterface(IWarpCardBase.getInterfacesFromItem(warper.card(), EnumWarpInterface.Item), IWarpCardBase.getFiltersFromItem(warper.card()), facing, EnumInterfaceDirection.In, stack))
            return stack;
        PacketInsert p = c.touch(new PacketInsert(warper, facing, slot, stack, simulate));
        return p.output;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        TileWarpController c = warper.getController();
        if(c == null)
            return ItemStack.EMPTY;
        PacketExtract pullTest = c.touch(new PacketExtract(warper, facing, slot, amount, true));
        if(!WarpInterface.canInterface(IWarpCardBase.getInterfacesFromItem(warper.card(), EnumWarpInterface.Item), IWarpCardBase.getFiltersFromItem(warper.card()), facing, EnumInterfaceDirection.Out, pullTest.output))
            return ItemStack.EMPTY;
        PacketExtract p = simulate ? pullTest : c.touch(new PacketExtract(warper, facing, slot, amount, false));
        return p.output;
    }

    @Override
    public int getSlotLimit(int slot) {
        TileWarpController c = warper.getController();
        if(c == null)
            return 0;
        PacketSlotLimit p = c.touch(new PacketSlotLimit(warper, facing, slot));
        return p.size;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        TileWarpController c = warper.getController();
        if(c == null)
            return ItemStack.EMPTY;
        PacketGetStackSlot p = c.touch(new PacketGetStackSlot(warper, facing, slot));
        return p.output;
    }

////////////////////////////////
// ENERGY BOX
////////////////////////////////

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        TileWarpController c = warper.getController();
        if(c == null)
            return 0;
        PacketReceiveEnergy p = c.touch(new PacketReceiveEnergy(warper, facing, maxReceive, simulate));
        return p.outp;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        TileWarpController c = warper.getController();
        if(c == null)
            return 0;
        PacketExtractEnergy p = c.touch(new PacketExtractEnergy(warper, facing, maxExtract, simulate));
        return p.outp;
    }

    @Override
    public int getEnergyStored() {
        TileWarpController c = warper.getController();
        if(c == null)
            return 0;
        PacketEnergyStored p = c.touch(new PacketEnergyStored(warper, facing));
        return p.outp;
    }

    @Override
    public int getMaxEnergyStored() {
        TileWarpController c = warper.getController();
        if(c == null)
            return 0;
        PacketMaxEnergyStored p = c.touch(new PacketMaxEnergyStored(warper, facing));
        return p.outp;
    }

    @Override
    public boolean canExtract() {
        TileWarpController c = warper.getController();
        if(c == null)
            return false;
        PacketCanExtract p = c.touch(new PacketCanExtract(warper, facing));
        return p.can_extract;
    }

    @Override
    public boolean canReceive() {
        TileWarpController c = warper.getController();
        if(c == null)
            return false;
        PacketCanReceive p = c.touch(new PacketCanReceive(warper, facing));
        return p.can_receive;
    }

////////////////////////////////
// FLUID BOX
////////////////////////////////

    @Override
    public IFluidTankProperties[] getTankProperties() {
        TileWarpController c = warper.getController();
        if(c == null)
            return new IFluidTankProperties[0];
        PacketTankProperties p = c.touch(new PacketTankProperties(warper, facing));
        return p.props.toArray(new IFluidTankProperties[0]);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        TileWarpController c = warper.getController();
        if(c == null)
            return 0;
        if(!WarpInterface.canInterface(IWarpCardBase.getInterfacesFromItem(warper.card(), EnumWarpInterface.Fluid), IWarpCardBase.getFiltersFromItem(warper.card()), facing, EnumInterfaceDirection.In, resource))
            return 0;
        PacketFill p = c.touch(new PacketFill(warper, facing, resource, doFill));
        return p.filled;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        TileWarpController c = warper.getController();
        if(c == null)
            return null;
        if(!WarpInterface.canInterface(IWarpCardBase.getInterfacesFromItem(warper.card(), EnumWarpInterface.Fluid), IWarpCardBase.getFiltersFromItem(warper.card()), facing, EnumInterfaceDirection.Out, resource))
            return null;
        PacketDrainStack p = c.touch(new PacketDrainStack(warper, facing, resource, doDrain));
        return p.drained;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
		TileWarpController c = warper.getController();
        if(c == null)
            return null;
        
        // Item Filters are handled by the packet. **THIS IS UNSAFE**
        if(!WarpInterface.canInterface(IWarpCardBase.getInterfacesFromItem(warper.card(), EnumWarpInterface.Fluid), facing, EnumInterfaceDirection.Out))
            return null;
        PacketDrainAmount p = c.touch(new PacketDrainAmount(warper, facing, maxDrain, doDrain));
        return p.drained;
	}
}