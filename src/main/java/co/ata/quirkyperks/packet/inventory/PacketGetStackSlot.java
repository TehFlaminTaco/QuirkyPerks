package co.ata.quirkyperks.packet.inventory;

import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class PacketGetStackSlot extends PacketInventorySlot {

    EnumFacing dir;
    public ItemStack output = ItemStack.EMPTY;

    public PacketGetStackSlot(TileWarper source, EnumFacing facing, int index){
        super(source, EnumRequestType.GetStackSlot, facing, index);
    }

    @Override
    public Packet GetBlank() {
        return new PacketSlotLimit(source, dir, index);
    }

    @Override
    public void touchSlot(TileWarper target, WarpInterface iface, IItemHandler handler, EnumFacing f, int slot) {
        output = handler.getStackInSlot(slot);
	}
}