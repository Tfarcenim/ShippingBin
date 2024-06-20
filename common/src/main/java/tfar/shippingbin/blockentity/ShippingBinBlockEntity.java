package tfar.shippingbin.blockentity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import tfar.shippingbin.init.ModBlockEntityTypes;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.level.ShippingBinInventories;
import tfar.shippingbin.menu.ShippingBinMenu;
import tfar.shippingbin.platform.Services;

import java.util.UUID;

public class ShippingBinBlockEntity<H extends CommonHandler> extends BlockEntity implements MenuProvider {

    protected UUID owner = Util.NIL_UUID;

    public ShippingBinBlockEntity(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    public ShippingBinBlockEntity(BlockPos $$1, BlockState $$2) {
        super(ModBlockEntityTypes.SHIPPING_BIN, $$1, $$2);
    }

    public static BlockEntityType.BlockEntitySupplier<ShippingBinBlockEntity<?>> shippingBin() {
        return (pPos, pState) -> Services.PLATFORM.blockEntity(ModBlockEntityTypes.SHIPPING_BIN,pPos,pState);
    }

    public Pair<H,H> getServerInventory() {
        if (this.level.isClientSide) {
            return Pair.of(CommonHandler.create(CommonHandler.SLOTS),CommonHandler.create(CommonHandler.SLOTS));
        } else return (Pair<H, H>) ShippingBinInventories.getOrCreateInstance(this.level.getServer()).getInventory(owner);
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

    @Override
    public Component getDisplayName() {
        return Component.literal("Shipping Bin");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        Pair<H,H> handlers = getServerInventory();
        return new ShippingBinMenu<>(id,inventory,handlers.getKey(),handlers.getValue());
    }
}
