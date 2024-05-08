package bluet.pkcp;

import java.util.Optional;

import bluet.pkcp.macro.MacroItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PKCPItem extends Item {
    public PKCPItem () {
        super (new Item.Properties () .stacksTo (1) .fireResistant ());
    }
    public static void shift_send (LocalPlayer player, ItemStack stack) {
        PKCPComponent component = stack.get (PKCPComponent.pkcp.get ());
        String item = ForgeRegistries.ITEMS.getKey (stack.getItem ()) .toString ();
        if (component != null) {
            // Landing Block
            BlockPos lb = component.landing ();
            item += String.format ("[pkcp:pkcp={landing:[I;%d,%d,%d],", lb.getX (), lb.getY (), lb.getZ ());
            // Return point
            item += String.format ("x:%.8fd,y:%.8fd,z:%.8fd,", component.retx (), component.rety (), component.retz ());
            item += String.format ("xr:%.8ff,yr:%.8ff,", component.rotx (), component.roty ());
            // Landing mode
            item += String.format ("mode:{landmode:%d}}]", component.mode () .toint ());
        }
        ClickEvent event = new ClickEvent (ClickEvent.Action.COPY_TO_CLIPBOARD, item);
        Style style = Style.EMPTY.withColor (0x22ff22) .withClickEvent (event);
        Component msg = Component.translatable ("pkcp.msg.click_copy") .withStyle (style);
        player.sendSystemMessage (msg);
    }
    @Override
    public InteractionResultHolder <ItemStack> use (Level level, Player player, InteractionHand hand) {
        // if (level.isClientSide ()) return super.use (level, player, hand);
        if (hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.fail (player.getItemInHand (hand));
        ItemStack stack = player.getItemInHand (hand);
        PKCPComponent com = stack.get (PKCPComponent.pkcp.get ());
        if (com == null) return InteractionResultHolder.fail (stack);
        // Shift copying
        if (player instanceof LocalPlayer pl) {
            Minecraft mc = Minecraft.getInstance ();
            Options opt = mc.options;
            if (opt.keyShift.isDown ()) {
                shift_send (pl, stack);
                return InteractionResultHolder.success (stack);
            }
        }
        com.returning (player);
        return InteractionResultHolder.success (stack);
    }
    private double lx, ly, lz;
    private boolean last = false, ground = false;
    private long lasttick;
    protected static class MaxObject {
        private double mx;
        private boolean ini;
        private final Player player;
        private final BlockPos base;
        private final PKCPLandMode mode;
        private final double lx, ly, lz;
        private final boolean ground;
        public MaxObject (Player player, BlockPos pos, PKCPLandMode mode, double lx, double ly, double lz, boolean ground) {
            ini = false;
            this.player = player;
            this.base = pos;
            this.mode = mode;
            this.lx = lx;
            this.ly = ly;
            this.lz = lz;
            this.ground = ground;
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
            int yb = this.base.getY ();
            int zb = this.base.getZ ();
            x1 += xb; x2 += xb;
            y1 += yb; y2 += yb;
            z1 += zb; z2 += zb;
            double tmp;
            // Player on the block or player lower than the block
            boolean flag;
            if (player.onGround ()) {
                var dy = Math.abs (player.getY () - Math.max (y1, y2));
                flag = dy <= 0.000001d;
                flag = flag && !this.ground;
            } else {
                flag = player.getY () < Math.max (y1, y2) && this.ly >= Math.max (y1, y2);
            }
            if (!flag) return;
            if (x1 > x2) {
                tmp = x1; x1 = x2; x2 = tmp;
            }
            if (z1 > z2) {
                tmp = z1; z1 = z2; z2 = tmp;
            }
            double mx = (x1 + x2) / 2;
            double mz = (z1 + z2) / 2;
            double rx, rz;
            rx = (.3d + x2 - mx) - Math.abs (lx - mx);
            rz = (.3d + z2 - mz) - Math.abs (lz - mz);
            if (rx < 0 || rz < 0) {
                rx = Math.min (0, rx);
                rz = Math.min (0, rz);
                this.setmax (-Math.sqrt (rx * rx + rz * rz));
            } else {
                if (mode == PKCPLandMode.X_ONLY) this.setmax (rx);
                else if (mode == PKCPLandMode.Z_ONLY) this.setmax (rz);
                else this.setmax (Math.sqrt (rx * rx + rz * rz));
            }
        }
    }
    public Optional <Double> calc_dist (Level level, BlockPos pos, Player player, PKCPLandMode mode) {
        BlockState state = level.getBlockState (pos);
        VoxelShape shape = state.getCollisionShape (level, pos);
        MaxObject obj = new MaxObject (player, pos, mode, this.lx, this.ly, this.lz, this.ground);
        shape.forAllBoxes (obj::consume);
        if (obj.init () == false) return Optional.empty ();
        else return Optional.of (obj.getmax ());
    }
    @Override
    public void inventoryTick (ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player && level.isClientSide ()) {
            if (!selected) return;
            tick (stack, level, player);
            this.last = true;
            this.lx = player.getX ();
            this.ly = player.getY ();
            this.lz = player.getZ ();
            this.lasttick = level.getGameTime ();
            this.ground = player.onGround ();
        }
    }
    public void tick (ItemStack stack, Level level, Player player) {
        if (! this.last) return;
        if (this.lasttick + 1 != level.getGameTime ()) return;
        PKCPComponent component = stack.get (PKCPComponent.pkcp.get ());
        if (component == null) return;
        PKCPLandMode mode = component.mode ();
        BlockPos pos = component.landing ();
        Optional <Double> d = calc_dist (level, pos, player, mode);
        if (d.isPresent ()) {
            double dist = d.get ();
            if (dist < -1) return;
            boolean landed = dist >= 0d;
            player.displayClientMessage (Component.translatable ("pkcp.msg.offset") .append (String.format ("%.8f", dist)) .withColor (landed ? 0x0000ff00 : 0x00ff0000), true);
            if (landed || MacroItem.instance.running ()) player.sendSystemMessage (Component.literal ((landed ? "+" : "") + String.format ("%.8f", dist)) .withColor (landed ? 0x0000ff00 : 0x00ff0000));
        }
    }
    public static final DeferredRegister <Item> registry = DeferredRegister.create (ForgeRegistries.ITEMS, PKCP.MOD_ID);
    public static final RegistryObject <Item> pkcp = registry.register ("pkcp", PKCPItem::new);
    public static final RegistryObject <Item> pksetlb = registry.register ("pksetlb", PKSetLB::new);
    public static final RegistryObject <Item> pksetreturn = registry.register ("pksetreturn", PKSetReturn::new);
    public static final RegistryObject <Item> pksetmode = registry.register ("pksetmode", PKSetMode::new);
    public static final RegistryObject <Item> pkmacro = registry.register ("pkmacro", MacroItem::new);
}
