package tfar.shippingbin.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.menu.ShippingBinMenu;

public class ShippingBinScreen extends AbstractContainerScreen<ShippingBinMenu> {

    private static final ResourceLocation TEXTURE_LOCATION = ShippingBin.id("textures/gui/shipping_bin2.png");

    public ShippingBinScreen(ShippingBinMenu $$0, Inventory $$1, Component $$2) {
        super($$0, $$1, $$2);
        this.imageHeight = 222+13;
        this.inventoryLabelY = this.imageHeight - 94;
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
        guiGraphics.blit(TEXTURE_LOCATION, i, j, 0, 0, this.imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, ChatFormatting.WHITE.getColor(), false);
       // guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
        //super.renderLabels(guiGraphics, mouseX, mouseY);
        guiGraphics.drawString(font,Component.literal("Profits"),titleLabelX,titleLabelY+68, ChatFormatting.WHITE.getColor(),false);
    }

    public static ShippingBinScreen shippingBin(ShippingBinMenu barrelContainer, Inventory playerInventory, Component component) {
        return new ShippingBinScreen(barrelContainer,playerInventory,component);
    }

}
