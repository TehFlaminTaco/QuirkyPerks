package co.ata.quirkyperks.gui;

import co.ata.quirkyperks.QuirkyPerks;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class    GUIEnderCharger extends GuiContainer implements ITooltipSetter{

    private static final ResourceLocation TEX_BG = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/endercharger.png");

    public final ContainerEnderCharger container;
    InventoryPlayer playerInv;
    int currentInterface = 0;

    @Override
    public void initGui() {
        ySize = 189;
        super.initGui();
    }

    public GUIEnderCharger(ContainerEnderCharger container, InventoryPlayer playerInv)
    {
        super(container);
        this.container = container;
        this.playerInv = playerInv;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();

        GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(TEX_BG);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

    }

    @Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = I18n.format("tile.endercharger.name");
		fontRenderer.drawString(name, 8, 6, 0xFFFFFF);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 110, 0xFFFFFF);

        if(!tooltip.isEmpty())
            drawHoveringText(tooltip, mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
        tooltip = "";

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        renderHoveredToolTip(mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
	}


    String tooltip = "";
    @Override
    public void setTooltip(String newTip) {
        tooltip = newTip;
    }
}