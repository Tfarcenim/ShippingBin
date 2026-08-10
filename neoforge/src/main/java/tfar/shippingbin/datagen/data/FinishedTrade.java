package tfar.shippingbin.datagen.data;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface FinishedTrade {
    void serializeRecipeData(JsonObject pJson);

    /**
     * Gets the JSON for the trade.
     */
    default JsonObject serializeTrade() {
        JsonObject jsonobject = new JsonObject();
        this.serializeRecipeData(jsonobject);
        return jsonobject;
    }

    /**
     * Gets the ID for the recipe.
     */
    ResourceLocation getId();
}
