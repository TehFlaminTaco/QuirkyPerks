package co.ata.quirkyperks;

import co.ata.quirkyperks.gui.ContainerCard;
import co.ata.quirkyperks.items.ItemWarpCard;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandleUpdateCard implements IMessageHandler<PacketUpdateCard, IMessage> {

    @Override
    public IMessage onMessage(PacketUpdateCard message, MessageContext ctx) {
         // This is the player the packet was sent to the server from
        EntityPlayerMP player = ctx.getServerHandler().player;
        // Execute the action on the main server thread by adding it as a scheduled task
        player.getServerWorld().addScheduledTask(() -> {
            ItemStack card = player.getHeldItemMainhand();
            if(card.isEmpty())
                return;
            if(card.getItem() != ItemWarpCard.INSTANCE)
                return;
            if(message.setInterface < 0){
                NBTTagCompound cardNBT = card.hasTagCompound() ? card.getTagCompound() : new NBTTagCompound();
                for(String key : message.tags.getKeySet()){
                    cardNBT.setTag(key, message.tags.getTag(key));
                }
                card.setTagCompound(cardNBT);
            }else{
                if(player.openContainer instanceof ContainerCard){
                    ((ContainerCard)player.openContainer).filterSlot = message.setInterface;
                }
            }
        });
        // No response packet
        return null;
	}

}