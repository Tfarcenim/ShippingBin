package tfar.shippingbin.rei;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import tfar.shippingbin.Utils;
import tfar.shippingbin.init.ModBlocks;

import java.util.List;

public class TradeCategory implements DisplayCategory<TradeDisplay> {
    @Override
    public CategoryIdentifier<TradeDisplay> getCategoryIdentifier() {
        return ModREIPluginClient.TRADING;
    }

    @Override
    public List<Widget> setupDisplay(TradeDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 65, bounds.getCenterY() - 9);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 5, startPoint.y)).entries(display.getInputEntries().get(0)).markInput());

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 30, startPoint.y)));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 64, startPoint.y)).entries(display.getOutputEntries().get(0)).markOutput());
        if (display.attribute != null) {
            widgets.add(Widgets.createLabel(new Point(bounds.x + 26, bounds.getMaxY() - 12),Component.translatable("category.shippingbin.trading.attribute",Component.translatable(display.attribute.getDescriptionId())))
                    .color(0xFF404040, 0xFFBBBBBB).noShadow().leftAligned());
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 46;
    }

    @Override
    public Component getTitle() {
        return Utils.TRADING;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.SHIPPING_BIN);
    }
}
