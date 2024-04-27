package bluet.pkcp;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class PKSetLB extends Item {
    public PKSetLB () {
        super (new Item.Properties () .stacksTo (1) .fireResistant ());
    }
    @Override
    public InteractionResult useOn (UseOnContext ctx) {
        BlockPos pos = ctx.getClickedPos ();
        CompoundTag tag = new CompoundTag ();
        tag.putInt ("x", pos.getX ());
        tag.putInt ("y", pos.getY ());
        tag.putInt ("z", pos.getZ ());
        Player player = ctx.getPlayer ();
        if (player == null) return InteractionResult.FAIL;
        ItemStack off = player.getOffhandItem ();
        if (! off.is (PKCPItem.pkcp.get ())) return InteractionResult.FAIL;
        off.getOrCreateTag () .put ("block", tag);
        player.displayClientMessage (Component.translatable ("pkcp.msg.success_setlb"), true);
        return InteractionResult.SUCCESS;
    }
}
