package tfar.shippingbin.datagen.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import tfar.shippingbin.ShippingBin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TradeProvider implements DataProvider {

    protected final PackOutput.PathProvider recipePathProvider;


    public TradeProvider(PackOutput pOutput) {
        this.recipePathProvider = pOutput.createPathProvider(PackOutput.Target.DATA_PACK, ShippingBin.FOLDER);
    }

    public CompletableFuture<?> run(CachedOutput pOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();
        this.buildRecipes((recipe) -> {
            if (!set.add(recipe.getId())) {
                throw new IllegalStateException("Duplicate trade " + recipe.getId());
            } else {
                list.add(DataProvider.saveStable(pOutput, recipe.serializeTrade(), this.recipePathProvider.json(recipe.getId())));
            }
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    protected void buildRecipes(Consumer<FinishedTrade> consumer) {
        TradeBuilder.builder(Items.DIAMOND,Items.DIRT).save(consumer);
        TradeBuilder.builderWithCount(Items.GOLD_INGOT,Items.IRON_INGOT,2);
    }

        @Override
    public String getName() {
        return "Trades";
    }
}
