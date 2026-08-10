package tfar.shippingbin.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import tfar.shippingbin.init.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(pOutput,lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SHIPPING_BIN)
                .define('t', ItemTags.TRAPDOORS)
                .define('b', net.neoforged.neoforge.common.Tags.Items.BARRELS_WOODEN)
                .pattern("t")
                .pattern("b")
                .unlockedBy("has_barrel",has((Tags.Items.BARRELS_WOODEN)))
                .save(pRecipeOutput);
    }
}
