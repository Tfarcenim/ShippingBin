package tfar.shippingbin.blockentity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import tfar.shippingbin.init.ModBlockEntityTypes;
import tfar.shippingbin.platform.Services;

import java.util.UUID;

public class ShippingBinBlockEntity extends BlockEntity {

    protected UUID owner = Util.NIL_UUID;

    public ShippingBinBlockEntity(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    public ShippingBinBlockEntity(BlockPos $$1, BlockState $$2) {
        super(ModBlockEntityTypes.SHIPPING_BIN, $$1, $$2);
    }

    public static BlockEntityType.BlockEntitySupplier<ShippingBinBlockEntity> shippingBin() {
        return (pPos, pState) -> Services.PLATFORM.blockEntity(ModBlockEntityTypes.SHIPPING_BIN,pPos,pState);
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
        setChanged();
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putUUID("owner",owner);
    }


    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        owner = tag.getUUID("owner");
    }
}
