package tfar.shippingbin.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.ShippingBinForge;
import tfar.shippingbin.blockentity.ShippingBinBlockEntity;
import tfar.shippingbin.blockentity.ShippingBinBlockEntityForge;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.inventory.ForgeHandler;
import tfar.shippingbin.network.PacketHandlerForge;
import tfar.shippingbin.network.client.S2CModPacket;
import tfar.shippingbin.network.server.C2SModPacket;
import tfar.shippingbin.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
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
    public <F> void registerAll(Class<?> clazz, Registry<? super F> registry, Class<? super F> filter) {
        List<Pair<ResourceLocation, Supplier<?>>> list = ShippingBinForge.registerLater.computeIfAbsent(registry, k -> new ArrayList<>());
        for (Field field : clazz.getFields()) {
            MappedRegistry<?> mappedRegistry = (MappedRegistry<?>) registry;
            mappedRegistry.unfreeze();
            try {
                Object o = field.get(null);
                if (filter.isInstance(o)) {
                    list.add(Pair.of(ShippingBin.id(field.getName().toLowerCase(Locale.ROOT)), () -> o));
                }
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
    }

    @Override
    public <H extends CommonHandler> H makeDummy(int slots) {
        return (H) new ForgeHandler(slots);
    }

    @Override
   public <H extends CommonHandler> ShippingBinBlockEntity<H> blockEntity(BlockEntityType<ShippingBinBlockEntity<?>> type, BlockPos pos, BlockState state) {
        return (ShippingBinBlockEntity<H>) new ShippingBinBlockEntityForge(type, pos, state);
    }

    int i;

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapS2C());
    }

}