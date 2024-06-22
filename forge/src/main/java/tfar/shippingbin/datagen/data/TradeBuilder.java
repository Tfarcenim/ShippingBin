package tfar.shippingbin.datagen.data;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TradeBuilder {

    private final ItemStack result;
    private final int count;
    private final Ingredient ingredient;
    @Nullable Attribute attribute;

    public TradeBuilder(ItemStack stack,Ingredient ingredient,int count) {
        this.result = stack;
        this.ingredient = ingredient;
        this.count = count;
    }

    public static TradeBuilder builder(ItemLike output,ItemLike input) {
        Ingredient ingredient = Ingredient.of(input);
        ItemStack stack = new ItemStack(output);
        return builderWithCount(stack,ingredient,1);
    }

    public static TradeBuilder builderWithCount(ItemStack output,Ingredient input,int count) {
        return new TradeBuilder(output, input,count);
    }

    public static TradeBuilder builderWithCount(ItemLike output,Ingredient input,int count) {
        return new TradeBuilder(output.asItem().getDefaultInstance(), input,count);
    }

    public static TradeBuilder builderWithCount(ItemLike output, TagKey<Item> input, int count) {
        return new TradeBuilder(output.asItem().getDefaultInstance(), Ingredient.of(input),count);
    }

    public static TradeBuilder builderWithCount(ItemLike output,Item input,int count) {
        return builderWithCount(new ItemStack(output),Ingredient.of(input),count);
    }

    public TradeBuilder setAttribute(@Nullable Attribute attribute) {
        this.attribute = attribute;
        return this;
    }

    public void save(Consumer<FinishedTrade> consumer, ResourceLocation pRecipeId) {
        consumer.accept(new Result(pRecipeId, this.result, ingredient, count,attribute));
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
        private final int count;
        @Nullable private final Attribute attribute;

        public Result(ResourceLocation tradeId, ItemStack result, Ingredient ingredient, int count, @Nullable Attribute attribute) {
            this.tradeId = tradeId;
            this.result = result;
            this.ingredient = ingredient;
            this.count = count;
            this.attribute = attribute;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            JsonObject input = new JsonObject();
            input.add("ingredient",ingredient.toJson());
            input.addProperty("count",count);
            pJson.add("input",input);
            pJson.add("output",writeStack(result));
            if (attribute != null) {
                pJson.addProperty("attribute",BuiltInRegistries.ATTRIBUTE.getKey(attribute).toString());
            }
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
