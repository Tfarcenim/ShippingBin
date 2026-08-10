package tfar.shippingbin.network;


import org.spongepowered.asm.mixin.MixinEnvironment;
import tfar.shippingbin.network.client.S2CClientTradesPacket;
import tfar.shippingbin.network.client.S2CCompletedTradesPacket;
import tfar.shippingbin.platform.Services;

public class PacketHandler {

    public static void registerPackets() {
        if (!Services.PLATFORM.getPlatformName().equals("Fabric") || MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
            registerClientPackets();
        }
    }

    public static void registerClientPackets() {
        Services.PLATFORM.registerClientPacket(S2CCompletedTradesPacket.TYPE, S2CCompletedTradesPacket.STREAM_CODEC);
        Services.PLATFORM.registerClientPacket(S2CClientTradesPacket.TYPE, S2CClientTradesPacket.STREAM_CODEC);

    }
}
