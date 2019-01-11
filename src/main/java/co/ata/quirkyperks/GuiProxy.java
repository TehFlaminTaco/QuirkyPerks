package co.ata.quirkyperks;

import co.ata.quirkyperks.gui.ContainerCard;
import co.ata.quirkyperks.gui.ContainerFilter;
import co.ata.quirkyperks.gui.GUICard;
import co.ata.quirkyperks.gui.GUIFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID){
            case ContainerCard.GUI_ID:
                return new ContainerCard(player.inventory, player.getHeldItemMainhand());
            case ContainerFilter.GUI_ID:
                return new ContainerFilter(player.inventory, player.getHeldItemMainhand());
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID){
            case ContainerCard.GUI_ID:
                return new GUICard(new ContainerCard(player.inventory, player.getHeldItemMainhand()), player.inventory);
            case ContainerFilter.GUI_ID:
                return new GUIFilter(new ContainerFilter(player.inventory, player.getHeldItemMainhand()), player.inventory);
        }
        return null;
    }
}