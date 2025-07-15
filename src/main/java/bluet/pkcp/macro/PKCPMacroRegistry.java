package bluet.pkcp.macro;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

import bluet.pkcp.macro.PlayerMacro.TickMacro;

public class PKCPMacroRegistry implements Function <String, MacroEntry> {
    public static final PKCPMacroRegistry instance;
    private final Map <String, Function <String, MacroEntry>> map = new HashMap <> ();
    @Nullable
    public MacroEntry apply (String macro) {
        macro = macro.strip ();
        String cl = macro.split (" ", 2) [0];
        var fun = this.map.get (cl);
        if (fun == null) return new SimpleMacro ("~ " + macro);
        MacroEntry cmd = fun.apply (macro);
        return cmd;
    }
    public void register (String name, Function <String, MacroEntry> function) {
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
    public static class SimpleMacro implements MacroEntry {
        public final float yaw, pitch;
        public final String keys;
        public final int len;
        public SimpleMacro (String cmd) {
            String [] ss = cmd.split (" ", 5);
            String [] tmp = ss; ss = new String [5];
            int i;
            for (i=0; i<tmp.length; i++) ss [i] = tmp [i];
            if (tmp.length < 5) ss [4] = "1";
            if (tmp.length < 4) ss [3] = "";
            if (tmp.length < 3) ss [2] = "";
            if (tmp.length < 2) ss [1] = "";
            this.keys = ss [1];
            this.yaw = ofloat (ss [2]);
            this.pitch = ofloat (ss [3]);
            this.len = oint (ss [4]);
        }
        public int length () {
            return this.len;
        }
        public TickMacro perform (int tick) {
            return new TickMacro (
                this.keys.contains("W"),
                this.keys.contains("A"),
                this.keys.contains("S"),
                this.keys.contains("D"),
                this.keys.contains("J"),
                this.keys.contains("C"),
                this.keys.contains("s"),
                this.keys.contains("L"),
                this.keys.contains("R"),
                this.yaw, this.pitch
            );
        }
    }
    public static class Jam implements MacroEntry {
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
    public static class E implements MacroEntry {
        public final int len;
        public final boolean display;
        public E (String cmd) {
            var s = cmd.split (" ", 3);
            if (s.length == 1) {
                this.len = 1;
                this.display = false;
            }
            else if (s.length == 2) {
                if (s [1] .charAt (0) == 'T') {
                    this.len = 1;
                    this.display = true;
                } else {
                    this.len = oint (s [1]);
                    this.display = false;
                }
            }
            else {
                this.len = oint (s [1]);
                this.display = s [2] .charAt (0) == 'T';
            }
        }
        public int length () {
            return this.len;
        }
        public TickMacro perform (int tick) {
            return new TickMacro (false, false, false, false, false, false, false, false, false, 0f, 0f);
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
        instance.register ("E", E::new);
    }
}
