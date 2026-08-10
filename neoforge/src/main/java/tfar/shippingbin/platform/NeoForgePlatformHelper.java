package tfar.shippingbin.platform;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.blockentity.ShippingBinBlockEntity;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.inventory.NeoForgeHandler;
import tfar.shippingbin.level.ShippingBinInventories;
import tfar.shippingbin.network.client.S2CModPacket;
import tfar.shippingbin.platform.services.IPlatformHelper;

import java.util.Objects;
import java.util.function.Predicate;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }


    @Override
    public <H extends CommonHandler> H makeDummy(int slots) {
        return (H) new NeoForgeHandler(slots);
    }

    public static PayloadRegistrar registrar;

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(CustomPacketPayload.Type<MSG> packetLocation, StreamCodec<RegistryFriendlyByteBuf, MSG> reader) {
        registrar.playToClient(packetLocation, reader,(payload, context) -> payload.handleClient());
    }

    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player,msg);
    }

    @Override
    public NeoForgeShippingBinWrapper
    createExternalWrapper(ShippingBinBlockEntity shippingBinBlockEntity) {
        return new NeoForgeShippingBinWrapper(shippingBinBlockEntity);
    }

    public static final class NeoForgeShippingBinWrapper extends ShippingBinWrapper implements
                 IItemHandlerModifiable {

        public NeoForgeShippingBinWrapper(ShippingBinBlockEntity shippingBinBlockEntity,
                                          NeoForgeHandler input, NeoForgeHandler output) {
            super(shippingBinBlockEntity, input, output);
        }

        public NeoForgeShippingBinWrapper(ShippingBinBlockEntity shippingBinBlockEntity) {
            this(shippingBinBlockEntity, (NeoForgeHandler) shippingBinBlockEntity.getServerInventory().getKey(),
                    (NeoForgeHandler) shippingBinBlockEntity.getServerInventory().getValue());
        }

        //forge required
        @Override
        public int getSlots() {
            return $getSlotCount();
        }

        @Override
        public int getSlotLimit(int slot) {
            return $getMaxStackSize(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return $isValid(stack);
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return $getStack(slot);
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            $setStack(slot, stack);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return $insertStack(slot, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return $extractStack(slot, amount, simulate);
        }
    }
}