package tfar.shippingbin.datagen.data;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import tfar.shippingbin.IngredientAndCount;
import tfar.shippingbin.trades.Trade;

import java.util.Optional;
import java.util.function.BiConsumer;

public class TradeBuilder {

    private final ItemStack result;
    private final int count;
    private final Ingredient ingredient;
    Optional<Holder<Attribute>> attribute =  Optional.empty();

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

    public TradeBuilder setAttribute(Holder<Attribute> attribute) {
        this.attribute = Optional.of(attribute);
        return this;
    }

    public void save(BiConsumer<Trade,ResourceLocation> output,ResourceLocation id) {
        output.accept(new Trade(new IngredientAndCount(ingredient,count),result,attribute),id);
    }

    public void save(BiConsumer<Trade,ResourceLocation> output) {
        this.save(output, getDefaultTradeId(result.getItem()));
    }

    static ResourceLocation getDefaultTradeId(ItemLike pItemLike) {
        return BuiltInRegistries.ITEM.getKey(pItemLike.asItem());
    }
}
