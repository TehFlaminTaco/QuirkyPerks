package co.ata.quirkyperks.packet.energy;

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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class PacketEnergy extends Packet {
    EnumFacing facing;
    public PacketEnergy(TileWarper source, EnumRequestType type, EnumFacing facing){
        super(source, type);
        this.facing = facing;
    }

    @Override
    public void touch(TileWarper target) {
        List<WarpInterface> interfaces = ItemWarpCard.getInterfaces(target.card(), EnumWarpInterface.Energy);
        HashSet<IEnergyStorage> handlers = new HashSet<IEnergyStorage>();
        HashMap<IEnergyStorage, EnumFacing> facings = new HashMap<IEnergyStorage, EnumFacing>();
        for(EnumFacing f : EnumFacing.values()){
            if(!WarpInterface.canInterface(interfaces, f, EnumInterfaceDirection.Both)) // Ignore ignorable inventories.
                continue;
            BlockPos tPos = target.getPos().offset(f);
            TileEntity te = target.getWorld().getTileEntity(tPos);
            if(te instanceof ICapabilityProvider){
                ICapabilityProvider cp = (ICapabilityProvider) te;
                if(cp.hasCapability(CapabilityEnergy.ENERGY, facing)){
                    IEnergyStorage handler = cp.getCapability(CapabilityEnergy.ENERGY, facing);
                    handlers.add(handler);
                    facings.put(handler, f);
                }
            }
        }
        for(IEnergyStorage handler : handlers){
            touchDevice(target, handler, facings.get(handler));
        }
    }

    public abstract void touchDevice(TileWarper target, IEnergyStorage store, EnumFacing f);
}