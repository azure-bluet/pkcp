package bluet.pkcp;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PKSetReturn extends Item {
    public PKSetReturn () {
        super (new Item.Properties () .stacksTo (1) .fireResistant ());
    }
    @Override
    public InteractionResultHolder <ItemStack> use (Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand (hand);
        CompoundTag tag = new CompoundTag ();
        ItemStack off = player.getOffhandItem ();
        if (! off.is (PKCPItem.pkcp.get ())) return InteractionResultHolder.fail (stack);
        tag.putDouble ("x", player.getX ());
        tag.putDouble ("y", player.getY ());
        tag.putDouble ("z", player.getZ ());
        tag.putFloat ("xrot", player.getXRot ());
        tag.putFloat ("yrot", player.getYRot ());
        off.getOrCreateTag () .put ("pos", tag);
        player.displayClientMessage (Component.translatable ("pkcp.msg.success_setreturn"), true);
        return InteractionResultHolder.success (stack);
    }
}
