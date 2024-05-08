package bluet.pkcp;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class PKSetLB extends Item {
    public PKSetLB () {
        super (new Item.Properties () .stacksTo (1) .fireResistant ());
    }
    @Override
    public InteractionResult useOn (UseOnContext ctx) {
        Level level = ctx.getLevel ();
        if (level == null || level.isClientSide ()) return InteractionResult.PASS;
        BlockPos pos = ctx.getClickedPos ();
        Player player = ctx.getPlayer ();
        if (player == null) return InteractionResult.FAIL;
        ItemStack off = player.getOffhandItem ();
        PKCPComponent com = off.get (PKCPComponent.pkcp.get ());
        if (com == null) return InteractionResult.FAIL;
        com.setlb (pos);
        player.displayClientMessage (Component.translatable ("pkcp.msg.success_setlb"), true);
        return InteractionResult.SUCCESS;
    }
}
