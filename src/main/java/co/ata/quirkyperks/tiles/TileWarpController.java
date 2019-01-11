package co.ata.quirkyperks.tiles;


import java.util.HashSet;
import java.util.Hashtable;

import co.ata.quirkyperks.RequestInfo;
import co.ata.quirkyperks.packet.Packet;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileWarpController extends TileEntity implements ITickable {
    public int controllerID = ThreadLocalRandom.current().nextInt(0, 9999);
    public HashSet<TileWarper> knownWarpers = new HashSet<TileWarper>();
    public HashSet<RequestInfo> requests = new HashSet<RequestInfo>();
    public Hashtable<RequestInfo, Packet> finishedRequests = new Hashtable<RequestInfo, Packet>();


    public TileWarpController(){
        super();
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if(compound.hasKey("controllerID"))
            controllerID = compound.getInteger("controllerID");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("controllerID", controllerID);
        System.out.println(String.format("WRITING TO COMPOUND %s", controllerID));
        return compound;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        compound.setInteger("controllerID", controllerID);
        return compound;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        controllerID = tag.getInteger("controllerID");
        super.handleUpdateTag(tag);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        NBTTagCompound nbtTag = new NBTTagCompound();
        nbtTag.setInteger("controllerID", controllerID);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
        NBTTagCompound tag = pkt.getNbtCompound();
        controllerID = tag.getInteger("controllerID");
    }

    @SuppressWarnings("unchecked")
    public <T extends Packet> T touch(T p){
        knownWarpers.add(p.source);
        RequestInfo ri = new RequestInfo(p.source, p.type);
        for(RequestInfo rr : requests){
            if(rr.equals(ri)){
                return (T)p.GetBlank();
            }
        }
        
        requests.add(ri);
        

        for (TileWarper t : (HashSet<TileWarper>)knownWarpers.clone()) {
            if(t.getPos() != p.source.getPos() && t.getController() == this)
                p.touch(t);
            if(p.burned) // Stop sorting through if it burns.
                break;
        }
        
        requests.remove(ri);
        return p;
    }

    int cleanCheck = 0;
    @SuppressWarnings("unchecked")
    @Override
    public void update() {
        requests.clear();
        
        cleanCheck++;
        if(cleanCheck > 10){
            cleanCheck = 0;
            HashSet<TileWarper> toDelete = new HashSet<TileWarper>();
            for (TileWarper t : (HashSet<TileWarper>)knownWarpers.clone()) 
                if(t.getController() != this)
                    toDelete.add(t);
            knownWarpers.removeAll(toDelete);
        }
    }

	public void wake(TileWarper tileWarper) {
        knownWarpers.add(tileWarper);
	}
}