package co.ata.quirkyperks.packet;

import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.tiles.TileWarper;

public abstract class Packet {
    public TileWarper source;
    public EnumRequestType type;
    public boolean burned = false;
    public EnumWarpInterface iface;

    public Packet(TileWarper source, EnumRequestType type, EnumWarpInterface iface){
        this.source = source;
        this.type = type;
        this.iface = iface;
    }

    public void Burn(){
        burned = true;
    }

    public abstract void touch(TileWarper target, WarpInterface iface);

    public abstract Packet GetBlank();
}