package tfar.shippingbin.datagen.data;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class TradeBuilder {

    private final ItemStack result;
    private final Ingredient ingredient;

    public TradeBuilder(ItemStack stack,Ingredient ingredient) {
        this.result = stack;
        this.ingredient = ingredient;
    }

    public static TradeBuilder builder(ItemLike output,ItemLike input) {
        Ingredient ingredient = Ingredient.of(output);
        ItemStack stack = new ItemStack(input);
        return new TradeBuilder(stack,ingredient);
    }

    public void save(Consumer<FinishedTrade> consumer, ResourceLocation pRecipeId) {
        consumer.accept(new Result(pRecipeId, this.result, ingredient));
    }

    public void save(Consumer<FinishedTrade> consumer) {
        this.save(consumer, getDefaultTradeId(result.getItem()));
    }

    static ResourceLocation getDefaultTradeId(ItemLike pItemLike) {
        return BuiltInRegistries.ITEM.getKey(pItemLike.asItem());
    }

    public static class Result implements FinishedTrade {

        private final ResourceLocation tradeId;
        private final ItemStack result;
        private final Ingredient ingredient;

        public Result(ResourceLocation tradeId, ItemStack result, Ingredient ingredient) {

            this.tradeId = tradeId;
            this.result = result;
            this.ingredient = ingredient;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("input",ingredient.toJson());
            pJson.add("output",writeStack(result));
        }

        public static JsonObject writeStack(ItemStack stack) {
            Item item = stack.getItem();
            String itemName = BuiltInRegistries.ITEM.getKey(item).toString();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item",itemName);

            if (stack.hasTag()) {
                jsonObject.addProperty("nbt",stack.getTag().toString());
            }

            return jsonObject;
        }

        @Override
        public ResourceLocation getId() {
            return tradeId;
        }
    }

}
