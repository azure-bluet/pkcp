package bluet.pkcp.macro;

import bluet.pkcp.PKCPComponent;
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
    private ItemStack last_stack;
    public static MacroItem instance;
    public MacroItem () {
        super (new Item.Properties () .stacksTo (1) .fireResistant ());
        this.macros = null;
        this.last_stack = null;
        instance = this;
    }
    @Override
    public InteractionResultHolder <ItemStack> use (Level level, Player pl, InteractionHand hand) {
        if (pl instanceof LocalPlayer player) {
            ItemStack stack = player.getItemInHand (hand);
            MacroComponent com = stack.get (PKCPComponent.macro.get ());
            if (com == null) return InteractionResultHolder.fail (stack);
            String str = com.macro ();
            Macros m = new Macros (str);
            if (m.cmds.isEmpty ()) return InteractionResultHolder.fail (stack);
            this.macros = m;
            this.last_stack = stack;
            return InteractionResultHolder.success (stack);
        } else return InteractionResultHolder.pass (pl.getItemInHand (hand));
    }
    @Override
    public void inventoryTick (ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof LocalPlayer player && stack == this.last_stack) {
            if (this.macros == null) return;
            if (this.macros.exec_tick (player)) {
                this.macros = null;
                PlayerMacro.clear (player);
            }
        }
    }
    public boolean running () {
        return this.macros != null;
    }
}
