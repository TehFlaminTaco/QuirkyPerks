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

public class PacketDrainAmount extends PacketFluid {

    public int amount;
    public FluidStack drainStack;
    public FluidStack drained;
    boolean doDrain = true;
    public PacketDrainAmount(TileWarper source, EnumFacing facing, int amount, boolean doDrain){
        super(source, EnumRequestType.DrainAmount, facing);
        this.amount = amount;
        this.drained = null;
        this.drainStack = null;
        this.doDrain = doDrain;
    }

    @Override
    public void touchHandler(TileWarper target, WarpInterface iface, IFluidHandler handler, EnumFacing f) {
        
        if(drainStack == null){
            FluidStack pulled = handler.drain(amount, false);
            // This is a really strange case in which I check if the source can push out of it remotely. **THIS WILL CAUSE PROBLEMS**
            if(pulled != null && iface.canInterface(IWarpCardBase.getFiltersFromItem(target.card()), f, EnumInterfaceDirection.In, pulled)
                              && iface.canInterface(IWarpCardBase.getFiltersFromItem(source.card()), facing, EnumInterfaceDirection.Out, pulled)){
                pulled = handler.drain(amount, true);
                drainStack = pulled;
                drained = drainStack.copy();
                drained.amount = amount - drainStack.amount;
            }
        }else if(iface.canInterface(IWarpCardBase.getFiltersFromItem(target.card()), f, EnumInterfaceDirection.In, drainStack)){
            FluidStack pulled = handler.drain(drainStack, doDrain);
            drainStack.amount = Math.max(0, drainStack.amount - pulled.amount); // Just incase.
            drained.amount += pulled.amount;
        }
            
    }

	@Override
	public Packet GetBlank() {
		return new PacketDrainAmount(source, facing, 0, false);
	}

}