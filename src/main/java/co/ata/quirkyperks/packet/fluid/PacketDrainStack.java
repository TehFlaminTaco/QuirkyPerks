package co.ata.quirkyperks.packet.fluid;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.items.IWarpCardBase;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class PacketDrainStack extends PacketFluid {

    public FluidStack stack;
    public FluidStack drained;
    boolean doDrain = true;
    public PacketDrainStack(TileWarper source, EnumFacing facing, FluidStack stack, boolean doDrain){
        super(source, EnumRequestType.DrainStack, facing);
        this.stack = stack.copy();
        this.drained = stack.copy();
        this.drained.amount = 0;
        this.doDrain = doDrain;
    }

    @Override
    public void touchHandler(TileWarper target, WarpInterface iface, IFluidHandler handler, EnumFacing f) {
        if(stack.amount <= 0)
            return;
        
        if(iface.canInterface(IWarpCardBase.getFiltersFromItem(target.card()), f, EnumInterfaceDirection.In, stack)){
            FluidStack pulled = handler.drain(stack, doDrain);
            stack.amount = Math.max(0, stack.amount - pulled.amount); // Just incase.
            drained.amount += pulled.amount;
        }
            
    }

	@Override
	public Packet GetBlank() {
		return new PacketFill(source, facing, stack, false);
	}

}