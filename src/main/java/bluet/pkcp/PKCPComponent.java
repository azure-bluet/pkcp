package bluet.pkcp;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import bluet.pkcp.macro.MacroComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PKCPComponent {
    public static final DeferredRegister <DataComponentType <?>> registry = DeferredRegister.create (Registries.DATA_COMPONENT_TYPE, PKCP.MOD_ID);
    private BlockPos landing;
    private double retx, rety, retz;
    private float rotx, roty;
    private PKCPLandMode mode;
    public BlockPos landing () {
        return this.landing;
    }
    public void setlb (BlockPos pos) {
        this.landing = pos;
    }
    public void returning (Player player) {
        if (player instanceof LocalPlayer) {
            player.setXRot (this.rotx);
            player.setYRot (this.roty);
        } else player.teleportTo (this.retx, this.rety, this.retz);
    }
    public void setrt (Player player) {
        this.retx = player.getX ();
        this.rety = player.getY ();
        this.retz = player.getZ ();
        this.rotx = player.getXRot ();
        this.roty = player.getYRot ();
    }
    public PKCPLandMode mode () {
        return this.mode;
    }
    public void setmode (PKCPLandMode mode) {
        this.mode = mode;
    }
    public double retx () {
        return this.retx;
    }
    public double rety () {
        return this.rety;
    }
    public double retz () {
        return this.retz;
    }
    public float rotx () {
        return this.rotx;
    }
    public float roty () {
        return this.roty;
    }
    public PKCPComponent (BlockPos landing, double x, double y, double z, float xr, float yr, PKCPLandMode mode) {
        this.landing = landing;
        this.retx = x;
        this.rety = y;
        this.retz = z;
        this.rotx = xr;
        this.roty = yr;
        this.mode = mode;
    }
    public static final Codec <PKCPComponent> CODEC = RecordCodecBuilder.create (
        (instance) -> instance.group (
            BlockPos.CODEC.fieldOf ("landing") .orElse (BlockPos.ZERO) .forGetter (PKCPComponent::landing),
            Codec.DOUBLE.fieldOf ("x") .orElse (0d) .forGetter (PKCPComponent::retx),
            Codec.DOUBLE.fieldOf ("y") .orElse (0d) .forGetter (PKCPComponent::rety),
            Codec.DOUBLE.fieldOf ("z") .orElse (0d) .forGetter (PKCPComponent::retz),
            Codec.FLOAT.fieldOf ("xr") .orElse (0f) .forGetter (PKCPComponent::rotx),
            Codec.FLOAT.fieldOf ("yr") .orElse (0f) .forGetter (PKCPComponent::roty),
            PKCPLandMode.CODEC.fieldOf ("mode") .orElse (PKCPLandMode.BOTH) .forGetter (PKCPComponent::mode)
        ) .apply (instance, PKCPComponent::new)
    );
    public static <T> Supplier <DataComponentType <T>> reg (Codec <T> codec) {
        return () -> DataComponentType.<T>builder () .persistent (codec) .build ();
    }
    public static final RegistryObject <DataComponentType <PKCPComponent>> pkcp = registry.register ("pkcp", reg (CODEC));
    public static final RegistryObject <DataComponentType <MacroComponent>> macro = registry.register ("macro", reg (MacroComponent.CODEC));
}
