package tfar.shippingbin.network.client;

import tfar.shippingbin.network.ModPacket;

public interface S2CModPacket extends ModPacket {

    void handleClient();

}
