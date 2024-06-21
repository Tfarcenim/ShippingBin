package tfar.shippingbin.trades;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;

public class Trade {

    private final Ingredient input;
    private final ItemStack output;

    public Trade(Ingredient input, ItemStack output) {

        this.input = input;
        this.output = output;
    }

    public static Trade deserialize(JsonObject jsonObject) {
        Ingredient ingredient = Ingredient.fromJson(jsonObject.getAsJsonObject("input"));
        ItemStack stack = getItemStack(jsonObject.getAsJsonObject("output"),true);
        return new Trade(ingredient, stack);
    }

    public static ItemStack getItemStack(JsonObject json, boolean readNBT) {
        String itemName = GsonHelper.getAsString(json, "item");
        Item item = getItem(itemName);
        if (readNBT && json.has("nbt"))
        {
            CompoundTag nbt = getNBT(json.get("nbt"));
            CompoundTag tmp = new CompoundTag();
            if (nbt.contains("ForgeCaps"))
            {
                tmp.put("ForgeCaps", nbt.get("ForgeCaps"));
                nbt.remove("ForgeCaps");
            }

            tmp.put("tag", nbt);
            tmp.putString("id", itemName);
            tmp.putInt("Count", GsonHelper.getAsInt(json, "count", 1));

            return ItemStack.of(tmp);
        }

        return new ItemStack(item, GsonHelper.getAsInt(json, "count", 1));
    }

    public static Item getItem(String itemName)
    {
        ResourceLocation itemKey = new ResourceLocation(itemName);
        if (!BuiltInRegistries.ITEM.containsKey(itemKey))
            throw new JsonSyntaxException("Unknown item '" + itemName + "'");

        Item item = BuiltInRegistries.ITEM.get(itemKey);
        if (item == Items.AIR)
            throw new JsonSyntaxException("Invalid item: " + itemName);
        return Objects.requireNonNull(item);
    }

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();


    public static CompoundTag getNBT(JsonElement element)
    {
        try
        {
            if (element.isJsonObject())
                return TagParser.parseTag(GSON.toJson(element));
            else
                return TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));
        }
        catch (CommandSyntaxException e)
        {
            throw new JsonSyntaxException("Invalid NBT Entry: " + e);
        }
    }

}
