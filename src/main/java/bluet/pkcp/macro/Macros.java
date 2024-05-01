package bluet.pkcp.macro;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.player.LocalPlayer;

public class Macros {
    public final List <MacroCommand> cmds;
    public int exec, tick;
    public Macros (String all) {
        this.exec = this.tick = 0;
        List <MacroCommand> li = new ArrayList <> ();
        var c = all.split (";");
        int i;
        for (i=0; i<c.length; i++) {
            var cmd = PKCPMacroRegistry.instance.apply (c [i]);
            if (cmd != null) li.add (cmd);
        }
        this.cmds = List.copyOf (li);
    }
    public boolean exec_tick (LocalPlayer player) {
        PlayerMacro.perform_tick (player, this.cmds.get (this.exec) .perform (this.tick));
        this.tick ++;
        if (this.tick == this.cmds.get (this.exec) .length ()) {
            this.exec ++;
            this.tick = 0;
        }
        return this.exec >= this.cmds.size ();
    }
}
