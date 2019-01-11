package co.ata.quirkyperks;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketUpdateCard implements IMessage {

    NBTTagCompound tags = new NBTTagCompound();
    int setInterface = -1;

    public PacketUpdateCard(){

    }

    public PacketUpdateCard(NBTTagCompound tags){
        this.tags = tags;
    }

    public PacketUpdateCard(int setInterface){
        this.setInterface = setInterface;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        setInterface = buf.readInt();
        if(setInterface < 0)
            tags = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(setInterface);
        if(setInterface < 0)
            ByteBufUtils.writeTag(buf, tags);
	}

}