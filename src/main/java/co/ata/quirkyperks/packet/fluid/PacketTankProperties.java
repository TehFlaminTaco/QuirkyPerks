package co.ata.quirkyperks.packet.fluid;

import java.util.ArrayList;
import java.util.List;

import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import scala.actors.threadpool.Arrays;

public class PacketTankProperties extends PacketFluid {

    public ArrayList<IFluidTankProperties> props = new ArrayList<IFluidTankProperties>();

    public PacketTankProperties(TileWarper source, EnumFacing facing){
        super(source, EnumRequestType.Properties, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void touchHandler(TileWarper target, IFluidHandler handler, EnumFacing f) {
        props.addAll((List<IFluidTankProperties>)Arrays.asList(handler.getTankProperties()));
    }

	@Override
	public Packet GetBlank() {
		return new PacketTankProperties(source, facing);
	}

}