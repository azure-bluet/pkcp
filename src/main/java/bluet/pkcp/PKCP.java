package bluet.pkcp;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod (PKCP.MOD_ID)
public class PKCP {
    public static final String MOD_ID = "pkcp";
    public PKCP () {
        IEventBus bus = FMLJavaModLoadingContext.get () .getModEventBus ();
        PKCPItem.registry.register (bus);
        PKCPTab.registry.register (bus);
        PKCPComponent.registry.register (bus);
    }
}
