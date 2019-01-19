package co.ata.quirkyperks.packet.inventory;

import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public abstract class PacketInventorySlot extends PacketInventory{
    int index = 0;
    public PacketInventorySlot(TileWarper source, EnumRequestType type, EnumFacing facing, int index){
        super(source,type,facing);
        this.index = index;
    }

    @Override
    public void touchHandler(TileWarper target, WarpInterface iface, IItemHandler handler, EnumFacing f) {
        if(burned)
            return;
        int size = handler.getSlots();
        if(index >= size){
            index -= size;
            return;
        }
        touchSlot(target, iface, handler, f, index);
        Burn();
    }
    
    public abstract void touchSlot(TileWarper target, WarpInterface iface, IItemHandler handler, EnumFacing f, int slot);
}