package co.ata.quirkyperks.blocks;

import java.util.List;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.QuirkyPerks;
import co.ata.quirkyperks.QuirkyProxy;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.items.IWarpCardBase;
import co.ata.quirkyperks.items.ItemWarpChip;
import co.ata.quirkyperks.packet.PacketActivate;
import co.ata.quirkyperks.tiles.TileWarpController;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWarpPlate extends BlockContainer {

    public static BlockWarpPlate INSTANCE = new BlockWarpPlate();
    public static Item ITEM = new ItemBlock(INSTANCE).setRegistryName(INSTANCE.getRegistryName());
    public PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockWarpPlate(){
        super(Material.IRON);
        setHardness(2.0f);
		setUnlocalizedName("warpplate");
        setRegistryName("warpplate");
        setCreativeTab(QuirkyPerks.tabQuirkyPerks);
        setLightOpacity(0);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
        fullBlock = false;
    }

    protected static final AxisAlignedBB AABB_PLATE_DOWN = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
    protected static final AxisAlignedBB AABB_PLATE_UP = new AxisAlignedBB(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_PLATE_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
    protected static final AxisAlignedBB AABB_PLATE_EAST = new AxisAlignedBB(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_PLATE_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_PLATE_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing face = state.getValue(FACING);
        if(face == EnumFacing.NORTH)
            return AABB_PLATE_NORTH;
        else if(face == EnumFacing.EAST)
            return AABB_PLATE_EAST;
        else if(face == EnumFacing.SOUTH)
            return AABB_PLATE_SOUTH;
        else if(face == EnumFacing.WEST)
            return AABB_PLATE_WEST;
        else if(face == EnumFacing.UP)
            return AABB_PLATE_UP;
        else
            return AABB_PLATE_DOWN;
    }

    @Override
	protected BlockStateContainer createBlockState() {
		 return new BlockStateContainer(this, new IProperty[] {FACING == null ? FACING = PropertyDirection.create("facing") : FACING});
    }
    public IBlockState getStateFromMeta(int meta) {
	    return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
    }

    public boolean isTopSolid(IBlockState state)
    {
        return state.getValue(FACING) == EnumFacing.UP;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return state.getValue(FACING) == face ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return state.getValue(FACING) == face;
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing mirror = EnumFacing.DOWN;
        if(facing == EnumFacing.DOWN) mirror = EnumFacing.UP;
        if(facing == EnumFacing.NORTH) mirror = EnumFacing.SOUTH;
        if(facing == EnumFacing.SOUTH) mirror = EnumFacing.NORTH;
        if(facing == EnumFacing.EAST) mirror = EnumFacing.WEST;
        if(facing == EnumFacing.WEST) mirror = EnumFacing.EAST;
        return this.getDefaultState().withProperty(FACING, mirror);
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
		if(item == null || item.getItem() != ItemWarpChip.INSTANCE){
            // Cardless Poke!
            TileWarpController c = tc.getController();
            if(c == null)
                return false;
            List<WarpInterface> interfaces = IWarpCardBase.getInterfacesFromItem(tc.card(), EnumWarpInterface.Button);
            if(!WarpInterface.canInterface(interfaces, IWarpCardBase.getFiltersFromItem(tc.card()), facing, EnumInterfaceDirection.In, item))
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
        World world = QuirkyProxy.getWorldFromID(nbt.hasKey("dimension") ? nbt.getInteger("dimension") : null, worldIn);
        
        if(world == null)
            return worldIn.isRemote; // Strange I know, but the behaviour I want. 

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