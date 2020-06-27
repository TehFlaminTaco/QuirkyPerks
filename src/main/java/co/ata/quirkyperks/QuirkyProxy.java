package co.ata.quirkyperks;

import java.util.HashMap;
import net.minecraft.world.World;

public class QuirkyProxy {
    public static HashMap<Integer, World> serverWorlds = new HashMap<Integer, World>();
    public static HashMap<Integer, World> clientWorlds = new HashMap<Integer, World>();

    public static World getWorldFromID(Integer id, World ifNull){
        if(ifNull==null)
            return null;
        if(id == null)
            return ifNull;
        if(ifNull.isRemote)
            return clientWorlds.get(id);
        else
            return serverWorlds.get(id);
    }
    
    public static void pokeWorld(World world){
        int id = world.provider.getDimension();
        if(world.isRemote)
            clientWorlds.put(id, world);
        else
            serverWorlds.put(id, world);
    }
}