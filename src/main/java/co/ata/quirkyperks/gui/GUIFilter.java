package co.ata.quirkyperks.gui;


import java.io.IOException;

import co.ata.quirkyperks.PacketUpdateFilter;
import co.ata.quirkyperks.QuirkyPacketHandler;
import co.ata.quirkyperks.QuirkyPerks;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIFilter extends GuiContainer implements ITooltipSetter{

    private static final ResourceLocation TEX_BG = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/filter.png");

    private static final ResourceLocation TEX_SELECTED = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/selected.png"); 
    private static final ResourceLocation TEX_CROSS = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/cross.png");
    private static final ResourceLocation TEX_WHITELIST = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/whitelist.png");
    private static final ResourceLocation TEX_BLACKLIST = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/blacklist.png");
    private static final ResourceLocation TEX_META = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/meta.png");
    private static final ResourceLocation TEX_NBT = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/dir_north.png");
    private static final ResourceLocation TEX_ORE = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/oredict.png");

    public final ItemStack filter;
    public final ContainerFilter container;
    InventoryPlayer playerInv;

    boolean whitelist = true; // The Default
    boolean meta = false;
    boolean nbt = false;
    boolean ore = false;


    @Override
    public void initGui() {
        ySize = 153;


        super.initGui();

        generateButtons();
    }

    private void generateButtons() {
        buttonList.clear();

        addButton(new GUIWarpButton(this, I18n.format("quirky.gui.reset_slots"),
            0, (width - xSize) / 2 + 152, (height - ySize) / 2 + 37,
            TEX_CROSS
        ));

        // We do this order because otherwise it spelt N O and honestly, not okay with that.
        if(whitelist)
            addButton(new GUIWarpButton(this, I18n.format("quirky.gui.whitelist.on"),
                1, (width - xSize) / 2 + 99, (height - ySize) / 2 + 55,
                TEX_WHITELIST
            ));
        else
            addButton(new GUIWarpButton(this, I18n.format("quirky.gui.whitelist.off"),
                1, (width - xSize) / 2 + 99, (height - ySize) / 2 + 55,
                TEX_BLACKLIST
            ));

        if(ore)        
            addButton(new GUIWarpButton(this, I18n.format("quirky.gui.ore_dict.on"),
                2, (width - xSize) / 2 + 117, (height - ySize) / 2 + 55,
                TEX_SELECTED, TEX_ORE
            ));
        else
            addButton(new GUIWarpButton(this, I18n.format("quirky.gui.ore_dict.off"),
                2, (width - xSize) / 2 + 117, (height - ySize) / 2 + 55,
                TEX_ORE
            ));

        if(meta)    
            addButton(new GUIWarpButton(this, I18n.format("quirky.gui.meta.on"),
                3, (width - xSize) / 2 + 134, (height - ySize) / 2 + 55,
                TEX_SELECTED, TEX_META
            ));
        else
            addButton(new GUIWarpButton(this, I18n.format("quirky.gui.meta.off"),
               3, (width - xSize) / 2 + 134, (height - ySize) / 2 + 55,
                TEX_META
            ));

        if(nbt)        
            addButton(new GUIWarpButton(this, I18n.format("quirky.gui.nbt.on"),
                4, (width - xSize) / 2 + 152, (height - ySize) / 2 + 55,
                TEX_SELECTED, TEX_NBT
            ));
        else
            addButton(new GUIWarpButton(this, I18n.format("quirky.gui.nbt.off"),
                4, (width - xSize) / 2 + 152, (height - ySize) / 2 + 55,
                TEX_NBT
            ));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if(button.enabled){
            switch(button.id){ // None of these are programatic, so we can just use a switch/case.
                case 0: // RESET FILTERS
                    container.clear();
                    QuirkyPacketHandler.INSTANCE.sendToServer(new PacketUpdateFilter(true));
                    break;
                case 1: // WHITELIST TOGGLE
                    whitelist = !whitelist;
                    saveFilterOptions();
                    break;
                case 2: // OREDICT TOGGLE
                    ore = !ore;
                    saveFilterOptions();
                    break;
                case 3: // META TOGGLE
                    meta = !meta;
                    saveFilterOptions();
                    break;
                case 4: // NBT TOGGLE
                    nbt = !nbt;
                    saveFilterOptions();
                    break;
            }
            generateButtons();
        }
    }

    private void saveFilterOptions(){
        NBTTagCompound filter_options = filter.getSubCompound("filter_options");
        filter_options.setBoolean("whitelist", whitelist);
        filter_options.setBoolean("meta", meta);
        filter_options.setBoolean("ore", ore);
        filter_options.setBoolean("nbt", nbt);
        QuirkyPacketHandler.INSTANCE.sendToServer(new PacketUpdateFilter(filter_options));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        saveFilterOptions();
    }

    public GUIFilter(ContainerFilter container, InventoryPlayer playerInv)
    {
        super(container);
        this.filter = container.filter;
        this.container = container;

        this.playerInv = playerInv;

        // Match stored filter options if any.
        NBTTagCompound filter_options = filter.getOrCreateSubCompound("filter_options");
        if(filter_options.hasKey("whitelist")) whitelist = filter_options.getBoolean("whitelist"); else filter_options.setBoolean("whitelist", whitelist);
        if(filter_options.hasKey("meta")) meta = filter_options.getBoolean("meta"); else filter_options.setBoolean("meta", meta);
        if(filter_options.hasKey("ore")) ore = filter_options.getBoolean("ore"); else filter_options.setBoolean("ore", ore);
        if(filter_options.hasKey("nbt")) nbt = filter_options.getBoolean("nbt"); else filter_options.setBoolean("nbt", nbt);        
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
		String name = I18n.format(filter.getDisplayName());
		fontRenderer.drawString(name, 8, 6, 0xFFFFFF);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 92, 0xFFFFFF);

        if(!tooltip.isEmpty())
            drawHoveringText(tooltip, mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
        tooltip = "";

        renderHoveredToolTip(mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

    String tooltip = "";
    @Override
    public void setTooltip(String newTip) {
        tooltip = newTip;
    }
}