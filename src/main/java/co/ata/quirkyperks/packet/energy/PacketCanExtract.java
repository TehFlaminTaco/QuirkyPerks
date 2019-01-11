package co.ata.quirkyperks.packet.energy;

import java.util.List;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.items.ItemWarpCard;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

public class PacketCanExtract extends PacketEnergy {

    public boolean can_extract = false;
    
    public PacketCanExtract(TileWarper source, EnumFacing facing){
        super(source, EnumRequestType.ReceiveEnergy, facing);
    }

    @Override
    public void touchDevice(TileWarper target, IEnergyStorage store, EnumFacing f) {
        if(burned)
            return;
        List<WarpInterface> interfaces = ItemWarpCard.getInterfaces(target.card(), EnumWarpInterface.Energy);
        if(!WarpInterface.canInterface(interfaces, f, EnumInterfaceDirection.In))
            return;
        if(store.canExtract()){
            can_extract = true;
            Burn();
        }
    }

    @Override
    public Packet GetBlank() {
		return new PacketCanReceive(source, facing);
	}
    
}