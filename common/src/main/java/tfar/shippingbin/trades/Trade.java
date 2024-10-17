package tfar.shippingbin.trades;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import tfar.shippingbin.Utils;

public record Trade(Ingredient input, int count, ItemStack output, @Nullable Attribute attribute) {

    public boolean matches(ItemStack stack) {
        return input.test(stack) && stack.getCount() >= count;
    }

    @Nullable
    public static Trade deserialize(JsonObject jsonObject) {
        if (jsonObject.size() == 0) {
            return null;
        }
        JsonObject input = GsonHelper.getAsJsonObject(jsonObject,"input");
        Ingredient ingredient = Ingredient.fromJson(GsonHelper.getNonNull(input,"ingredient"));
        int count = GsonHelper.getAsInt(input, "count", 1);

        ItemStack stack = Utils.getItemStack(GsonHelper.getAsJsonObject(jsonObject,"output"), true);
        Attribute attribute1 = jsonObject.has("attribute") ? Utils.getAttribute(jsonObject.get("attribute").getAsString()) : null;
        return new Trade(ingredient, count, stack,attribute1);
    }
}
