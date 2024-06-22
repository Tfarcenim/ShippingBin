package tfar.shippingbin.datagen.data;

import com.google.common.collect.Sets;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.init.ModAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TradeProvider implements DataProvider {

    protected final PackOutput.PathProvider tradePathProvider;


    public TradeProvider(PackOutput pOutput) {
        this.tradePathProvider = pOutput.createPathProvider(PackOutput.Target.DATA_PACK, ShippingBin.FOLDER);
    }

    public CompletableFuture<?> run(CachedOutput pOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();
        this.buildTrades((trade) -> {
            if (!set.add(trade.getId())) {
                throw new IllegalStateException("Duplicate trade " + trade.getId());
            } else {
                list.add(DataProvider.saveStable(pOutput, trade.serializeTrade(), this.tradePathProvider.json(trade.getId())));
            }
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    protected void buildTrades(Consumer<FinishedTrade> consumer) {
        TradeBuilder.builder(Items.DIAMOND,Items.DIRT).save(consumer);
        TradeBuilder.builderWithCount(Items.GOLD_INGOT,Items.IRON_INGOT,2).save(consumer);
        TradeBuilder.builderWithCount(Items.COBBLESTONE, ItemTags.PLANKS,4).setAttribute(Attributes.ATTACK_DAMAGE).save(consumer);
    }

        @Override
    public String getName() {
        return "Trades";
    }
}
