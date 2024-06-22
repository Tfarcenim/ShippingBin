package tfar.shippingbin.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import tfar.shippingbin.init.ModMenuTypes;
import tfar.shippingbin.menu.ShippingBinMenu;
import tfar.shippingbin.trades.TradeManager;

public class ModClient {

    static final TradeManager tradeManager = new TradeManager();

    public static void setup() {
        MenuScreens.register(ModMenuTypes.SHIPPING_BIN, (ShippingBinMenu barrelContainer, Inventory playerInventory, Component component) -> ShippingBinScreen.shippingBin(barrelContainer, playerInventory, component));
    }

    public static void loadTrades() {

    }

    public static TradeManager getTradeManager() {
        return tradeManager;
    }
}
