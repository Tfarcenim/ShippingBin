package tfar.shippingbin.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import tfar.shippingbin.blockentity.ShippingBinBlockEntity;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.network.client.S2CModPacket;
import tfar.shippingbin.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.Function;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <F> void registerAll(Class<?> clazz, Registry<? super F> registry, Class<? super F> filter) {

    }

    @Override
    public <H extends CommonHandler> H makeDummy(int slots) {
        return null;
    }

    @Override
    public <H extends CommonHandler> ShippingBinBlockEntity<H> blockEntity(BlockEntityType<ShippingBinBlockEntity<?>> type, BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {

    }

    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {

    }
}
