package co.ata.quirkyperks;

import java.util.List;

import co.ata.quirkyperks.items.ItemFilter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class WarpInterface {
    public EnumWarpInterface type = EnumWarpInterface.Item;
    public int id = -1;
    public boolean[] outSides = new boolean[]{
        false,
        false,
        false,
        false,
        false,
        false
    };
    public boolean[] inSides = new boolean[]{
        false,
        false,
        false,
        false,
        false,
        false
    };

    public void toNBT(NBTTagCompound nbt){
        nbt.setInteger("interface_type", type.ordinal());
        nbt.setInteger("interface_sides",
            (outSides[0]?1:0) +
            (outSides[1]?2:0) +
            (outSides[2]?4:0) +
            (outSides[3]?8:0) +
            (outSides[4]?16:0) +
            (outSides[5]?32:0) +
            (inSides[0]?64:0) +
            (inSides[1]?128:0) +
            (inSides[2]?256:0) +
            (inSides[3]?512:0) +
            (inSides[4]?1024:0) +
            (inSides[5]?2048:0)
        );
    }

    public WarpInterface(int id){
        this.id = id;
    }

    public static WarpInterface fromNBT(NBTTagCompound nbt, int id){
        WarpInterface i = new WarpInterface(id);
        i.type = EnumWarpInterface.values()[nbt.getInteger("interface_type")];
        int c = nbt.getInteger("interface_sides");
        i.outSides[0] = (c&1) > 0;
        i.outSides[1] = (c&2) > 0;
        i.outSides[2] = (c&4) > 0;
        i.outSides[3] = (c&8) > 0;
        i.outSides[4] = (c&16) > 0;
        i.outSides[5] = (c&32) > 0;
         i.inSides[0] = (c&64) > 0;
         i.inSides[1] = (c&128) > 0;
         i.inSides[2] = (c&256) > 0;
         i.inSides[3] = (c&512) > 0;
         i.inSides[4] = (c&1024) > 0;
         i.inSides[5] = (c&2048) > 0;

        return i;
    }

    public static boolean canInterface(List<WarpInterface> interfaces, NonNullList<ItemStack> filters, EnumFacing f, EnumInterfaceDirection dir, ItemStack target){
        for(WarpInterface i : interfaces){
            if(ItemFilter.CanAccept(filters.get(i.id), target) && (dir.ordinal() & EnumInterfaceDirection.Out.ordinal())>0 && i.outSides[f.ordinal()])
                return true;
            if(ItemFilter.CanAccept(filters.get(i.id), target) && (dir.ordinal() & EnumInterfaceDirection.In.ordinal())>0 && i.inSides[f.ordinal()])
                return true;
        }
        return false;
    }

    public static boolean canInterface(List<WarpInterface> interfaces, NonNullList<ItemStack> filters, EnumFacing f, EnumInterfaceDirection dir, FluidStack target){
        for(WarpInterface i : interfaces){
            if(ItemFilter.CanAccept(filters.get(i.id), target) && (dir.ordinal() & EnumInterfaceDirection.Out.ordinal())>0 && i.outSides[f.ordinal()])
                return true;
            if(ItemFilter.CanAccept(filters.get(i.id), target) && (dir.ordinal() & EnumInterfaceDirection.In.ordinal())>0 && i.inSides[f.ordinal()])
                return true;
        }
        return false;
    }

    public static boolean canInterface(List<WarpInterface> interfaces, EnumFacing f, EnumInterfaceDirection dir){
        for(WarpInterface i : interfaces){
            if((dir.ordinal() & EnumInterfaceDirection.Out.ordinal())>0 && i.outSides[f.ordinal()])
                return true;
            if((dir.ordinal() & EnumInterfaceDirection.In.ordinal())>0 && i.inSides[f.ordinal()])
                return true;
        }
        return false;
    }
}