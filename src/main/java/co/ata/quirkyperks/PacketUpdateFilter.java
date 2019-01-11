package co.ata.quirkyperks;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketUpdateFilter implements IMessage {

    NBTTagCompound tags = new NBTTagCompound();
    boolean resetSlots = false;

    public PacketUpdateFilter(){

    }

    public PacketUpdateFilter(NBTTagCompound tags){
        this.tags = tags;
    }

    public PacketUpdateFilter(boolean slots){
        this.resetSlots = slots;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        resetSlots = buf.readBoolean();
        if(!resetSlots)
            tags = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(resetSlots);
        if(!resetSlots)
            ByteBufUtils.writeTag(buf, tags);
	}

}