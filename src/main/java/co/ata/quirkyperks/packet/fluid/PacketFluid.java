package co.ata.quirkyperks.packet.fluid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.items.ItemWarpCard;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class PacketFluid extends Packet {
    EnumFacing facing;
    public PacketFluid(TileWarper source, EnumRequestType type, EnumFacing facing){
        super(source, type);
        this.facing = facing;
    }

    @Override
    public void touch(TileWarper target) {
        HashSet<IFluidHandler> handlers = new HashSet<IFluidHandler>();
        HashMap<IFluidHandler, EnumFacing> facings = new HashMap<IFluidHandler, EnumFacing>();
        List<WarpInterface> interfaces = ItemWarpCard.getInterfaces(target.card(), EnumWarpInterface.Fluid);
        for(EnumFacing f : EnumFacing.values()){
            if(!WarpInterface.canInterface(interfaces, f, EnumInterfaceDirection.Both)) // Ignore ignorable inventories.
                continue;
            BlockPos tPos = target.getPos().offset(f);
            TileEntity te = target.getWorld().getTileEntity(tPos);
            if(te instanceof ICapabilityProvider){
                ICapabilityProvider cp = (ICapabilityProvider) te;
                if(cp.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)){
                    IFluidHandler h = cp.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
                    handlers.add(h);
                    facings.put(h, f);
                }
            }
        }
        for(IFluidHandler handler : handlers){
            touchHandler(target, handler, facings.get(handler));
        }
    }

    public abstract void touchHandler(TileWarper target, IFluidHandler handler, EnumFacing f);
}