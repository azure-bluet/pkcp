package bluet.pkcp.macro;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

import bluet.pkcp.macro.PlayerMacro.TickMacro;

public class PKCPMacroRegistry implements Function <String, MacroCommand> {
    public static final PKCPMacroRegistry instance;
    private final Map <String, Function <String, MacroCommand>> map = new HashMap <> ();
    @Nullable
    public MacroCommand apply (String macro) {
        macro = macro.strip ();
        String cl = macro.split (" ", 2) [0];
        var fun = this.map.get (cl);
        if (fun == null) return new SimpleMacro ("~ " + macro);
        MacroCommand cmd = fun.apply (macro);
        return cmd;
    }
    public void register (String name, Function <String, MacroCommand> function) {
        this.map.put (name, function);
    }
    public static float ofloat (String s) {
        try {
            return Float.valueOf (s);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }
    public static int oint (String s) {
        try {
            return Integer.valueOf (s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public static class SimpleMacro implements MacroCommand {
        public final float yaw, pitch;
        public final String keys;
        public SimpleMacro (String cmd) {
            String [] ss = cmd.split (" ", 4);
            String [] tmp = ss; ss = new String [4];
            int i;
            for (i=0; i<tmp.length; i++) ss [i] = tmp [i];
            if (tmp.length < 4) ss [3] = "";
            if (tmp.length < 3) ss [2] = "";
            if (tmp.length < 2) ss [1] = "";
            this.keys = ss [1];
            this.yaw = ofloat (ss [2]);
            this.pitch = ofloat (ss [3]);
        }
        public int length () {
            return 1;
        }
        public TickMacro perform (int tick) {
            return new TickMacro (
                this.keys.indexOf ("W") != -1,
                this.keys.indexOf ("A") != -1,
                this.keys.indexOf ("S") != -1,
                this.keys.indexOf ("D") != -1,
                this.keys.indexOf ("J") != -1,
                this.keys.indexOf ("C") != -1,
                this.keys.indexOf ("s") != -1,
                this.keys.indexOf ("L") != -1,
                this.keys.indexOf ("R") != -1,
                this.yaw, this.pitch
            );
        }
    }
    public static class Jam implements MacroCommand {
        public final int tier;
        public final TickMacro j, aj, air;
        public int length () {
            return this.tier;
        }
        public TickMacro perform (int tick) {
            if (tick == 0) return this.j;
            if (tick == 1) return this.aj;
            return this.air;
        }
        public Jam (TickMacro jamming, TickMacro start, TickMacro air, String macro) {
            this.j = jamming;
            this.aj = start;
            this.air = air;
            String [] ms = macro.split (" ");
            if (ms.length == 1) this.tier = 12;
            else this.tier = oint (ms [1]);
        }
    }
    static {
        instance = new PKCPMacroRegistry ();
        instance.register ("simple", SimpleMacro::new);

        // Jam, 45 Jam and Double 45 jam
        TickMacro onj = new TickMacro (true, false, false, false, true, true, false, false, false, 0f, 0f);
        TickMacro dub = new TickMacro (true, false, false, false, true, true, false, false, false, -45f, 0f);
        TickMacro air = new TickMacro (true, false, false, false, false, true, false, false, false, 0f, 0f);
        TickMacro sfe = new TickMacro (true, true, false, false, false, true, false, false, false, 45f, 0f);
        TickMacro sfa = new TickMacro (true, true, false, false, false, true, false, false, false, 0f, 0f);
        instance.register ("jam", (s) -> new Jam (onj, air, air, s));
        instance.register ("jam45", (s) -> new Jam (onj, sfe, sfa, s));
        instance.register ("dub45", (s) -> new Jam (dub, sfe, sfa, s));
    }
}
