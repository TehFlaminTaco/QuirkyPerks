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

public class PacketReceiveEnergy extends PacketEnergy {

    int amount = 0;
    public int outp = 0;
    boolean simulate;
    
    public PacketReceiveEnergy(TileWarper source, EnumFacing facing, int amount, boolean simulate){
        super(source, EnumRequestType.ReceiveEnergy, facing);
        this.amount = amount;
        this.simulate = simulate;
    }

    @Override
    public void touchDevice(TileWarper target, IEnergyStorage store, EnumFacing f) {
        if(amount <= 0)
        return;
        
        List<WarpInterface> interfaces = ItemWarpCard.getInterfaces(target.card(), EnumWarpInterface.Energy);
        if(!WarpInterface.canInterface(interfaces, f, EnumInterfaceDirection.Out))
            return;
        int received = store.receiveEnergy(amount, simulate);
        amount -= received;
        outp += received;
    }

    @Override
    public Packet GetBlank() {
		return new PacketReceiveEnergy(source, facing, amount, simulate);
	}
    
}