package tfar.shippingbin.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.platform.NeoForgePlatformHelper;

public class PacketHandlerNeoForge {
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ShippingBin.MOD_ID);
        NeoForgePlatformHelper.registrar =  registrar;
        PacketHandler.registerPackets();
    }
}
