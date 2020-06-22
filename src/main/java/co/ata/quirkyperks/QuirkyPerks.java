package co.ata.quirkyperks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;


import co.ata.quirkyperks.items.ItemWarpCard;

@Mod(modid = QuirkyPerks.MODID, name = QuirkyPerks.NAME, version = QuirkyPerks.VERSION)
public class QuirkyPerks
{
    public static final String MODID = "quirkyperks";
    public static final String NAME = "QuirkyPerks";
    public static final String VERSION = "1.2";


    public static QuirkyPerks INSTANCE;

    public static final CreativeTabs tabQuirkyPerks = (new CreativeTabs("tabQuirkyPerks"){
        @Override
        public ItemStack getTabIconItem(){
            return new ItemStack(ItemWarpCard.INSTANCE);
        }
    });

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        INSTANCE = this;
        MinecraftForge.EVENT_BUS.register(new QuirkyEventHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiProxy());
        QuirkyPacketHandler.INSTANCE.registerMessage(PacketHandleUpdateCard.class, PacketUpdateCard.class, 0, Side.SERVER);
        QuirkyPacketHandler.INSTANCE.registerMessage(PacketHandleUpdateFilter.class, PacketUpdateFilter.class, 1, Side.SERVER);
    }
}
