package tfar.shippingbin.platform.services;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.blockentity.ShippingBinBlockEntity;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.level.ShippingBinInventories;
import tfar.shippingbin.network.client.S2CModPacket;

import java.util.Objects;
import java.util.function.Predicate;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }


    <H extends CommonHandler> H makeDummy(int slots);

    <MSG extends S2CModPacket> void registerClientPacket(CustomPacketPayload.Type<MSG> packetLocation, StreamCodec<RegistryFriendlyByteBuf,MSG> reader);

    void sendToClient(S2CModPacket msg, ServerPlayer player);

    <H extends CommonHandler> H createExternalWrapper(ShippingBinBlockEntity shippingBinBlockEntity);

    class ShippingBinWrapper implements
            CommonHandler{
        private final ShippingBinBlockEntity shippingBinBlockEntity;
        private final CommonHandler input;
        private final CommonHandler output;

        public ShippingBinWrapper(ShippingBinBlockEntity shippingBinBlockEntity, CommonHandler input,
                                  CommonHandler output) {
            this.shippingBinBlockEntity = shippingBinBlockEntity;
            this.input = input;
            this.output = output;
        }

            public ShippingBinWrapper(ShippingBinBlockEntity shippingBinBlockEntity) {
                this(shippingBinBlockEntity, shippingBinBlockEntity.getServerInventory().getKey(), shippingBinBlockEntity.getServerInventory().getValue());
            }

        @Override
        public ItemStack $getStack(int slot) {
            if (slot < input.$getSlotCount()) return input.$getStack(slot);

            if (slot - input.$getSlotCount() < slot) return output.$getStack(slot - input.$getSlotCount());
            warnSlot(slot);
            return ItemStack.EMPTY;
        }

        @Override
        public void $setStack(int slot, ItemStack stack) {
            if (slot < input.$getSlotCount()) input.$setStack(slot, stack);

            if (slot - input.$getSlotCount() < slot) output.$setStack(slot - input.$getSlotCount(), stack);
            warnSlot(slot);
        }

        @Override
        public int $getSlotCount() {
            return input.$getSlotCount() + output.$getSlotCount();
        }

        @Override
        public int $getMaxStackSize(int slot) {
            if (slot < input.$getSlotCount()) {
                return input.$getMaxStackSize(slot);
            } else if (slot - input.$getSlotCount() < output.$getSlotCount()) {
                return output.$getMaxStackSize(slot - input.$getSlotCount());
            }
            return 0;
        }

        @Override
        public boolean $isValid(ItemStack stack) {
            return ShippingBinInventories.ONLY_INPUTS.test(stack);
        }

        protected void warnSlot(int slot) {
            ShippingBin.LOG.warn("Tried to access out of bounds slot {}", slot);
            new Throwable().printStackTrace();
        }

        @Override
        public boolean isEmpty() {
            return input.isEmpty() && output.isEmpty();
        }

        @Override
        public ItemStack $insertStack(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (slot < input.$getSlotCount() && $isValid(stack)) {
                return input.$insertStack(slot, stack, simulate);
            }
            return stack;
        }

        @Override
        public ItemStack $extractStack(int slot, int amount, boolean simulate) {
            if (slot < input.$getSlotCount()) {
                return ItemStack.EMPTY;
            } else if (slot - input.$getSlotCount() < output.$getSlotCount()) {
                return output.$extractStack(slot - input.$getSlotCount(), amount, simulate);
            }
            warnSlot(slot);
            return ItemStack.EMPTY;
        }

        //these do nothing here

        @Override
        public CompoundTag $serialize(HolderLookup.Provider provider) {
            return null;
        }

        @Override
        public void $deserialize(HolderLookup.Provider provider, CompoundTag invTag) {

        }

        @Override
        public void $setInputPredicate(Predicate<ItemStack> predicate) {

        }

        @Override
        public Slot addInvSlot(int slot, int x, int y) {
            return null;
        }

        ////////////

        public ShippingBinBlockEntity shippingBinBlockEntity() {
            return shippingBinBlockEntity;
        }

        public CommonHandler input() {
            return input;
        }

        public CommonHandler output() {
            return output;
        }
    }
}