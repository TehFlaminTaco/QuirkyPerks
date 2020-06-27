package co.ata.quirkyperks.items;

import java.util.List;

import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.WarpInterface;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IWarpCardBase {
    public static NonNullList<ItemStack> getFiltersFromItem(ItemStack card){
        Item i = card.getItem();
        if(i instanceof IWarpCardBase){
            IWarpCardBase cardItem = (IWarpCardBase) i;
            return cardItem.getFilters(card);
        }
        return null;
    }
    public static List<WarpInterface> getInterfacesFromItem(ItemStack card){
        Item i = card.getItem();
        if(i instanceof IWarpCardBase){
            IWarpCardBase cardItem = (IWarpCardBase) i;
            return cardItem.getInterfaces(card);
        }
        return null;
    }
    public static List<WarpInterface> getInterfacesFromItem(ItemStack card, EnumWarpInterface type){
        Item i = card.getItem();
        if(i instanceof IWarpCardBase){
            IWarpCardBase cardItem = (IWarpCardBase) i;
            return cardItem.getInterfaces(card, type);
        }
        return null;
    }

    public NonNullList<ItemStack> getFilters(ItemStack card);
    public List<WarpInterface> getInterfaces(ItemStack stack);
    public List<WarpInterface> getInterfaces(ItemStack stack, EnumWarpInterface type);
}