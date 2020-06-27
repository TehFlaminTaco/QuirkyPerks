package co.ata.quirkyperks.items;

import java.util.ArrayList;
import java.util.List;

import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.QuirkyPerks;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.blocks.BlockWarpController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWarpChip extends Item implements IWarpCardBase {
    public static ItemWarpChip INSTANCE = new ItemWarpChip();

    public ItemWarpChip(){
        super();
        setCreativeTab(QuirkyPerks.tabQuirkyPerks);
        setUnlocalizedName("warpchip");
        setRegistryName("warpchip");
        setMaxStackSize(1);
        setMaxDamage(0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        NBTTagCompound nbt = stack.getTagCompound();
        if(nbt == null)
            return;
        
        World world = null;
        if(nbt.hasKey("dimension") ? nbt.getInteger("dimension") == worldIn.provider.getDimension() : true)
            world = Minecraft.getMinecraft().world;
        else if(nbt.hasKey("controllerID")){
            tooltip.add("Synced to controller \u00A75\u00A7k" + nbt.getInteger("controllerID") + "\u00A7r");
            return;
        }else
            return;
        // If we have a controller, and we still point to the correct controller.
        if(nbt.hasKey("controllerID") && BlockWarpController.isController(world, new BlockPos(nbt.getDouble("targetX"), nbt.getDouble("targetY"), nbt.getDouble("targetZ")), nbt.getInteger("controllerID")))
            tooltip.add(String.format("Synced to controller %s", nbt.getInteger("controllerID")));
    }

    public NonNullList<ItemStack> getFilters(ItemStack card){
        return NonNullList.<ItemStack>withSize(EnumWarpInterface.values().length, ItemStack.EMPTY);
    }
    public List<WarpInterface> getInterfaces(ItemStack stack){
        ArrayList<WarpInterface> wis = new ArrayList<WarpInterface>();
        for(EnumWarpInterface type : EnumWarpInterface.values()){
            WarpInterface iface = new WarpInterface(type.ordinal());
            iface.type = type;
            iface.inSides = new boolean[]{true, true, true, true, true, true};
            iface.outSides = new boolean[]{true, true, true, true, true, true};
            wis.add(iface);
        }
        return wis;
    }
    public List<WarpInterface> getInterfaces(ItemStack stack, EnumWarpInterface type){
        ArrayList<WarpInterface> wis = new ArrayList<WarpInterface>();
        WarpInterface iface = new WarpInterface(type.ordinal());
        iface.type = type;
        iface.inSides = new boolean[]{true, true, true, true, true, true};
        iface.outSides = new boolean[]{true, true, true, true, true, true};
        wis.add(iface);

        return wis;
    }

}