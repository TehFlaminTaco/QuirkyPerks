package co.ata.quirkyperks;

import co.ata.quirkyperks.gui.ContainerCard;
import co.ata.quirkyperks.gui.ContainerEnderCharger;
import co.ata.quirkyperks.gui.ContainerFilter;
import co.ata.quirkyperks.gui.GUICard;
import co.ata.quirkyperks.gui.GUIEnderCharger;
import co.ata.quirkyperks.gui.GUIFilter;
import co.ata.quirkyperks.tiles.TileEnderCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
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
            case ContainerEnderCharger.GUI_ID:
                return new ContainerEnderCharger(player.inventory, (TileEnderCharger)world.getTileEntity(new BlockPos(x,y,z)));
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
            case ContainerEnderCharger.GUI_ID:
                return new GUIEnderCharger(new ContainerEnderCharger(player.inventory, (TileEnderCharger)world.getTileEntity(new BlockPos(x,y,z))), player.inventory);
        }
        return null;
    }
}