package tfar.shippingbin.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import tfar.shippingbin.client.ModClient;
import tfar.shippingbin.trades.CompletedTrade;

import java.util.ArrayList;
import java.util.List;


public class S2CCompletedTradesPacket implements S2CModPacket {

    List<CompletedTrade> trades = new ArrayList<>();

    public S2CCompletedTradesPacket(List<CompletedTrade> trades) {
        this.trades = trades;
    }


    public S2CCompletedTradesPacket(FriendlyByteBuf buf) {
      int size = buf.readInt();
      for (int i = 0; i < size;i++) {
          trades.add(CompletedTrade.read(buf));
      }
    }

    @Override
    public void handleClient() {
        ModClient.displayCompletedTrades(trades);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(trades.size());
        trades.forEach(completedTrade -> completedTrade.write(to));
    }
}
