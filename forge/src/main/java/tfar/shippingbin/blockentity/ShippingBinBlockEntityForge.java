package tfar.shippingbin.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.inventory.ForgeHandler;

public class ShippingBinBlockEntityForge extends ShippingBinBlockEntity<ForgeHandler> {
    public ShippingBinBlockEntityForge(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    protected LazyOptional<ShippingBinWrapper> optional = LazyOptional.of(ShippingBinWrapper::new);

    public class ShippingBinWrapper implements IItemHandler {

        private ForgeHandler input;
        private ForgeHandler output;

        public ShippingBinWrapper() {
            Pair<ForgeHandler,ForgeHandler> pair = getServerInventory();
            input = pair.getKey();
            output = pair.getValue();
        }

        @Override
        public int getSlots() {
            return input.$getSlotCount() + output.$getSlotCount();
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            if (slot < input.$getSlotCount()) return input.$getStack(slot);

            if (slot - input.$getSlotCount()< slot) return output.$getStack(slot - input.$getSlotCount());
            warnSlot(slot);
            return ItemStack.EMPTY;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (slot < input.$getSlotCount()) {
                return input.insertItem(slot,stack,simulate);
            }
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot < input.$getSlotCount()) {
                return ItemStack.EMPTY;
            } else if (slot -input.$getSlotCount()< output.$getSlotCount()) {
                    return output.extractItem(slot - input.$getSlotCount(),amount,simulate);
            }
            warnSlot(slot);
            return ItemStack.EMPTY;
        }

        protected void warnSlot(int slot) {
            ShippingBin.LOG.warn("Tried to access out of bounds slot {}", slot);
            new Throwable().printStackTrace();
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot < input.$getSlotCount()) {
                return input.getSlotLimit(slot);
            } else if (slot - input.$getSlotCount()< output.$getSlotCount()) {
                return output.getSlotLimit(slot - input.$getSlotCount());
            }
            return 0;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot < input.$getSlotCount();
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? optional.cast() : super.getCapability(cap, side);
    }

    public ShippingBinBlockEntityForge(BlockPos $$1, BlockState $$2) {
        super($$1, $$2);
    }
}
