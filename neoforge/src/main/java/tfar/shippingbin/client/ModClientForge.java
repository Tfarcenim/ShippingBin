package tfar.shippingbin.client;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModClientForge {

    public static void init(IEventBus bus) {
        bus.addListener(ModClientForge::setup);
    }

    public static void setup(FMLClientSetupEvent event) {
        ModClient.setup();
    }

}
