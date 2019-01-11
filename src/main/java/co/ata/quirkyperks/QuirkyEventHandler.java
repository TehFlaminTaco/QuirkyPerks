package co.ata.quirkyperks;

import java.util.ArrayList;
import java.util.List;

import co.ata.quirkyperks.blocks.BlockWarpController;
import co.ata.quirkyperks.blocks.BlockWarper;
import co.ata.quirkyperks.items.ItemFilter;
import co.ata.quirkyperks.items.ItemGeneric;
import co.ata.quirkyperks.items.ItemWarpCard;
import co.ata.quirkyperks.tiles.TileWarpController;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid=QuirkyPerks.MODID)
public class QuirkyEventHandler{
    static List<Item> genericItems = new ArrayList<Item>();
 
    public QuirkyEventHandler(){
        genericItems.add(new ItemGeneric("infusedsteel", "ingotEnder"));
        genericItems.add(new ItemGeneric("enderdust", "dustEnder"));
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(
            BlockWarpController.INSTANCE,
            BlockWarper.INSTANCE
        );

        GameRegistry.registerTileEntity(TileWarpController.class,   BlockWarpController.INSTANCE.getRegistryName());
        GameRegistry.registerTileEntity(TileWarper.class,           BlockWarper.INSTANCE.getRegistryName());
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
            // STANDALONE ITEMS
            ItemWarpCard.INSTANCE,
            ItemFilter.INSTANCE,

            // ITEM BLOCKS
            BlockWarpController.ITEM,
            BlockWarper.ITEM
        );
        for(Item i : genericItems){
            event.getRegistry().register(i);
        }
    }

    @SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		registerRender(BlockWarpController.ITEM);
        registerRender(BlockWarper.ITEM);
        for(Item i : genericItems){
            registerRender(i);
        }
        registerRender(ItemWarpCard.INSTANCE);
        registerRender(ItemFilter.INSTANCE);
	}
	
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}