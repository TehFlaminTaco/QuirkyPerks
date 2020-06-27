package co.ata.quirkyperks;

import java.util.ArrayList;
import java.util.List;

import co.ata.quirkyperks.blocks.BlockEnderCharger;
import co.ata.quirkyperks.blocks.BlockWarpController;
import co.ata.quirkyperks.blocks.BlockWarper;
import co.ata.quirkyperks.items.ItemFilter;
import co.ata.quirkyperks.items.ItemGeneric;
import co.ata.quirkyperks.items.ItemWarpCard;
import co.ata.quirkyperks.items.ItemWarpChip;
import co.ata.quirkyperks.tiles.TileEnderCharger;
import co.ata.quirkyperks.tiles.TileWarpController;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid=QuirkyPerks.MODID)
public class QuirkyEventHandler{
    static List<ItemGeneric> genericItems = new ArrayList<ItemGeneric>();

    public static ItemGeneric itemFauxenderdust;
    
    public QuirkyEventHandler(){
        genericItems.add(new ItemGeneric("infusedsteel", "ingotEnderSteel"));
        genericItems.add(new ItemGeneric("enderdust", "nuggetEnderpearl"));
        genericItems.add(itemFauxenderdust = new ItemGeneric("fauxenderdust"){
            @Override
            public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
                tooltip.add("Works just as good as the real deal!");
            }
        });
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(
            BlockWarpController.INSTANCE,
            BlockWarper.INSTANCE,
            BlockEnderCharger.INSTANCE
        );

        GameRegistry.registerTileEntity(TileWarpController.class,   BlockWarpController.INSTANCE.getRegistryName());
        GameRegistry.registerTileEntity(TileWarper.class,           BlockWarper.INSTANCE.getRegistryName());
        GameRegistry.registerTileEntity(TileEnderCharger.class,     BlockEnderCharger.INSTANCE.getRegistryName());
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
            // STANDALONE ITEMS
            ItemWarpCard.INSTANCE,
            ItemWarpChip.INSTANCE,
            ItemFilter.INSTANCE,

            // ITEM BLOCKS
            BlockWarpController.ITEM,
            BlockWarper.ITEM,
            BlockEnderCharger.ITEM
        );
        for(ItemGeneric i : genericItems){
            event.getRegistry().register(i);
            i.registerOres();
        }
    }

    @SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		registerRender(BlockWarpController.ITEM);
        registerRender(BlockWarper.ITEM);
        registerRender(BlockEnderCharger.ITEM);
        for(Item i : genericItems){
            registerRender(i);
        }
        registerRender(ItemWarpCard.INSTANCE);
        registerRender(ItemWarpChip.INSTANCE);
        registerRender(ItemFilter.INSTANCE);
	}
	
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}