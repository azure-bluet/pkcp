package bluet.pkcp.mixins.macro;

import bluet.pkcp.macro.Macros;
import bluet.pkcp.macro.PlayerMacro;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin (LocalPlayer.class)
public abstract class MacroExecutionMixin {
    @Inject (method = "aiStep", at = @At ("HEAD"))
    private void macro (CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer)(Object)this;
        if (Macros.shared == null || !Macros.executing) return;
        if (Macros.shared.exec >= Macros.shared.cmds.size ()) {
            PlayerMacro.clear (player);
            Macros.executing = false;
            return;
        }
        PlayerMacro.perform_tick (player, Macros.shared.cmds.get (Macros.shared.exec) .perform (Macros.shared.tick));
        Macros.shared.tick ++;
        if (Macros.shared.tick == Macros.shared.cmds.get (Macros.shared.exec) .length ()) {
            Macros.shared.exec ++;
            Macros.shared.tick = 0;
        }
    }
}
