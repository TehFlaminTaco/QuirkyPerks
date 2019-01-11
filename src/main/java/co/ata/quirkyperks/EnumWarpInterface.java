package co.ata.quirkyperks;

import net.minecraft.util.ResourceLocation;

public enum EnumWarpInterface {
    Button("textures/gui/warp_button.png"),
    Item("textures/gui/warp_item.png"),
    Energy("textures/gui/warp_energy.png"),
    Fluid("textures/gui/warp_fluid.png");

    
    public final ResourceLocation icon;

    EnumWarpInterface(String icon_name){
        icon = new ResourceLocation(QuirkyPerks.MODID, icon_name);
    }
}