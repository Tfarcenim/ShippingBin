package tfar.shippingbin.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import tfar.shippingbin.blockentity.ShippingBinBlockEntity;

public class ModBlockEntityTypes {

    public static final BlockEntityType<ShippingBinBlockEntity<?>> SHIPPING_BIN = BlockEntityType.Builder.of(ShippingBinBlockEntity.shippingBin(),ModBlocks.SHIPPING_BIN).build(null);

}
