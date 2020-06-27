package co.ata.quirkyperks.packet.inventory;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.items.IWarpCardBase;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class PacketExtract extends PacketInventorySlot {

    public boolean simulate;
    int amount;
    EnumFacing dir;
    public ItemStack output = ItemStack.EMPTY;

    public PacketExtract(TileWarper source, EnumFacing facing, int index, int amount, boolean simulate){
        super(source, EnumRequestType.Extract, facing, index);
        this.amount = amount;
        this.simulate = simulate;
    }

    @Override
    public Packet GetBlank() {
        return new PacketExtract(source, dir, index, amount, simulate);
    }

    @Override
    public void touchSlot(TileWarper target, WarpInterface iface, IItemHandler handler, EnumFacing f, int slot) {
        ItemStack pulledItem = handler.extractItem(slot, 1, true);
        if(!iface.canInterface(IWarpCardBase.getFiltersFromItem(target.card()), f, EnumInterfaceDirection.In, pulledItem))
            output = ItemStack.EMPTY;
        else
            output = handler.extractItem(slot, amount, simulate);
	}
}