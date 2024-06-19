package tfar.shippingbin.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import tfar.shippingbin.init.ModMenuTypes;
import tfar.shippingbin.menu.ShippingBinMenu;

public class ModClient {

    public static void setup() {
        MenuScreens.register(ModMenuTypes.SHIPPING_BIN, (ShippingBinMenu barrelContainer, Inventory playerInventory, Component component) -> ShippingBinScreen.shippingBin(barrelContainer, playerInventory, component));
    }

}
