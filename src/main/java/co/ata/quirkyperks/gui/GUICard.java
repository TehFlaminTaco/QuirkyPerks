package co.ata.quirkyperks.gui;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.PacketUpdateCard;
import co.ata.quirkyperks.QuirkyPacketHandler;
import co.ata.quirkyperks.QuirkyPerks;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.items.ItemWarpCard;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUICard extends GuiContainer implements ITooltipSetter{

    private static final ResourceLocation TEX_BG = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/card.png");
    private static final ResourceLocation TEX_BGF = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/cardf.png");

    private static final ResourceLocation TEX_IN = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/arrow_in.png"); 
    private static final ResourceLocation TEX_OUT = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/arrow_out.png");

    private static final ResourceLocation TEX_SELECTED = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/selected.png"); 

    private static final ResourceLocation TEX_PLUS = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/plus.png"); 
    private static final ResourceLocation TEX_MINUS = new ResourceLocation(QuirkyPerks.MODID, "textures/gui/minus.png");
    private static final HashMap<EnumFacing, ResourceLocation> TEX_DIRECTION = new HashMap<EnumFacing, ResourceLocation>();

    public final ItemStack card;
    public final ContainerCard container;
    public GUIWarpText priorityField = null;
    InventoryPlayer playerInv;
    List<WarpInterface> interfaces = new ArrayList<WarpInterface>();
    int currentInterface = 0;

    @Override
    public void initGui() {
        ySize = 214;

        TEX_DIRECTION.put(EnumFacing.NORTH, new ResourceLocation(QuirkyPerks.MODID, "textures/gui/dir_north.png"));
        TEX_DIRECTION.put(EnumFacing.EAST, new ResourceLocation(QuirkyPerks.MODID, "textures/gui/dir_east.png"));
        TEX_DIRECTION.put(EnumFacing.SOUTH, new ResourceLocation(QuirkyPerks.MODID, "textures/gui/dir_south.png"));
        TEX_DIRECTION.put(EnumFacing.WEST, new ResourceLocation(QuirkyPerks.MODID, "textures/gui/dir_west.png"));
        TEX_DIRECTION.put(EnumFacing.UP, new ResourceLocation(QuirkyPerks.MODID, "textures/gui/dir_up.png"));
        TEX_DIRECTION.put(EnumFacing.DOWN, new ResourceLocation(QuirkyPerks.MODID, "textures/gui/dir_down.png"));

        super.initGui();

        priorityField = new GUIWarpText(32, fontRenderer, 44, 84, 70, 16);
        priorityField.setFocused(true);
        generateButtons();
        
    }

    private void generateButtons() {
        buttonList.clear();

        if(interfaces.size() == 0){
            addButton(new GUIWarpButton(this, I18n.format("quirky.gui.add_interface"),
                0, (width - xSize) / 2 + 8, (height - ySize) / 2 + 20,
                TEX_PLUS
            ));
            priorityField.setVisible(false);
            return;
        }else if(interfaces.size() < 8){
            addButton(new GUIWarpButton(this, I18n.format("quirky.gui.add_interface"),
                0, (width - xSize) / 2 + 8 + (18 * interfaces.size()), (height - ySize) / 2 + 20,
                TEX_PLUS
            ));
        }
        priorityField.setVisible(true);
        priorityField.setText("" + interfaces.get(currentInterface).priority);

        addButton(new GUIWarpButton(this, I18n.format("quirky.gui.remove_interface"),
            1, (width + xSize) / 2 - 16 - 8, (height - ySize) / 2 + 8,
            TEX_MINUS
        ));

        for(int i = 0; i < interfaces.size(); i++) { // Add a new button for each interface.
            GuiButton b = addButton(new GUIWarpButton(2 + i, (width - xSize) / 2 + (8 + 18 * i), (height - ySize) / 2 + 20, interfaces.get(i).type.icon));
            if(i == currentInterface)
                b.enabled = false;
        }

        WarpInterface curInt = interfaces.get(currentInterface);
        EnumWarpInterface[] itfs = EnumWarpInterface.values();
        for(int i = 0; i < itfs.length; i++){
            GuiButton b = addButton(new GUIWarpButton(this, itfs[i].name(),
                2 + interfaces.size() + i, (width + xSize) / 2 - 16 - 8, (height - ySize) / 2 + 38 + i * 18, itfs[i].icon
            ));
            if(itfs[i] == curInt.type)
                b.enabled = false;
        }

        EnumFacing[] fs = EnumFacing.values();
        for(int i = 0; i < fs.length; i++){

            if(curInt.inSides[i])
                addButton(new GUIWarpButton(this, "IN: "+fs[i].name(),
                    2 + interfaces.size() + itfs.length + i, (width - xSize) / 2 + 26 + (18 * i), (height - ySize) / 2 + 48,
                    TEX_SELECTED, TEX_DIRECTION.get(fs[i])
                ));
            else
                addButton(new GUIWarpButton(this, "IN: "+fs[i].name(),
                    2 + interfaces.size() + itfs.length + i, (width - xSize) / 2 + 26 + (18 * i), (height - ySize) / 2 + 48,
                    TEX_DIRECTION.get(fs[i])
                ));

            if(curInt.outSides[i])
                addButton(new GUIWarpButton(this, "OUT: "+fs[i].name(),
                    2 + interfaces.size() + itfs.length + fs.length + i, (width - xSize) / 2 + 26 + (18 * i), (height - ySize) / 2 + 66,
                    TEX_SELECTED, TEX_DIRECTION.get(fs[i])
                ));
            else
                addButton(new GUIWarpButton(this, "OUT: "+fs[i].name(),
                    2 + interfaces.size() + itfs.length + fs.length + i, (width - xSize) / 2 + 26 + (18 * i), (height - ySize) / 2 + 66,
                    TEX_DIRECTION.get(fs[i])
                ));
        }



    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if(button.enabled){
            if(button.id == 0){
                if(interfaces.size() < 8){
                    interfaces.add(new WarpInterface(interfaces.size()));
                    currentInterface = interfaces.size()-1;
                    QuirkyPacketHandler.INSTANCE.sendToServer(new PacketUpdateCard(currentInterface));
                    NBTTagCompound tagHolder = new NBTTagCompound();
                    NBTTagList outIfs = new NBTTagList();
                    tagHolder.setTag("interfaces", outIfs);
                    for(int i=0; i < interfaces.size(); i++){
                        NBTTagCompound nbt = new NBTTagCompound();
                        interfaces.get(i).toNBT(nbt);
                        outIfs.appendTag(nbt);
                    }
                    QuirkyPacketHandler.INSTANCE.sendToServer(new PacketUpdateCard(tagHolder));
                    container.filterSlot = currentInterface;
                    priorityField.setText(""+interfaces.get(currentInterface).priority);
                    generateButtons();
                }
            }
            if(button.id >= 2 && button.id < interfaces.size() + 2){
                currentInterface = button.id - 2;
                priorityField.setText(""+interfaces.get(currentInterface).priority);
                QuirkyPacketHandler.INSTANCE.sendToServer(new PacketUpdateCard(currentInterface));
                container.filterSlot = currentInterface;
                generateButtons();
            }
            EnumWarpInterface[] ewis = EnumWarpInterface.values();
            EnumFacing[] fs = EnumFacing.values();
            if(button.id >= 2 + interfaces.size() && button.id < 2 + interfaces.size() + ewis.length){
                interfaces.get(currentInterface).type = EnumWarpInterface.values()[button.id - 2 - interfaces.size()];
                generateButtons();
            }
            if(button.id >= 2 + interfaces.size() + ewis.length && button.id < 2 + interfaces.size() + ewis.length + fs.length){
                interfaces.get(currentInterface).inSides[button.id - (2 + interfaces.size() + ewis.length)] = !interfaces.get(currentInterface).inSides[button.id - (2 + interfaces.size() + ewis.length)];
                generateButtons();
            }
            if(button.id >= 2 + interfaces.size() + ewis.length + fs.length && button.id < 2 + interfaces.size() + ewis.length + fs.length + fs.length){
                interfaces.get(currentInterface).outSides[button.id - (2 + interfaces.size() + ewis.length + fs.length)] = !interfaces.get(currentInterface).outSides[button.id - (2 + interfaces.size() + ewis.length + fs.length)];
                generateButtons();
            }
        }

    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        priorityField.textboxKeyTyped(typedChar, keyCode);
        if(interfaces.size() > 0){
            WarpInterface inf = interfaces.get(currentInterface);
            try{
                inf.priority = Integer.parseInt(priorityField.getText());
            }catch(Exception e){
                inf.priority = 0;
            }
        }
        
        if(keyCode == 1)
            this.mc.player.closeScreen();

        /*this.serverNameField.textboxKeyTyped(typedChar, keyCode);
        this.serverIPField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 15)
        {
            this.serverNameField.setFocused(!this.serverNameField.isFocused());
            this.serverIPField.setFocused(!this.serverIPField.isFocused());
        }

        if (keyCode == 28 || keyCode == 156)
        {
            this.actionPerformed(this.buttonList.get(0));
        }

        (this.buttonList.get(0)).enabled = !this.serverIPField.getText().isEmpty() && this.serverIPField.getText().split(":").length > 0 && !this.serverNameField.getText().isEmpty();*/
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        System.out.println(String.format("%s, %s", mouseX, mouseY));
        priorityField.mouseClicked(mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2), mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        NBTTagCompound tagHolder = new NBTTagCompound();
        NBTTagList outIfs = new NBTTagList();
        tagHolder.setTag("interfaces", outIfs);
        for(int i=0; i < interfaces.size(); i++){
            NBTTagCompound nbt = new NBTTagCompound();
            interfaces.get(i).toNBT(nbt);
            outIfs.appendTag(nbt);
        }
        QuirkyPacketHandler.INSTANCE.sendToServer(new PacketUpdateCard(tagHolder));
    }

    public GUICard(ContainerCard container, InventoryPlayer playerInv)
    {
        super(container);
        this.card = container.card;
        this.container = container;

        this.playerInv = playerInv;

        interfaces = ItemWarpCard.getInterfaces(card);
        
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();

        GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(interfaces.size()>0 ? TEX_BGF : TEX_BG);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

    }

    @Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = I18n.format(card.getDisplayName());
		fontRenderer.drawString(name, 8, 6, 0xFFFFFF);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 110, 0xFFFFFF);
        
        if(interfaces.size() > 0){
            GlStateManager.scale(1f/16f, 1f/16f, 1f/16f);

            mc.getTextureManager().bindTexture(TEX_IN);
            drawTexturedModalRect(16 * 8, 16 * 48, 0, 0, 256, 256);
            mc.getTextureManager().bindTexture(TEX_OUT);
            drawTexturedModalRect(16 * 8, 16 * 66, 0, 0, 256, 256);

            GlStateManager.scale(16f, 16f, 16f);

            if(priorityField.getVisible()){
                boolean isInt = false;
                try{
                    Integer.parseInt(priorityField.getText());
                    isInt = true;
                }catch(Exception e){};
                priorityField.drawTextBox(isInt ? 0xE0E0E0 : 0xFF0000);
            }
        }

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