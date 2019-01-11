package co.ata.quirkyperks;

import co.ata.quirkyperks.tiles.TileWarper;

public class RequestInfo {
    public TileWarper source;
    public EnumRequestType type;

    public RequestInfo(TileWarper source, EnumRequestType type){
        this.source = source;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RequestInfo){
            RequestInfo ri = (RequestInfo)obj;
            return ri.source == source && ri.type == type;
        }
        return super.equals(obj);
    }
}