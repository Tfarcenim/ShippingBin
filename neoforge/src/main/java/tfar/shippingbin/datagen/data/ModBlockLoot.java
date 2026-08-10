package tfar.shippingbin.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import tfar.shippingbin.datagen.ModDatagen;
import tfar.shippingbin.init.ModBlocks;

public class ModBlockLoot extends VanillaBlockLoot {

    public ModBlockLoot(HolderLookup.Provider pRegistries) {
        super(pRegistries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.SHIPPING_BIN);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModDatagen.getKnownBlocks().toList();
    }
}
