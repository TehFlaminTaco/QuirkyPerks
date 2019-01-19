package co.ata.quirkyperks.items;

import java.util.ArrayList;
import java.util.List;

import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.QuirkyPerks;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.blocks.BlockWarpController;
import co.ata.quirkyperks.gui.ContainerCard;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

public class ItemWarpCard extends Item {
    public static ItemWarpCard INSTANCE = new ItemWarpCard(); 

    public ItemWarpCard(){
        super();
        setCreativeTab(QuirkyPerks.tabQuirkyPerks);
        setUnlocalizedName("warpcard");
        setRegistryName("warpcard");
        setMaxStackSize(1);
        setMaxDamage(0);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        NBTTagCompound nbt = stack.getTagCompound();
        if(nbt == null)
            return;
        
        World world = nbt.hasKey("dimension") ? DimensionManager.getWorld(nbt.getInteger("dimension")) : worldIn;
        // If we have a controller, and we still point to the correct controller.
        if(nbt.hasKey("controllerID") && BlockWarpController.isController(world, new BlockPos(nbt.getDouble("targetX"), nbt.getDouble("targetY"), nbt.getDouble("targetZ")), nbt.getInteger("controllerID")))
            tooltip.add(String.format("Synced to controller %s", nbt.getInteger("controllerID")));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(playerIn.isSneaking()){
            playerIn.openGui(QuirkyPerks.INSTANCE, ContainerCard.GUI_ID, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
            return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
    
    public static List<WarpInterface> getInterfaces(ItemStack stack){
        ArrayList<WarpInterface> interfaces = new ArrayList<WarpInterface>();

        // Empty, invalid, or stacks without tag compounds are ignored.
        if(stack.isEmpty())
            return interfaces;
        if(stack.getItem() != INSTANCE)
            return interfaces;
        if(!stack.hasTagCompound())
            return interfaces;
        
        NBTTagCompound nbt = stack.getTagCompound();
        NBTTagList tags = nbt.getTagList("interfaces", Constants.NBT.TAG_COMPOUND);    

        for(int id = 0; id < tags.tagCount(); id++){
            NBTBase t = tags.get(id);
            if(t instanceof NBTTagCompound)
                interfaces.add(WarpInterface.fromNBT((NBTTagCompound)t, id));
        }
            
        

        return interfaces;
    }

    public static NonNullList<ItemStack> getFilters(ItemStack card){
        NonNullList<ItemStack> filters = NonNullList.<ItemStack>withSize(8, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(card.getOrCreateSubCompound("filters"), filters);
        return filters;
    }

    public static List<WarpInterface> getInterfaces(ItemStack stack, EnumWarpInterface type){
        ArrayList<WarpInterface> interfaces = new ArrayList<WarpInterface>();

        // Empty, invalid, or stacks without tag compounds are ignored.
        if(stack == null)
            return interfaces;
        if(stack.isEmpty())
            return interfaces;
        if(stack.getItem() != INSTANCE)
            return interfaces;
        if(!stack.hasTagCompound())
            return interfaces;
        
        NBTTagCompound nbt = stack.getTagCompound();
        NBTTagList tags = nbt.getTagList("interfaces", Constants.NBT.TAG_COMPOUND);    

        for(int id = 0; id < tags.tagCount(); id++){
            NBTBase t = tags.get(id);
            if(t instanceof NBTTagCompound){
                WarpInterface wi = WarpInterface.fromNBT((NBTTagCompound)t, id);
                if(wi.type == type)
                    interfaces.add(wi);
            }
        }

        return interfaces;
    }
    
}