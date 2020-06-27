package co.ata.quirkyperks.blocks;

import java.util.Random;

import co.ata.quirkyperks.QuirkyPerks;
import co.ata.quirkyperks.gui.ContainerEnderCharger;
import co.ata.quirkyperks.tiles.TileEnderCharger;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEnderCharger extends BlockContainer {
    public static BlockEnderCharger INSTANCE = new BlockEnderCharger();
	public static Item ITEM = new ItemBlock(INSTANCE).setRegistryName(INSTANCE.getRegistryName());

    public BlockEnderCharger(){
        super(Material.IRON);
		setHardness(2.0f);
		setUnlocalizedName("endercharger");
		setRegistryName("endercharger");
        //setCreativeTab(QuirkyPerks.tabQuirkyPerks);
        setTickRandomly(true);
    }

    @Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEnderCharger();
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand){
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEnderCharger){
            TileEnderCharger tec = (TileEnderCharger) te;
            tec.Charge();
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        playerIn.openGui(QuirkyPerks.INSTANCE, ContainerEnderCharger.GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}