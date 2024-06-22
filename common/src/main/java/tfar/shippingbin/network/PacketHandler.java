package tfar.shippingbin.network;


import org.spongepowered.asm.mixin.MixinEnvironment;
import tfar.shippingbin.network.client.S2CTradePacket;
import tfar.shippingbin.platform.Services;

public class PacketHandler {

    public static void registerPackets() {
        if (!Services.PLATFORM.getPlatformName().equals("Fabric") || MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
            registerClientPackets();
        }
    }

    public static void registerClientPackets() {
        Services.PLATFORM.registerClientPacket(S2CTradePacket.class, S2CTradePacket::new);
    }
}
