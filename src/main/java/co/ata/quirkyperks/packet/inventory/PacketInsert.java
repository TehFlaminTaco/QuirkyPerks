package co.ata.quirkyperks.packet.inventory;

import java.util.List;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.items.ItemWarpCard;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class PacketInsert extends PacketInventorySlot {

    public boolean simulate;
    ItemStack stack;
    EnumFacing dir;
    public ItemStack output;

    public PacketInsert(TileWarper source, EnumFacing facing, int index, ItemStack stack, boolean simulate){
        super(source, EnumRequestType.Insert, facing, index);
        this.stack = stack;
        this.output = stack;
        this.simulate = simulate;
    }

    @Override
    public Packet GetBlank() {
        return new PacketInsert(source, dir, index, stack, simulate);
    }

    @Override
    public void touchSlot(TileWarper target, IItemHandler handler, EnumFacing f, int slot) {
        List<WarpInterface> interfaces = ItemWarpCard.getInterfaces(target.card(), EnumWarpInterface.Item);
        if(!WarpInterface.canInterface(interfaces, ItemWarpCard.getFilters(target.card()), f, EnumInterfaceDirection.Out, stack))
            output = stack;
        else
            output = handler.insertItem(slot, stack, simulate);
	}
}