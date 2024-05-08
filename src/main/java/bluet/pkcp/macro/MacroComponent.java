package bluet.pkcp.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class MacroComponent {
    private String macro;
    public MacroComponent (String macro) {
        this.macro = macro;
    }
    public String macro () {
        return this.macro;
    }
    public static final Codec <MacroComponent> CODEC = RecordCodecBuilder.create (
        (instance) -> instance.group (
            Codec.STRING.fieldOf ("macro") .orElse ("") .forGetter (MacroComponent::macro)
        ) .apply (instance, MacroComponent::new)
    );
}
