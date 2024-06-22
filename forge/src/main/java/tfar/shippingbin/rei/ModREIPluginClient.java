package tfar.shippingbin.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.core.registries.BuiltInRegistries;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.client.ShippingBinScreen;
import tfar.shippingbin.init.ModItems;
import tfar.shippingbin.trades.Trade;

import java.util.Comparator;

public class ModREIPluginClient implements REIClientPlugin {

    public static CategoryIdentifier<TradeDisplay> TRADING = CategoryIdentifier.of(ShippingBin.MOD_ID, "plugins/trading");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new TradeCategory());
        registry.addWorkstations(TRADING, EntryStacks.of(ModItems.SHIPPING_BIN));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(Trade.class, TradeDisplay::new);

        ShippingBin.getTradeManager().getTrades().forEach((resourceLocation, trade) -> registry.add(new TradeDisplay(trade)));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(88, 32, 28, 23), ShippingBinScreen.class, TRADING);
    }

}
