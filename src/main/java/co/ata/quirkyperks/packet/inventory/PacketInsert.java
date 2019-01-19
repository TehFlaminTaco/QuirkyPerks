package co.ata.quirkyperks.packet.inventory;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.items.ItemWarpCard;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class PacketInsert extends PacketInventorySlot {

    public boolean simulate;
    ItemStack stack;
    EnumFacing dir;
    public ItemStack output = ItemStack.EMPTY;
    
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
    public void touchSlot(TileWarper target, WarpInterface iface, IItemHandler handler, EnumFacing f, int slot) {
        if(!iface.canInterface(ItemWarpCard.getFilters(target.card()), f, EnumInterfaceDirection.Out, stack))
            output = stack;
        else
            output = handler.insertItem(slot, stack, simulate);
	}
}