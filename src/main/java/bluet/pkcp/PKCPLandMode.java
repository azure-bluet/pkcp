package bluet.pkcp;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public enum PKCPLandMode {
    BOTH, Z_ONLY, X_ONLY;
    public static PKCPLandMode fromint (int x) {
        switch (x) {
            case 1: return Z_ONLY;
            case 2: return X_ONLY;
            default: return BOTH;
        }
    }
    public int toint () {
        switch (this) {
            case BOTH: return 0;
            case Z_ONLY: return 1;
            default: return 2;
        }
    }
    public PKCPLandMode next () {
        switch (this) {
            case X_ONLY: return PKCPLandMode.BOTH;
            case Z_ONLY: return PKCPLandMode.X_ONLY;
            default: return PKCPLandMode.Z_ONLY;
        }
    }
    public String trans () {
        String base = "pkcp.mode.";
        switch (this) {
            case X_ONLY: base += "x"; break;
            case Z_ONLY: base += "z"; break;
            default: base += "both"; break;
        }
        return base;
    }
    public static final MapCodec <PKCPLandMode> CODEC = RecordCodecBuilder.mapCodec (
        (instance) -> instance.group (
            Codec.INT.fieldOf ("landmode") .forGetter (PKCPLandMode::toint)
        ) .apply (instance, PKCPLandMode::fromint)
    );
}
