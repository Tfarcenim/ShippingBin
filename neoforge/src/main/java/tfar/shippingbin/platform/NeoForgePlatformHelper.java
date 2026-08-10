package tfar.shippingbin.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tfar.shippingbin.blockentity.ShippingBinBlockEntity;
import tfar.shippingbin.blockentity.ShippingBinBlockEntityForge;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.inventory.ForgeHandler;
import tfar.shippingbin.network.PacketHandlerNeoForge;
import tfar.shippingbin.network.client.S2CModPacket;
import tfar.shippingbin.platform.services.IPlatformHelper;

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
        return (H) new ForgeHandler(slots);
    }

    @Override
   public <H extends CommonHandler> ShippingBinBlockEntity<H> blockEntity(BlockEntityType<ShippingBinBlockEntity<?>> type, BlockPos pos, BlockState state) {
        return (ShippingBinBlockEntity<H>) new ShippingBinBlockEntityForge(type, pos, state);
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
}