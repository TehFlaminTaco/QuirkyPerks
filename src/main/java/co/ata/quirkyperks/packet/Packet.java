package co.ata.quirkyperks.packet;

import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.tiles.TileWarper;

public abstract class Packet {
    public TileWarper source;
    public EnumRequestType type;
    public boolean burned = false;

    public Packet(TileWarper source, EnumRequestType type){
        this.source = source;
        this.type = type;
    }

    public void Burn(){
        burned = true;
    }

    public abstract void touch(TileWarper target);

    public abstract Packet GetBlank();
}