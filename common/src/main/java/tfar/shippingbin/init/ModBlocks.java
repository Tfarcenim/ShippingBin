package tfar.shippingbin.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.block.ShippingBinBlock;
import tfar.shippingbin.blockentity.ShippingBinBlockEntity;

public class ModBlocks {

    public static final Block SHIPPING_BIN = new ShippingBinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(0.6F).sound(SoundType.WOOD).ignitedByLava());

    static {
        Registry.register(BuiltInRegistries.BLOCK, ShippingBin.id("shipping_bin"), SHIPPING_BIN);
    }

    public static void init() {}

}
