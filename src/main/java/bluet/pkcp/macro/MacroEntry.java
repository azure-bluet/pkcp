package bluet.pkcp.macro;

import bluet.pkcp.macro.PlayerMacro.TickMacro;

public interface MacroEntry {
    public int length ();
    public TickMacro perform (int tick);
}
