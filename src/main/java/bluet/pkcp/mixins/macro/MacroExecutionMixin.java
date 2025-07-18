package bluet.pkcp.mixins.macro;

import bluet.pkcp.macro.Macros;
import bluet.pkcp.macro.PlayerMacro;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin (LocalPlayer.class)
public abstract class MacroExecutionMixin {
    @Inject (method = "aiStep", at = @At ("HEAD"))
    private void macro (CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer)(Object)this;
        if (!Macros.executing) return;
        if (Macros.tick >= Macros.ticks.size ()) {
            PlayerMacro.clear (player);
            Macros.executing = false;
            return;
        }
        PlayerMacro.perform_tick (player, Macros.ticks.get (Macros.tick));
        Macros.tick ++;
    }
    @Inject (method = "getViewXRot", at = @At ("HEAD"), cancellable = true)
    private void getViewXRot (float partial, CallbackInfoReturnable <Float> cir) {
        if (!Macros.executing || Macros.tick >= Macros.ticks.size ()) return;
        float d = Macros.ticks.get (Macros.tick) .dpitch ();
        LocalPlayer player = (LocalPlayer)(Object)this;
        cir.setReturnValue (player.getXRot () + linear (d, partial));
    }
    @Inject (method = "getViewYRot", at = @At ("HEAD"), cancellable = true)
    private void getViewYRot (float partial, CallbackInfoReturnable <Float> cir) {
        if (!Macros.executing || Macros.tick >= Macros.ticks.size ()) return;
        float d = Macros.ticks.get (Macros.tick) .dyaw ();
        LocalPlayer player = (LocalPlayer)(Object)this;
        cir.setReturnValue (player.getYRot () + linear (d, partial));
    }
    // for the next two functions, `a` `b` and `c` refers to previous tick, current tick and next tick
    // This is pretty good on neos, but really strange on 45s (-3 preturn visually)
    private float cubical (float a, float b, float c, float partial) {
        float p = (a - 2 * b + c) / 6, q = (b - a) / 2 - 3 * p, r = a - p - q;
        partial += 1;
        float square = partial * partial, cube = square * partial;
        return p * cube + q * square + r * partial - a;
    }
    // wtf is this
    private float cubical_alt (float a, float b, float c, float partial) {
        float p = a - 2 * b + c, q = -2 * a + 3 * b - c, r = a;
        partial += 1;
        float square = partial * partial, cube = square * partial;
        return p * cube + q * square + r * partial;
    }
    // best solution currently
    private float linear (float d, float partial) {
        return d * partial;
    }
}
