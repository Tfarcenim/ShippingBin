package tfar.shippingbin.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import tfar.shippingbin.blockentity.ShippingBinBlockEntity;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.network.client.S2CModPacket;

import java.util.function.Function;

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


    <F> void registerAll(Class<?> clazz, Registry<? super F> registry, Class<? super F> filter);
    <H extends CommonHandler> H makeDummy(int slots);
    <H extends CommonHandler> ShippingBinBlockEntity<H> blockEntity(BlockEntityType<ShippingBinBlockEntity<?>> type, BlockPos pos, BlockState state);
    <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf,MSG> reader);


    void sendToClient(S2CModPacket msg, ServerPlayer player);


}