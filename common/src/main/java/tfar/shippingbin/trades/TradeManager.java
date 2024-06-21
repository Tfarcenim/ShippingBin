package tfar.shippingbin.trades;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.*;

public class TradeManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogUtils.getLogger();
    private Map<ResourceLocation, Trade> trades;
    private boolean hasErrors;

    public TradeManager() {
        super(GSON, "trades");
        this.trades = ImmutableMap.of();
    }

    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller $$2) {
        this.hasErrors = false;
        ImmutableMap.Builder<ResourceLocation, Trade> builder = ImmutableMap.builder();

        for (Map.Entry<ResourceLocation, JsonElement> resourceLocationJsonElementEntry : map.entrySet()) {
            Map.Entry<ResourceLocation, JsonElement> $$5 = resourceLocationJsonElementEntry;
            ResourceLocation $$6 = $$5.getKey();

            try {
                Trade $$7 = fromJson($$6, GsonHelper.convertToJsonObject($$5.getValue(), "top element"));
                builder.put($$6, $$7);
            } catch (IllegalArgumentException | JsonParseException var10) {
                RuntimeException $$8 = var10;
                LOGGER.error("Parsing error loading trade {}", $$6, $$8);
            }
        }
        this.trades = builder.build();
        LOGGER.info("Loaded {} trades", trades.size());
    }

    public boolean hadErrorsLoading() {
        return this.hasErrors;
    }

    public static Trade fromJson(ResourceLocation id, JsonObject jsonObject) {
        return Trade.deserialize(jsonObject);
    }

}