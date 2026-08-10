package tfar.shippingbin.trades;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import tfar.shippingbin.Utils;

public record Trade(Ingredient input, int count, ItemStack output, @Nullable Holder<Attribute> attribute) {

    public static final Codec<Trade> CODEC = RecordCodecBuilder.create(tradeInstance -> tradeInstance.group(
            Ingredient.CODEC.fieldOf("input").forGetter(Trade::input),
            Codec.INT.fieldOf("input_count").forGetter(Trade::count),

            ItemStack.CODEC.fieldOf("input").forGetter(Trade::output),
            BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(Trade::attribute)
            ).apply(tradeInstance, Trade::new));

    public boolean matches(ItemStack stack) {
        return input.test(stack) && stack.getCount() >= count;
    }

    @Nullable
    public static Trade deserialize(RegistryOps<JsonElement> ops,JsonObject jsonObject) {
        if (jsonObject.size() == 0) {
            return null;
        }
        return CODEC.parse(ops,jsonObject).getOrThrow(JsonParseException::new);
    }
}
