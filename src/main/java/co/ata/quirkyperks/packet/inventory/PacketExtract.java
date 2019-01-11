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

public class PacketExtract extends PacketInventorySlot {

    public boolean simulate;
    int amount;
    EnumFacing dir;
    public ItemStack output;

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
    public void touchSlot(TileWarper target, IItemHandler handler, EnumFacing f, int slot) {
        List<WarpInterface> interfaces = ItemWarpCard.getInterfaces(target.card(), EnumWarpInterface.Item);
        ItemStack pulledItem = handler.extractItem(slot, 1, true);
        if(!WarpInterface.canInterface(interfaces, ItemWarpCard.getFilters(target.card()), f, EnumInterfaceDirection.In, pulledItem))
            output = ItemStack.EMPTY;
        else
            output = handler.extractItem(slot, amount, simulate);
	}
}