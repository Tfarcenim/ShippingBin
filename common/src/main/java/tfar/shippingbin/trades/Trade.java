package tfar.shippingbin.trades;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import tfar.shippingbin.IngredientAndCount;

import java.util.Optional;

public record Trade(IngredientAndCount input, ItemStack output, Optional<Holder<Attribute>> attribute) {

    public static final StreamCodec<RegistryFriendlyByteBuf,Trade> STREAM_CODEC = StreamCodec.composite(
            IngredientAndCount.STREAM_CODEC,Trade::input,
            ItemStack.STREAM_CODEC,Trade::output,
            ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE)),Trade::attribute,
            Trade::new);

    public static final Codec<Trade> CODEC = RecordCodecBuilder.create(tradeInstance -> tradeInstance.group(
            IngredientAndCount.CODEC.fieldOf("input").forGetter(Trade::input),

            ItemStack.CODEC.fieldOf("output").forGetter(Trade::output),
            BuiltInRegistries.ATTRIBUTE.holderByNameCodec().optionalFieldOf("attribute")
                    .forGetter(Trade::attribute)
            ).apply(tradeInstance, Trade::new));

    public boolean matches(ItemStack stack) {
        return input.test(stack);
    }

    @Nullable
    public static Trade deserialize(RegistryOps<JsonElement> ops,JsonObject jsonObject) {
        if (jsonObject.size() == 0) {
            return null;
        }
        return CODEC.parse(ops,jsonObject).getOrThrow(JsonParseException::new);
    }
}
