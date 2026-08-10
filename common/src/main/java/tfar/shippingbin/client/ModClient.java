package tfar.shippingbin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import tfar.shippingbin.ShippingBinConfig;
import tfar.shippingbin.init.ModMenuTypes;
import tfar.shippingbin.menu.ShippingBinMenu;
import tfar.shippingbin.trades.CompletedTrade;
import tfar.shippingbin.trades.Trade;
import tfar.shippingbin.trades.TradeManager;

import java.util.List;
import java.util.Map;

public class ModClient {

    static TradeManager tradeManager;

    public static void setup() {
        MenuScreens.register(ModMenuTypes.SHIPPING_BIN, (ShippingBinMenu barrelContainer, Inventory playerInventory, Component component) -> ShippingBinScreen.shippingBin(barrelContainer, playerInventory, component));
    }

    public static void displayCompletedTrades(List<CompletedTrade> list) {
        if (ShippingBinConfig.Client.DISPLAY_SELL_TOAST.get()) {
            for (CompletedTrade completedTrade : list) {
                Minecraft.getInstance().getToasts().addToast(new CompletedTradeToast(completedTrade));
            }
        }
    }

    public static TradeManager getTradeManager() {
        if (tradeManager == null) {
            tradeManager = new TradeManager(Minecraft.getInstance().level.registryAccess());
        }
        return tradeManager;
    }

    public static void syncTrades(Map<ResourceLocation, Trade> trades) {
        getTradeManager().replaceTrades(trades);
    }
}
