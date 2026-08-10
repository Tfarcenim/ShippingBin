package tfar.shippingbin.datagen.data;

import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.trades.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TradeProvider implements DataProvider {

    protected final PackOutput.PathProvider tradePathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;


    public TradeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        this.tradePathProvider = pOutput.createPathProvider(PackOutput.Target.DATA_PACK, ShippingBin.FOLDER);
        this.registries = pRegistries;
    }

    @Override
    public final CompletableFuture<?> run(CachedOutput pOutput) {
        return this.registries.thenCompose(p_323133_ -> this.run(pOutput, p_323133_));
    }


    public CompletableFuture<?> run(CachedOutput pOutput, HolderLookup.Provider pRegistries) {
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();
        this.buildTrades((trade,resourceLocation) -> {
            if (!set.add(resourceLocation)) {
                throw new IllegalStateException("Duplicate trade " + resourceLocation);
            } else {
                list.add(DataProvider.saveStable(pOutput, pRegistries,Trade.CODEC,trade, this.tradePathProvider.json(resourceLocation)));
            }
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }


    protected void buildTrades(BiConsumer<Trade,ResourceLocation> consumer) {
     //   TradeBuilder.builder(Items.DIAMOND,Items.DIRT).save(consumer);
    //    TradeBuilder.builderWithCount(Items.GOLD_INGOT,Items.IRON_INGOT,2).save(consumer);
     //   TradeBuilder.builderWithCount(Items.COBBLESTONE, ItemTags.PLANKS,4).setAttribute(Attributes.ATTACK_DAMAGE).save(consumer);

    }

        @Override
    public String getName() {
        return "Trades";
    }
}
