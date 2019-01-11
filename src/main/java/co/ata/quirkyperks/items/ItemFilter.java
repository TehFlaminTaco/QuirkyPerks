package co.ata.quirkyperks.items;

import co.ata.quirkyperks.QuirkyPerks;
import co.ata.quirkyperks.gui.ContainerFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemFilter extends Item {
    public static ItemFilter INSTANCE = new ItemFilter(); 

    public ItemFilter(){
        super();
        setCreativeTab(QuirkyPerks.tabQuirkyPerks);
        setUnlocalizedName("filter");
        setRegistryName("filter");
        setMaxStackSize(1);
        setMaxDamage(0);
    }

    // Pre init this, to avoid constantly reinit-ing it.
    private static NonNullList<ItemStack> filterItems = NonNullList.<ItemStack>withSize(17, ItemStack.EMPTY);

    public static boolean CanAccept(ItemStack filter, ItemStack target){
    //    boolean did = ca(filter, target);
    //    System.out.println(String.format("CAN FILTER: %s, %s, %s", filter, target, did));
    //    return did;
    //}

    //private static boolean ca(ItemStack filter, ItemStack target){
        // Ensure the Filter is 100% valid. Might be a bit wasteful.
        if(filter.isEmpty())
            return true;
        if(filter.getItem() != ItemFilter.INSTANCE)
            return true;
        if(!filter.hasTagCompound())
            return true;
        if(!filter.getTagCompound().hasKey("filters"))
            return true;
        if(!filter.getTagCompound().hasKey("filter_options"))
            return true;
        

        NBTTagCompound filter_options = filter.getSubCompound("filter_options");
        boolean whitelist = filter_options.getBoolean("whitelist");
        boolean meta = filter_options.getBoolean("meta");
        boolean nbt = filter_options.getBoolean("nbt");
        boolean ore = filter_options.getBoolean("ore");

        boolean foundItem = false;
        filterItems.clear();
        ItemStackHelper.loadAllItems(filter.getSubCompound("filters"), filterItems);
        if(target.isEmpty()){
            for(ItemStack i : filterItems){
                if(i.isEmpty())
                    return whitelist;
            }
            return !whitelist;
        }

        for(ItemStack i : filterItems){
            if(i.isEmpty()) // Ignore empty filters.
                continue;
            boolean types_match = i.getItem() == target.getItem();
            if(!types_match && ore){
                int[] t_ids = OreDictionary.getOreIDs(target);
                int[] i_ids = OreDictionary.getOreIDs(i);
                for(int k=0; !types_match && k < t_ids.length; k++)
                    for(int c=0; !types_match && c < i_ids.length; c++)
                        if(t_ids[k] == i_ids[c])
                            types_match = true;
            }
            if(!types_match)
                continue;
            if(!meta && i.getMetadata() != target.getMetadata())
                continue;
            NBTTagCompound tNBT = target.getTagCompound();
            NBTTagCompound iNBT = i.getTagCompound();
            if(!nbt && !(
                (tNBT == null && iNBT == null) ||
                (tNBT != null && iNBT != null && tNBT.equals(iNBT))
            ))
                continue;
            foundItem = true;
            break;
        }

        // Truthy if we found the item and using a whitelist, Falsy if we found the item and using a blacklist, And visaversa.
        return foundItem ^! whitelist;
    }

    public static boolean CanAccept(ItemStack filter, FluidStack target){
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(playerIn.isSneaking()){
            playerIn.openGui(QuirkyPerks.INSTANCE, ContainerFilter.GUI_ID, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
            return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}