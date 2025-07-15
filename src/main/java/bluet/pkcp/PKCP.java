package bluet.pkcp;

import com.mojang.brigadier.arguments.StringArgumentType;

import bluet.pkcp.macro.MacroComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod (PKCP.MOD_ID)
@Mod.EventBusSubscriber (bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PKCP {
    public static final String MOD_ID = "pkcp";
    public PKCP (FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus ();
        PKCPItem.registry.register (bus);
        PKCPTab.registry.register (bus);
        PKCPComponent.registry.register (bus);
    }
    public static int setmode (PKCPLandMode mode) {
        PKCPComponent.shared_mode = mode;
        return 1;
    }
    @SubscribeEvent
    public static void regcmd (RegisterCommandsEvent event) {
        event.getDispatcher () .register (
            Commands.literal ("macro") .then (
                Commands.argument ("macro", StringArgumentType.greedyString ()) .executes (ctx -> {
                    Entity entity = ctx.getSource () .getEntity ();
                    if (entity instanceof Player player) {
                        ItemStack stack = new ItemStack (PKCPItem.pkmacro.get ());
                        MacroComponent component = new MacroComponent (StringArgumentType.getString (ctx, "macro"));
                        stack.set (PKCPComponent.macro.get (), component);
                        player.getInventory () .setItem (4, stack);
                    }
                    return 1;
                })
            )
        );
    }
    @SubscribeEvent
    public static void regcmd (RegisterClientCommandsEvent event) {
        event.getDispatcher () .register (
            Commands.literal ("pkcp") .then (
                Commands.literal ("setmode") .then (
                    Commands.literal ("x") .executes (ctx -> setmode (PKCPLandMode.X_ONLY))
                ) .then (
                    Commands.literal ("z") .executes (ctx -> setmode (PKCPLandMode.Z_ONLY))
                ) .then (
                    Commands.literal ("both") .executes (ctx -> setmode (PKCPLandMode.BOTH))
                )
            ) .then (
                Commands.literal ("setlb") .executes (ctx -> {
                    Minecraft mc = Minecraft.getInstance();
                    BlockPos pos = mc.player.blockPosition ();
                    PKCPComponent.shared_pos = pos.below ();
                    return 1;
                })
            )
        );
    }
    @SubscribeEvent
    public static void tickplayer (TickEvent.PlayerTickEvent.Post event) {
        if (event.player.isLocalPlayer ()) {
            PKCPItem.tick (event.player.level (), event.player, event.player.getMainHandItem ());
        }
    }
}
