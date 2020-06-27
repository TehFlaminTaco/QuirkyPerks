package co.ata.quirkyperks.blocks;

import co.ata.quirkyperks.QuirkyPerks;
import co.ata.quirkyperks.items.IWarpCardBase;
import co.ata.quirkyperks.tiles.TileWarpController;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWarpController extends Block{

	public static BlockWarpController INSTANCE = new BlockWarpController();
	public static Item ITEM = new ItemBlock(INSTANCE).setRegistryName(INSTANCE.getRegistryName());

	public BlockWarpController() {
		super(Material.IRON);
		setHardness(2.0f);
		setUnlocalizedName("warpcontroller");
		setRegistryName("warpcontroller");
		setCreativeTab(QuirkyPerks.tabQuirkyPerks);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileWarpController();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		ItemStack item = playerIn.getHeldItemMainhand();
		if(item == null)
			return false;
		if(!(item.getItem() instanceof IWarpCardBase))
			return false;
		
		NBTTagCompound nbt;
		if(item.hasTagCompound())
			nbt = item.getTagCompound();
		else
			nbt = new NBTTagCompound();
		item.setTagCompound(nbt);

		TileEntity te = worldIn.getTileEntity(pos);
		if(!(te instanceof TileWarpController)){
			return false;
		}
		TileWarpController tc = (TileWarpController)te;

		if(worldIn.isRemote) // Client escapes here.
			return true;


		nbt.setInteger("controllerID", tc.controllerID);
		nbt.setDouble("targetX", pos.getX());
		nbt.setDouble("targetY", pos.getY());
		nbt.setDouble("targetZ", pos.getZ());
		nbt.setInteger("dimension", worldIn.provider.getDimension());
		item.setTagCompound(nbt);
		

		return true;
	}

	public static boolean isController(World worldIn, BlockPos point, int controllerID){
		TileEntity te = worldIn.getTileEntity(point);
		if(te == null){
			return false;
		}
		if(!(te instanceof TileWarpController)){
			return false;
		}
		TileWarpController tc = (TileWarpController)te;

		return controllerID == tc.controllerID;
	}
	
}