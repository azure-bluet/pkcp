package bluet.pkcp.macro;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn (Dist.CLIENT)
public class PlayerMacro {
    public static record TickMacro (boolean W, boolean A, boolean S, boolean D, boolean JUMP, boolean SPRINT, boolean SNEAK, boolean LMB, boolean RMB, float dyaw, float dpitch) {}
    public static float delay_yaw = 0f, delay_pitch = 0f;
    public static void rot (LocalPlayer player, float yaw, float pitch) {
        player.setYRot (player.getYRot () + delay_yaw);
        player.setXRot (player.getXRot () + delay_pitch);
        delay_yaw = yaw;
        delay_pitch = pitch;
    }
    public static void perform_tick (LocalPlayer player, TickMacro macro) {
        Minecraft mc = Minecraft.getInstance ();
        Options options = mc.options;
        options.keyUp.setDown (macro.W);
        options.keyLeft.setDown (macro.A);
        options.keyDown.setDown (macro.S);
        options.keyRight.setDown (macro.D);
        options.keyJump.setDown (macro.JUMP);
        options.keySprint.setDown (macro.SPRINT);
        options.keyShift.setDown (macro.SNEAK);
        options.keyAttack.setDown (macro.LMB);
        options.keyUse.setDown (macro.RMB);
        rot (player, macro.dyaw, macro.dpitch);
        player.displayClientMessage (Component.translatable ("pkcp.msg.macro_running") .withColor (0x00ffff00), true);
    }
    public static void clear (LocalPlayer player) {
        KeyMapping.releaseAll ();
        rot (player, 0f, 0f);
        player.displayClientMessage (Component.translatable ("pkcp.msg.macro_end") .withColor (0x0000ff00), true);
    }
}
