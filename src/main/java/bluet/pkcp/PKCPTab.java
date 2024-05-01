package bluet.pkcp;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PKCPTab {
    public static final DeferredRegister <CreativeModeTab> registry = DeferredRegister.create (Registries.CREATIVE_MODE_TAB, PKCP.MOD_ID);
    public static final RegistryObject <CreativeModeTab> pkcp_tab = registry.register (
        "pkcp_tab",
        () -> CreativeModeTab.builder ()
        .icon (() -> new ItemStack (PKCPItem.pkcp.get ()))
        .title (Component.translatable ("tab.pkcp.pkcp_tab"))
        .displayItems (
            (p, t) -> {
                t.accept (PKCPItem.pkcp.get ());
                t.accept (PKCPItem.pksetlb.get ());
                t.accept (PKCPItem.pksetreturn.get ());
                t.accept (PKCPItem.pksetmode.get ());
                t.accept (PKCPItem.pkmacro.get ());

                ItemStack head = new ItemStack (Items.PLAYER_HEAD);
                head.getOrCreateTag () .putString ("SkullOwner", "azure__bluet");
                t.accept (head);
            }
        )
        .build ()
    );
}
