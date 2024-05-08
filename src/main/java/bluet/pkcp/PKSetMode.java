package bluet.pkcp;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PKSetMode extends Item {
    public PKSetMode () {
        super (new Item.Properties () .stacksTo (1) .fireResistant ());
    }
    @Override
    public InteractionResultHolder <ItemStack> use (Level level, Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND) return InteractionResultHolder.pass (player.getItemInHand (hand));
        if (level.isClientSide) return InteractionResultHolder.pass (player.getItemInHand (hand));
        ItemStack off = player.getOffhandItem (), stack = player.getItemInHand (hand);
        if (! off.is (PKCPItem.pkcp.get ())) return InteractionResultHolder.fail (stack);
        PKCPComponent com = off.get (PKCPComponent.pkcp.get ());
        if (com == null) return InteractionResultHolder.fail (stack);
        com.setmode (com.mode () .next ());
        String trans = com.mode () .trans ();
        player.displayClientMessage (Component.translatable ("pkcp.msg.lbmode") .append (Component.translatable (trans)), true);
        return InteractionResultHolder.success (stack);
    }
}
