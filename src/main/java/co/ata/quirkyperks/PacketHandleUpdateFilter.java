package co.ata.quirkyperks;

import co.ata.quirkyperks.gui.ContainerFilter;
import co.ata.quirkyperks.items.ItemFilter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandleUpdateFilter implements IMessageHandler<PacketUpdateFilter, IMessage> {

    @Override
    public IMessage onMessage(PacketUpdateFilter message, MessageContext ctx) {
         // This is the player the packet was sent to the server from
        EntityPlayerMP player = ctx.getServerHandler().player;
        // Execute the action on the main server thread by adding it as a scheduled task
        player.getServerWorld().addScheduledTask(() -> {
            ItemStack filter = player.getHeldItemMainhand();
            if(filter.isEmpty())
                return;
            if(filter.getItem() != ItemFilter.INSTANCE)
                return;
            NBTTagCompound cardNBT = filter.hasTagCompound() ? filter.getTagCompound() : new NBTTagCompound();
            cardNBT.setTag("filter_options", message.tags);
            if(message.resetSlots){
                if(player.openContainer instanceof ContainerFilter){
                    ((ContainerFilter)player.openContainer).clear();
                }
                //player.openGui(QuirkyPerks.INSTANCE, ContainerFilter.GUI_ID, player.world, (int)player.posX, (int)player.posY, (int)player.posZ);
            }else{
                filter.setTagCompound(cardNBT);
            }
        });
        // No response packet
        return null;
	}

}