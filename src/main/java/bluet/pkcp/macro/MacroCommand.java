package bluet.pkcp.macro;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber (bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MacroCommand {
    private static String last = null;
    public static File cfg (String name) {
        var gd = Minecraft.getInstance () .gameDirectory;
        var md = gd.getPath () + File.separator + "macros";
        if (! new File (md) .exists ()) {
            new File (md) .mkdir ();
        }
        return new File (md + File.separator + name + ".m0");
    }
    public static List <String> items () {
        var gd = Minecraft.getInstance () .gameDirectory;
        var md = gd.getPath () + File.separator + "macros";
        if (! new File (md) .exists ()) {
            return List.of ();
        }
        var fs = new File (md) .listFiles ();
        List <String> li = new ArrayList <String> ();
        for (var f : fs) li.add (f.getName ());
        return List.copyOf (li);
    }
    public static String get (String name) {
        try {
            var is = new FileInputStream (cfg (name));
            var res = new String (is.readAllBytes ());
            is.close (); return res;
        } catch (IOException e) {
            return "";
        }
    }
    public static boolean save (String name, String macro) {
        try {
            var os = new FileOutputStream (cfg (name));
            os.write (macro.getBytes ());
            os.close ();
            return false;
        } catch (IOException e) {
            return true;
        }
    }
    public static void msg (CommandContext <CommandSourceStack> ctx, Component component) {
        if (ctx.getSource () .getEntity () instanceof Player player) {
            player.displayClientMessage (component, true);
        }
    }
    public static void msg (CommandContext <CommandSourceStack> ctx, String key, int color) {
        msg (ctx, Component.translatable (key) .withColor (color));
    }
    public static void text (CommandContext <CommandSourceStack> ctx, Component component) {
        if (ctx.getSource () .getEntity () instanceof Player player) {
            player.sendSystemMessage (component);
        }
    }
    @SubscribeEvent
    public static void reg (RegisterClientCommandsEvent event) {
        event.getDispatcher () .register (
            Commands.literal ("climacro") .then (
                Commands.literal ("edit") .then (
                    Commands.argument ("macro", StringArgumentType.greedyString ()) .executes (
                        ctx -> {
                            last = StringArgumentType.getString (ctx, "macro");
                            Macros m = new Macros (last);
                            if (m.cmds.size () > 0) {
                                Macros.shared = m;
                                msg (ctx, "pkcp.cmd.yesset", 0x00ff00);
                                return 1;
                            } else {
                                msg (ctx, "pkcp.cmd.noset", 0xff0000);
                                return 0;
                            }
                        }
                    )
                )
            ) .then (
                Commands.literal ("list") .executes (ctx -> {
                    var ms = items ();
                    text (ctx, Component.translatable ("pkcp.cmd.list"));
                    for (var str : ms) text (ctx, Component.literal (str));
                    return 1;
                })
            ) .then (
                Commands.literal ("save") .then (
                    Commands.argument ("name", StringArgumentType.greedyString ()) .executes (ctx -> {
                        if (save (StringArgumentType.getString (ctx, "name"), last)) {
                            msg (ctx, "pkcp.cmd.nosave", 0xff0000);
                            return 0;
                        } else {
                            msg (ctx, "pkcp.cmd.yessave", 0x00ff00);
                            return 1;
                        }
                    })
                )
            ) .then (
                Commands.literal ("load") .then (
                    Commands.argument ("name", StringArgumentType.greedyString ()) .executes (ctx -> {
                        last = get (StringArgumentType.getString (ctx, "name"));
                        Macros m = new Macros (last);
                        if (m.cmds.size () > 0) {
                            Macros.shared = m;
                            msg (ctx, "pkcp.cmd.yesload", 0x00ff00);
                            return 1;
                        } else {
                            msg (ctx, "pkcp.cmd.noload", 0xff0000);
                            return 0;
                        }
                    })
                )
            )
        );
    }
}
