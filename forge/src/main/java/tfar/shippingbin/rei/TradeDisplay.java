package tfar.shippingbin.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.brewing.BrewingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import tfar.shippingbin.trades.Trade;

import java.util.List;
import java.util.Optional;

public class TradeDisplay extends BasicDisplay{

    public TradeDisplay(Trade trade) {
        this(trade.getInput(), trade.getCount(),trade.getOutput());
    }

    public TradeDisplay(Ingredient input, int count, ItemStack output) {
        this(List.of(createStackedIngredient(input,count)),List.of(EntryIngredients.of(output)),Optional.empty());
    }

    public TradeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location) {
        super(inputs, outputs, location);
    }


    static EntryIngredient createStackedIngredient(Ingredient ingredient,int count) {
        ItemStack[] matching = ingredient.getItems();
        EntryIngredient.Builder result = EntryIngredient.builder(matching.length);
        for (ItemStack stack : matching) {
            ItemStack copy = stack.copy();
            copy.setCount(count);
            result.add(EntryStacks.of(copy));
        }
        return result.build();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ModREIPluginClient.TRADING;
    }
}
