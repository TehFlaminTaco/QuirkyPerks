package co.ata.quirkyperks.packet.inventory;

import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class PacketInvSize extends PacketInventory {

    public int size = 0;

    public PacketInvSize(TileWarper source, EnumFacing facing){
        super(source, EnumRequestType.InvSize, facing);
    }

    @Override
    public Packet GetBlank() {
        return new PacketInvSize(source, facing);
    }

    @Override
    public void touchHandler(TileWarper target, WarpInterface iface, IItemHandler handler, EnumFacing f) {
        size += handler.getSlots();
	}
}