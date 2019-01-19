package co.ata.quirkyperks.blocks;

import co.ata.quirkyperks.QuirkyPerks;
import co.ata.quirkyperks.tiles.TileEnderCharger;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class BlockEnderCharger extends BlockContainer {
    public static BlockEnderCharger INSTANCE = new BlockEnderCharger();
	public static Item ITEM = new ItemBlock(INSTANCE).setRegistryName(INSTANCE.getRegistryName());

    public BlockEnderCharger(){
        super(Material.IRON);
		setHardness(2.0f);
		setUnlocalizedName("endercharger");
		setRegistryName("endercharger");
        setCreativeTab(QuirkyPerks.tabQuirkyPerks);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEnderCharger();
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}