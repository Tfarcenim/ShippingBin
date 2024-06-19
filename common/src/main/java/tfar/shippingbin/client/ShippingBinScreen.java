package tfar.shippingbin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.menu.ShippingBinMenu;

public class ShippingBinScreen extends AbstractContainerScreen<ShippingBinMenu> {
    public ShippingBinScreen(ShippingBinMenu $$0, Inventory $$1, Component $$2) {
        super($$0, $$1, $$2);
    }



    public static ShippingBinScreen shippingBin(ShippingBinMenu barrelContainer, Inventory playerInventory, Component component) {
        return new ShippingBinScreen(barrelContainer,playerInventory,component);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }
}
