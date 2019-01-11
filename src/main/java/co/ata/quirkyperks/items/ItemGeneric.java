package co.ata.quirkyperks.items;

import co.ata.quirkyperks.QuirkyPerks;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class ItemGeneric extends Item {
    public ItemGeneric(String regName, String... oreName){
        super();
        setCreativeTab(QuirkyPerks.tabQuirkyPerks);
        setUnlocalizedName(regName);
        setRegistryName(regName);
        setMaxStackSize(64);
        
        for(String s : oreName)
            OreDictionary.registerOre(s, this);
    }
}