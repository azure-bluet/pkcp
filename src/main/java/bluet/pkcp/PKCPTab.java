package bluet.pkcp;

import java.util.Optional;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
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
                ItemStack pkcp = new ItemStack (PKCPItem.pkcp.get ());
                pkcp.set (PKCPComponent.pkcp.get (), new PKCPComponent (BlockPos.ZERO, 0d, 0d, 0d, 0f, 0f, PKCPLandMode.BOTH));
                t.accept (pkcp);
                t.accept (PKCPItem.pksetlb.get ());
                t.accept (PKCPItem.pksetreturn.get ());
                t.accept (PKCPItem.pksetmode.get ());

                // Fuck you Mojang HOWWWWWWWWWw
                // Hack implementation for now, better code coming soon!
                // Um probably not soon
                ItemStack head = new ItemStack (Items.PLAYER_HEAD);
                PropertyMap map = new PropertyMap ();
                map.put (
                    "F**kYouMojangWhyAreYouDoingThis",
                    new Property (
                        "textures",
                        "ewogICJ0aW1lc3RhbXAiIDogMTcxNTAwMDk0Nzc0NCwKICAicHJvZmlsZUlkIiA6ICJmMWE5MDJmNjNmOGY0MTFiOGY2NTJhNDcxN2QxZjM0MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJhenVyZV9fYmx1ZXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU2YTNkMTJlMjQxMTg3NmY3YzVmMmFkMjdkZThiOWQzZTY1NWJmYTI3OWZlZDA5ZTY2YWFkMGZkNzhmYWZlOSIKICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZkNTUzYjM5MzU4YTI0ZWRmZTNiOGE5YTkzOWZhNWZhNGZhYTRkOWE5YzNkNmFmOGVhZmIzNzdmYTA1YzJiYiIKICAgIH0KICB9Cn0=",
                        "X4RC93xvwZYug5w+3zPDi08OD/P/TsKqsBah/hhfTBC+BG9mAm7cbfZ1dqlUsrzbTJ6jX2o0hXLNbo4PCtbbHjava2rkQq88MipeuA+OMf8EkkaIDrQJP6cddXwtOxUwxXD4WppgAHrD4wWECxdP41Powcrud4yIr3bRSMkRpMWQ29OT7a5bDJz7Vcv/7kRU0pvSkBn/BOqKjAAkM3xsbqG6TVMnLYiQCXfQmw4+LuzjsCzFPE3vzI+XYEwtPBgB04TY/oSvrulNAmqrMddgpgKMjvOuuRuwkbk3hLXXMwzeJ3eKlwOYvLD8a6oQyecUeB5NidOo2HEY9ORfmZ+gfu7FBjVM1tQtMeWd7r4PHjbt5hfwAhHcK326wpTwIXj4D/KLv7ZSxnZfyG0gnhBM5D8sMUZElv1Z/O4w2eo22/Y2Km6qEoppxRfbAyTKHljKSyDoE/RKNualJtLWLb/QQFi2moa3YvYQdHEuY/RtW3fU57cHSTk7VqMqnoVFttCGyRt3/Elr/jilogCWQkaGxSykUJ+kTcVrmJ4BX31bavXQaSuhAmJv81amgRHTTQzUwI2ujH2DEVSMCB94QFbqx2jwUmlNFblwsULOEkhQcvM47WsCC/SHQ55wk7VCWl/LsJyxFBSUtW5TfrPq2MOAMHktbjjiTiUYFcjWtnfMLbE="
                    )
                );
                ResolvableProfile profile = new ResolvableProfile (Optional.of ("azure__bluet"), Optional.of (Util.NIL_UUID), map);
                head.set (DataComponents.PROFILE, profile);
                t.accept (head);
            }
        )
        .build ()
    );
}
