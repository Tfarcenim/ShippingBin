package tfar.shippingbin.network.client;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.client.ModClient;
import tfar.shippingbin.trades.CompletedTrade;
import tfar.shippingbin.trades.Trade;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public record S2CClientTradesPacket(Map<ResourceLocation, Trade> trades) implements S2CModPacket {

    public static final Type<S2CClientTradesPacket> TYPE = new Type<>(ShippingBin.id("client_trades"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CClientTradesPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(LinkedHashMap::new,ResourceLocation.STREAM_CODEC,Trade.STREAM_CODEC),
                    S2CClientTradesPacket::trades, S2CClientTradesPacket::new);
    @Override
    public void handleClient() {
        ModClient.syncTrades(trades);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
