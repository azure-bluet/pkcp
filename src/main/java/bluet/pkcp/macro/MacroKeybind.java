package bluet.pkcp.macro;

import com.mojang.blaze3d.platform.InputConstants;
import cpw.mods.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber (bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MacroKeybind {
    public static final Lazy <KeyMapping> KEY_RUN_MACRO = Lazy.of (() -> new KeyMapping (
        "pkcp.key.runmacro",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_O,
        "key.categories.misc"
    ));
    @SubscribeEvent
    public static void register (RegisterKeyMappingsEvent event) {
        event.register (KEY_RUN_MACRO.get ());
    }
    private static boolean previous = false;
    @Mod.EventBusSubscriber (bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    private static class $ {
        @SubscribeEvent
        public static void tick (TickEvent.ClientTickEvent.Pre event) {
            if (KEY_RUN_MACRO.get () .isDown () && !previous) Macros.begin ();
            previous = KEY_RUN_MACRO.get () .isDown ();
        }
    }
}
