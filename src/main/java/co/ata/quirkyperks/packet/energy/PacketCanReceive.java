package co.ata.quirkyperks.packet.energy;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

public class PacketCanReceive extends PacketEnergy {

    public boolean can_receive = false;
    
    public PacketCanReceive(TileWarper source, EnumFacing facing){
        super(source, EnumRequestType.ReceiveEnergy, facing);
    }

    @Override
    public void touchDevice(TileWarper target, WarpInterface iface, IEnergyStorage store, EnumFacing f) {
        if(burned)
            return;
        if(!iface.canInterface(f, EnumInterfaceDirection.Out))
            return;
        if(store.canReceive()){
            can_receive = true;
            Burn();
        }
    }

    @Override
    public Packet GetBlank() {
		return new PacketCanReceive(source, facing);
	}
    
}