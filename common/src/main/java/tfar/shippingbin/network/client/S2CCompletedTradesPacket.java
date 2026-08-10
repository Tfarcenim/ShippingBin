package tfar.shippingbin.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.client.ModClient;
import tfar.shippingbin.trades.CompletedTrade;

import java.util.ArrayList;
import java.util.List;


public record S2CCompletedTradesPacket(List<CompletedTrade> trades) implements S2CModPacket {

    public static final Type<S2CCompletedTradesPacket> TYPE = new Type<>(ShippingBin.id("completed_trades"));
    public static final StreamCodec<RegistryFriendlyByteBuf,S2CCompletedTradesPacket> STREAM_CODEC =
            StreamCodec.composite(CompletedTrade.STREAM_CODEC.apply(ByteBufCodecs.list()),S2CCompletedTradesPacket::trades,S2CCompletedTradesPacket::new);
    @Override
    public void handleClient() {
        ModClient.displayCompletedTrades(trades);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
