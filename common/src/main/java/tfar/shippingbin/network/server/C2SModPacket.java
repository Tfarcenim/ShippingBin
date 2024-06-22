package tfar.shippingbin.network.server;

import net.minecraft.server.level.ServerPlayer;
import tfar.shippingbin.network.ModPacket;

public interface C2SModPacket extends ModPacket {

    void handleServer(ServerPlayer player);

}
