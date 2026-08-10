package tfar.shippingbin.client;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.UnknownNullability;

public class ModClientNeoForge {

    public static void init(net.neoforged.bus.api.@UnknownNullability IEventBus bus) {
        bus.addListener(ModClientNeoForge::setup);
    }

    public static void setup(FMLClientSetupEvent event) {
        ModClient.setup();
    }

}
