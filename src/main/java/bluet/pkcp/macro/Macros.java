package bluet.pkcp.macro;

import java.util.ArrayList;
import java.util.List;

public class Macros {
    public final List <MacroEntry> cmds;
    public int exec, tick;
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
    public static void begin () {
        if (shared == null) return;
        shared.exec = shared.tick = 0;
        executing = true;
    }
}
