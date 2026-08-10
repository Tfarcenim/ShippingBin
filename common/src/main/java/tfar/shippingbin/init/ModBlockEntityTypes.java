package tfar.shippingbin.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.blockentity.ShippingBinBlockEntity;

public class ModBlockEntityTypes {

    public static final BlockEntityType<ShippingBinBlockEntity<?>> SHIPPING_BIN = BlockEntityType.Builder.of(ShippingBinBlockEntity.shippingBin(),ModBlocks.SHIPPING_BIN).build(null);

    static {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ShippingBin.id("shipping_bin"), SHIPPING_BIN);
    }

    public static void init() {}
}
