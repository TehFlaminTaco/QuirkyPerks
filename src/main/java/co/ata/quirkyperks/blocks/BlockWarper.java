package co.ata.quirkyperks.blocks;

import java.util.List;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.QuirkyPerks;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.items.ItemWarpCard;
import co.ata.quirkyperks.packet.PacketActivate;
import co.ata.quirkyperks.tiles.TileWarpController;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class BlockWarper extends BlockContainer {

    public static BlockWarper INSTANCE = new BlockWarper();
    public static Item ITEM = new ItemBlock(INSTANCE).setRegistryName(INSTANCE.getRegistryName());

    public BlockWarper(){
        super(Material.IRON);
		setHardness(2.0f);
		setUnlocalizedName("warper");
		setRegistryName("warper");
		setCreativeTab(QuirkyPerks.tabQuirkyPerks);
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileWarper();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileWarper();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileWarper){
            TileWarper tc = (TileWarper)te;
            tc.DropCard();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){

        TileEntity te = worldIn.getTileEntity(pos);
        if(!(te instanceof TileWarper)){
            return false;
        }
        TileWarper tc = (TileWarper)te;

        if(playerIn.isSneaking()){
            if(!worldIn.isRemote)
                tc.DropCard();
            return true;
        }

		ItemStack item = playerIn.getHeldItemMainhand();
		if(item == null || item.getItem() != ItemWarpCard.INSTANCE){
            // Cardless Poke!
            TileWarpController c = tc.getController();
            if(c == null)
                return false;
            List<WarpInterface> interfaces = ItemWarpCard.getInterfaces(tc.card(), EnumWarpInterface.Button);
            if(!WarpInterface.canInterface(interfaces, ItemWarpCard.getFilters(tc.card()), facing, EnumInterfaceDirection.In, item))
                return false;
            if(!worldIn.isRemote)
                c.touch(new PacketActivate(tc, playerIn, hand, facing, hitX, hitY, hitZ));
            return true;
        }
		
        NBTTagCompound nbt = item.getTagCompound();
		if(nbt==null)
            return false;
        
        if(!nbt.hasKey("controllerID"))
            return false;

            
        BlockPos targetPos = new BlockPos(nbt.getDouble("targetX"), nbt.getDouble("targetY"), nbt.getDouble("targetZ"));
        int cID = nbt.getInteger("controllerID");
        World world = nbt.hasKey("dimension") ? DimensionManager.getWorld(nbt.getInteger("dimension")) : worldIn;
        if(!BlockWarpController.isController(world, targetPos, cID))
            return false;
        
        if(worldIn.isRemote) // Client escapes here.
            return true;

        tc.DropCard();
        
        tc.storedCard.set(0, item);
        playerIn.setHeldItem(playerIn.getActiveHand(), ItemStack.EMPTY); // Delete the item from the player.
        tc.markDirty();

        worldIn.notifyBlockUpdate(pos, state, state, 2);

		return true;
	}
}