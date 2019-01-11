package co.ata.quirkyperks.packet.inventory;

import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class PacketSlotLimit extends PacketInventorySlot {

    EnumFacing dir;
    public int size;

    public PacketSlotLimit(TileWarper source, EnumFacing facing, int index){
        super(source, EnumRequestType.SlotLimit, facing, index);
    }

    @Override
    public Packet GetBlank() {
        return new PacketSlotLimit(source, dir, index);
    }

    @Override
    public void touchSlot(TileWarper target, IItemHandler handler, EnumFacing f, int slot) {
        size = handler.getSlotLimit(slot);
	}
}