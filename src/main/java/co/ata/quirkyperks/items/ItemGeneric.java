package co.ata.quirkyperks.items;

import co.ata.quirkyperks.QuirkyPerks;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class ItemGeneric extends Item {
    public String[] ores;
    public ItemGeneric(String regName, String... oreName){
        super();
        setCreativeTab(QuirkyPerks.tabQuirkyPerks);
        setUnlocalizedName(regName);
        setRegistryName(regName);
        setMaxStackSize(64);
        
        ores = oreName;
    }

    public void registerOres(){
        for(String s : ores)
            OreDictionary.registerOre(s, this);
    }
}