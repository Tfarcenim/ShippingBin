package tfar.shippingbin.datagen;

import com.google.gson.JsonElement;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import tfar.shippingbin.init.ModBlocks;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModBlockModelProvider extends BlockModelGenerators {

    public ModBlockModelProvider(Consumer<BlockStateGenerator> pBlockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> pModelOutput, Consumer<Item> pSkippedAutoModelsOutput) {
        super(pBlockStateOutput, pModelOutput, pSkippedAutoModelsOutput);
    }

    @Override
    public void run() {
       // createNonTemplateModelBlock(ModBlocks.SHIPPING_BIN, Blocks.COMPOSTER);
        //this.createNonTemplateModelBlock(ModBlocks.MINEABLE_LAVA,Blocks.LAVA);
     //   this.createTrivialBlock(ModBlocks.LAVA_TNT, TexturedModel.CUBE_TOP_BOTTOM);

    }
}
