package tfar.shippingbin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import tfar.shippingbin.trades.CompletedTrade;

import java.util.List;

public class CompletedTradeToast implements Toast {
    private final CompletedTrade completedTrade;
    long duration = 2500L;

    public CompletedTradeToast(CompletedTrade completedTrade) {

        this.completedTrade = completedTrade;
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent manager, long startTime) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.blit(TEXTURE, 0, 0, 0, 32, this.width(), this.height());

            List<FormattedCharSequence> split = manager.getMinecraft().font.split(completedTrade.message(),130);

        int y = (int) (17 - split.size() * 5.5);
        for (int i = 0; i < split.size();i++) {
                graphics.drawString(manager.getMinecraft().font,split.get(i),24,y + i * 11,ChatFormatting.BLACK.getColor(),false);
                // graphics.drawWordWrap(manager.getMinecraft().font, completedTrade.message(), 24, 12,140, ChatFormatting.BLACK.getColor());
            }

            graphics.renderFakeItem(completedTrade.icon(), 5, 7);

        //    graphics.drawString(manager.getMinecraft().font, title(), 22, 7, ChatFormatting.WHITE.getColor());
        return startTime > duration ? Visibility.HIDE : Visibility.SHOW;
    }
}
