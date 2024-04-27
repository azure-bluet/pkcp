package bluet.pkcp;

import net.minecraft.nbt.CompoundTag;
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
        ItemStack off = player.getOffhandItem (), stack = player.getItemInHand (hand);
        if (! off.is (PKCPItem.pkcp.get ())) return InteractionResultHolder.fail (stack);
        CompoundTag tag = off.getOrCreateTag ();
        int previous = tag.getInt ("mode");
        previous ++;
        previous %= 3;
        tag.putInt ("mode", previous);
        String trans = "pkcp.mode.";
        switch (previous) {
            case 1:
            trans += "z";
            break;
            case 2:
            trans += "x";
            break;
            default:
            trans += "both";
            break;
        }
        player.displayClientMessage (Component.translatable ("pkcp.msg.lbmode") .append (Component.translatable (trans)), true);
        return InteractionResultHolder.success (stack);
    }
}
