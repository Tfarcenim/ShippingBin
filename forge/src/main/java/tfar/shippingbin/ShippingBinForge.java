package tfar.shippingbin;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.shippingbin.client.ModClientForge;
import tfar.shippingbin.datagen.ModDatagen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod(ShippingBin.MOD_ID)
public class ShippingBinForge {
    
    public ShippingBinForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
        bus.addListener(ModDatagen::gather);
        bus.addListener(this::register);
        bus.addListener(this::setup);

        if (FMLEnvironment.dist.isClient()) {
            ModClientForge.init(bus);
        }

        // Use Forge to bootstrap the Common mod.
        ShippingBin.init();
        
    }

    public static Map<Registry<?>, List<Pair<ResourceLocation, Supplier<?>>>> registerLater = new HashMap<>();
    private void register(RegisterEvent e) {
        for (Map.Entry<Registry<?>,List<Pair<ResourceLocation, Supplier<?>>>> entry : registerLater.entrySet()) {
            Registry<?> registry = entry.getKey();
            List<Pair<ResourceLocation, Supplier<?>>> toRegister = entry.getValue();
            for (Pair<ResourceLocation,Supplier<?>> pair : toRegister) {
                e.register((ResourceKey<? extends Registry<Object>>)registry.key(),pair.getLeft(),(Supplier<Object>)pair.getValue());
            }
        }
    }

    private void setup(FMLCommonSetupEvent event) {
        registerLater.clear();
    }
}