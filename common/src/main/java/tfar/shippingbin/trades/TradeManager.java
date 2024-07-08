package tfar.shippingbin.trades;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.platform.Services;

import java.util.*;

public class TradeManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogUtils.getLogger();
    private Map<ResourceLocation, Trade> trades;
    Set<Ingredient> allowedInputs = new HashSet<>();
    private boolean hasErrors;

    public TradeManager() {
        super(GSON, "trades");
        this.trades = ImmutableMap.of();
    }

    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        this.hasErrors = false;
        ImmutableMap.Builder<ResourceLocation, Trade> builder = ImmutableMap.builder();

        for (Map.Entry<ResourceLocation, JsonElement> resourceLocationJsonElementEntry : map.entrySet()) {
            ResourceLocation location = resourceLocationJsonElementEntry.getKey();

            try {
                    Trade trade = fromJson(location, GsonHelper.convertToJsonObject(resourceLocationJsonElementEntry.getValue(), "top element"));
                    if (trade != null) {
                        builder.put(location, trade);
                        allowedInputs.add(trade.input());
                    }
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading trade {}", location, exception);
            }
        }
        this.trades = builder.build();
        LOGGER.info("Loaded {} trades", trades.size());
    }

    public boolean hadErrorsLoading() {
        return this.hasErrors;
    }

    public Map<ResourceLocation, Trade> getTrades() {
        return trades;
    }

    public boolean isInput(ItemStack stack) {
        return allowedInputs.stream().anyMatch(ingredient -> ingredient.test(stack));
    }

    public static Trade fromJson(ResourceLocation id, JsonObject jsonObject) {
        return Trade.deserialize(jsonObject);
    }

}