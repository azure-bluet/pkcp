package bluet.pkcp;

import java.util.Locale;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PKCPItem extends Item {
    public PKCPItem () {
        super (new Item.Properties () .stacksTo (1) .fireResistant ());
    }
    @Override
    public InteractionResultHolder <ItemStack> use (Level level, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.fail (player.getItemInHand (hand));
        ItemStack stack = player.getItemInHand (hand);
        CompoundTag tag = stack.getTag ();
        if (tag == null) return InteractionResultHolder.fail (stack);
        tag = tag.getCompound ("pos");
        double x, y, z;
        x = tag.getDouble ("x");
        y = tag.getDouble ("y");
        z = tag.getDouble ("z");
        float xr, yr;
        xr = tag.getFloat ("xrot");
        yr = tag.getFloat ("yrot");
        player.teleportTo (x, y, z);
        player.setXRot (xr);
        player.setYRot (yr);
        return InteractionResultHolder.success (stack);
    }
    protected static class MaxObject {
        private double mx;
        private boolean ini;
        private final Player player;
        private final BlockPos base;
        public MaxObject (Player player, BlockPos pos) {
            ini = false;
            this.player = player;
            this.base = pos;
        }
        public void setmax (double d) {
            if (ini) mx = mx > d ? mx : d;
            else {
                mx = d;
                ini = true;
            }
        }
        public boolean init () {
            return this.ini;
        }
        public double getmax () {
            return this.mx;
        }
        public void consume (double x1, double y1, double z1, double x2, double y2, double z2) {
            int xb = this.base.getX ();
            int zb = this.base.getZ ();
            x1 += xb; x2 += xb;
            z1 += zb; z2 += zb;
            double tmp;
            tmp = Math.max (y1, y2) + this.base.getY ();
            Vec3 vec = this.player.getDeltaMovement ();
            if (player.getY () - tmp <= .0000001d) return;
            if (player.getY () + vec.y >= tmp) return;
            if (x1 > x2) {
                tmp = x1; x1 = x2; x2 = tmp;
            }
            if (z1 > z2) {
                tmp = z1; z1 = z2; z2 = tmp;
            }
            double px = player.getX ();
            double pz = player.getZ ();
            double mx = (x1 + x2) / 2;
            double mz = (z1 + z2) / 2;
            double rx, rz;
            rx = (.3d + x2 - mx) - Math.abs (px - mx);
            rz = (.3d + z2 - mz) - Math.abs (pz - mz);
            if (rx < 0 || rz < 0) {
                rx = Math.min (0, rx);
                rz = Math.min (0, rz);
                this.setmax (-Math.sqrt (rx * rx + rz * rz));
            } else {
                if (Math.max (rx, rz) > .3d) this.setmax (Math.min (rx, rz));
                else this.setmax (Math.sqrt (rx * rx + rz * rz));
            }
        }
    }
    public static Optional <Double> calc_dist (Level level, BlockPos pos, Player player) {;
        BlockState state = level.getBlockState (pos);
        VoxelShape shape = state.getCollisionShape (level, pos);
        MaxObject obj = new MaxObject (player, pos);
        shape.forAllBoxes (obj::consume);
        if (obj.init () == false) return Optional.empty ();
        else return Optional.of (obj.getmax ());
    }
    @Override
    public void inventoryTick (ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (! selected) return;
        if (entity instanceof Player player) {
            int x, y, z;
            CompoundTag tag = stack.getTag ();
            if (tag == null) return;
            tag = tag.getCompound ("block");
            x = tag.getInt ("x");
            y = tag.getInt ("y");
            z = tag.getInt ("z");
            BlockPos pos = new BlockPos (x, y, z);
            Optional <Double> d = calc_dist (level, pos, player);
            if (d.isPresent ()) {
                double dist = d.get ();
                if (dist < -1) return;
                player.displayClientMessage (Component.translatable ("pkcp.msg.offset") .append (String.format (Locale.ROOT, "%.6f", dist)), true);
            }
        }
    }
    public static final DeferredRegister <Item> registry = DeferredRegister.create (ForgeRegistries.ITEMS, PKCP.MOD_ID);
    public static final RegistryObject <Item> pkcp = registry.register ("pkcp", PKCPItem::new);
    public static final RegistryObject <Item> pksetlb = registry.register ("pksetlb", PKSetLB::new);
    public static final RegistryObject <Item> pksetreturn = registry.register ("pksetreturn", PKSetReturn::new);
}