package co.ata.quirkyperks.packet;

import java.util.List;

import co.ata.quirkyperks.EnumInterfaceDirection;
import co.ata.quirkyperks.EnumRequestType;
import co.ata.quirkyperks.EnumWarpInterface;
import co.ata.quirkyperks.WarpInterface;
import co.ata.quirkyperks.items.ItemWarpCard;
import co.ata.quirkyperks.tiles.TileWarper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class PacketActivate extends Packet {
    
    EntityPlayer playerIn;
    EnumHand hand;
    EnumFacing facing;
    float hitX;
    float hitY;
    float hitZ;
    public boolean worked = false;

    public PacketActivate(TileWarper source, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        super(source, EnumRequestType.Activate);
        this.playerIn = playerIn;
        this.hand = hand;
        this.facing = facing;
        this.hitX = hitX;
        this.hitY = hitY;
        this.hitZ = hitZ;
    }

    @Override
    public void touch(TileWarper target) {
        List<WarpInterface> interfaces = ItemWarpCard.getInterfaces(target.card(), EnumWarpInterface.Button);
        for(EnumFacing f : EnumFacing.values()){
            if(!WarpInterface.canInterface(interfaces, ItemWarpCard.getFilters(target.card()), f, EnumInterfaceDirection.Out, playerIn.getHeldItemMainhand()))
                continue;
            BlockPos tPos = target.getPos().offset(f);
            IBlockState state = target.getWorld().getBlockState(tPos);
            worked |= state.getBlock().onBlockActivated(target.getWorld(), tPos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
    }

    @Override
    public Packet GetBlank() {
		return new PacketActivate(source, playerIn, hand, facing, hitX, hitY, hitZ);
	}

}