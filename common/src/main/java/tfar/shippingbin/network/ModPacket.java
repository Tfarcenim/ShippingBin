package tfar.shippingbin.network;

import net.minecraft.network.FriendlyByteBuf;

public interface ModPacket {
    void write(FriendlyByteBuf to);
}