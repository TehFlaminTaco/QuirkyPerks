package co.ata.quirkyperks.gui;

import java.util.Arrays;
import java.util.List;

import co.ata.quirkyperks.QuirkyPerks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIWarpButton extends GuiButton
{
    private final List<ResourceLocation> resourceLocations;
    ITooltipSetter screen = null;
    String tooltip = "";
    private static final ResourceLocation back = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/button.png");
    

    public GUIWarpButton(int buttonID, int x, int y, List<ResourceLocation> buttonTexs)
    {
        super(buttonID, x, y, 16, 16, "");
        visible = true;
        this.resourceLocations = buttonTexs;
    }

    public GUIWarpButton(int buttonID, int x, int y, ResourceLocation... buttonTexs)
    {
        super(buttonID, x, y, 16, 16, "");
        visible = true;
        this.resourceLocations = Arrays.asList(buttonTexs);
    }

    public GUIWarpButton(ITooltipSetter screen, String tooltip, int buttonID, int x, int y, ResourceLocation... buttonTexs)
    {
        super(buttonID, x, y, 16, 16, "");
        this.screen = screen;
        this.tooltip = tooltip;
        visible = true;
        this.resourceLocations = Arrays.asList(buttonTexs);
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.color(1, 1, 1, 1);

            GlStateManager.disableDepth();
            GlStateManager.scale(1f/16f, 1f/8f, 1f/16f);
            mc.getTextureManager().bindTexture(GUIWarpButton.back);
            drawTexturedModalRect(this.x * 16, this.y * 8, 0, enabled ? 0 : 128, this.width * 16, this.height * 8);

            GlStateManager.scale(1f, 1/2f, 1f);

            for(ResourceLocation l : resourceLocations){
                mc.getTextureManager().bindTexture(l);
                drawTexturedModalRect(this.x * 16, this.y * 16, 0, 0, this.width * 16, this.height * 16);
            }

            

            GlStateManager.scale(16f, 16f, 16f);

            if(hovered && screen!=null){
                screen.setTooltip(tooltip);
            }

            GlStateManager.enableDepth();
        }
    }
}