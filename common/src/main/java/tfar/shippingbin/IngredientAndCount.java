package tfar.shippingbin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record IngredientAndCount(Ingredient ingredient, int count) {
    public static final Codec<IngredientAndCount> CODEC = RecordCodecBuilder.create(tradeInstance -> tradeInstance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(IngredientAndCount::ingredient),
            Codec.INT.fieldOf("count").forGetter(IngredientAndCount::count)
    ).apply(tradeInstance, IngredientAndCount::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, IngredientAndCount> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,IngredientAndCount::ingredient,
            ByteBufCodecs.INT,IngredientAndCount::count,
            IngredientAndCount::new);

    public boolean test(ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() >= count;
    }
}
