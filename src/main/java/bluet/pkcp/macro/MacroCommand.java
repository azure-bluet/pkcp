package bluet.pkcp.macro;

import bluet.pkcp.macro.PlayerMacro.TickMacro;

public interface MacroCommand {
    public int length ();
    public TickMacro perform (int tick);
}
