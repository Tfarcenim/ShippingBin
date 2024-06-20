package tfar.shippingbin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.menu.ShippingBinMenu;

public class ShippingBinScreen extends AbstractContainerScreen<ShippingBinMenu> {

    private static final ResourceLocation TEXTURE_LOCATION = ShippingBin.id("textures/gui/shipping_bin.png");

    public ShippingBinScreen(ShippingBinMenu $$0, Inventory $$1, Component $$2) {
        super($$0, $$1, $$2);
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
    }


    @Override
    protected void init() {
        super.init();

        addRenderableWidget(Button.builder(Component.literal("Input"),button -> sendButtonToServer(ShippingBinMenu.ButtonAction.INPUT)).bounds(leftPos,topPos,20,20).build());
        addRenderableWidget(Button.builder(Component.literal("Output"),button -> sendButtonToServer(ShippingBinMenu.ButtonAction.OUTPUT)).bounds(leftPos+40,topPos,20,20).build());

    }

    private void sendButtonToServer(ShippingBinMenu.ButtonAction action) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, action.ordinal());
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE_LOCATION, i, j, 0, 0, this.imageWidth, 6 * 18 + 17);
        guiGraphics.blit(TEXTURE_LOCATION, i, j + 6 * 18 + 17, 0, 126, this.imageWidth, 96);
    }

    public static ShippingBinScreen shippingBin(ShippingBinMenu barrelContainer, Inventory playerInventory, Component component) {
        return new ShippingBinScreen(barrelContainer,playerInventory,component);
    }

}
