package bluet.pkcp.macro;

import java.util.ArrayList;
import java.util.List;

public class Macros {
    public final List <MacroEntry> cmds;
    public Macros (String all) {
        List <MacroEntry> li = new ArrayList <> ();
        var c = all.split (";");
        int i;
        for (i=0; i<c.length; i++) {
            var cmd = PKCPMacroRegistry.instance.apply (c [i]);
            if (cmd != null) li.add (cmd);
        }
        this.cmds = List.copyOf (li);
    }
    public Macros (List <String> all) {
        List <MacroEntry> li = new ArrayList <> ();
        for (String single : all) {
            var cmd = PKCPMacroRegistry.instance.apply (single);
            if (cmd != null) li.add (cmd);
        }
        this.cmds = List.copyOf (li);
    }
    public static Macros shared = null;
    public static boolean executing = false;
    public static List <PlayerMacro.TickMacro> ticks = null;
    public static void cache () {
        ticks = new ArrayList <> ();
        for (var entry : shared.cmds) {
            int i; for (i=0; i<entry.length (); i++)
                ticks.add (entry.perform (i));
        }
    }
    public static int tick;
    public static void begin () {
        if (shared == null) return;
        tick = 0; executing = true;
    }
}
