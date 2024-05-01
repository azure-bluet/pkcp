package bluet.pkcp.macro;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MacroItem extends Item {
    private Macros macros;
    public MacroItem () {
        super (new Item.Properties () .stacksTo (1) .fireResistant ());
        this.macros = null;
    }
    @Override
    public InteractionResultHolder <ItemStack> use (Level level, Player pl, InteractionHand hand) {
        if (pl instanceof LocalPlayer player) {
            ItemStack stack = player.getItemInHand (hand);
            var tag = stack.getTag ();
            if (tag == null) return InteractionResultHolder.fail (stack);
            String str = tag.getString ("macro");
            this.macros = new Macros (str);
            return InteractionResultHolder.success (stack);
        } else return InteractionResultHolder.pass (pl.getItemInHand (hand));
    }
    @Override
    public void inventoryTick (ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof LocalPlayer player) {
            if (this.macros == null) return;
            if (this.macros.exec_tick (player)) {
                this.macros = null;
                PlayerMacro.clear (player);
            }
        }
    }
}
