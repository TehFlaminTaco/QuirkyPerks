package co.ata.quirkyperks.packet.energy;

import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

public class PacketEnergyStored extends PacketEnergy {

    public int outp = 0;
    
    public PacketEnergyStored(TileWarper source, EnumFacing facing){
        super(source, EnumRequestType.ExtractEnergy, facing);
    }

    @Override
    public void touchDevice(TileWarper target, WarpInterface iface, IEnergyStorage store, EnumFacing f) {
        int received = store.getEnergyStored();
        outp += received;
    }

    @Override
    public Packet GetBlank() {
		return new PacketEnergyStored(source, facing);
	}
    
}