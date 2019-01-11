package co.ata.quirkyperks.packet.fluid;

import java.util.List;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.items.ItemWarpCard;
import co.ata.quirkyperks.packet.Packet;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class PacketFill extends PacketFluid {

    public FluidStack stack;
    boolean doFill = true;
    public int filled = 0;
    public PacketFill(TileWarper source, EnumFacing facing, FluidStack stack, boolean doFill){
        super(source, EnumRequestType.Fill, facing);
        this.stack = stack.copy();
        this.doFill = doFill;
    }

    @Override
    public void touchHandler(TileWarper target, IFluidHandler handler, EnumFacing f) {
        if(stack.amount <= 0)
            return;
        List<WarpInterface> interfaces = ItemWarpCard.getInterfaces(target.card(), EnumWarpInterface.Fluid);
        //ItemStack pulledItem = handler.extractItem(slot, 1, true);
        
        if(WarpInterface.canInterface(interfaces, ItemWarpCard.getFilters(target.card()), f, EnumInterfaceDirection.Out, stack)){
            int pulled = handler.fill(stack, doFill);
            stack.amount = Math.max(0, stack.amount - pulled); // Just incase.
            filled += pulled;
        }
            
    }

	@Override
	public Packet GetBlank() {
		return new PacketFill(source, facing, stack, false);
	}

}