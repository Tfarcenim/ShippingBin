package tfar.shippingbin.datagen.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.Tags;
import tfar.shippingbin.init.ModBlocks;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SHIPPING_BIN)
                .define('t', ItemTags.TRAPDOORS)
                .define('b', Tags.Items.BARRELS_WOODEN)
                .pattern("t")
                .pattern("b")
                .unlockedBy("has_barrel",has((Tags.Items.BARRELS_WOODEN)))
                .save(pWriter);
    }
}
