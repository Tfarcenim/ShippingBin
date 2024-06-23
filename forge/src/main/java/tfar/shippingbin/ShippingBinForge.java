package tfar.shippingbin;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.shippingbin.client.ModClientForge;
import tfar.shippingbin.datagen.ModDatagen;
import tfar.shippingbin.init.ModAttributes;
import tfar.shippingbin.trades.TradeManager;

import java.util.*;
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
        bus.addListener(this::addAttributes);
        MinecraftForge.EVENT_BUS.addListener(this::reloadListener);
        MinecraftForge.EVENT_BUS.addListener(this::serverTick);
        MinecraftForge.EVENT_BUS.addListener(this::onSleep);

        if (FMLEnvironment.dist.isClient()) {
            ModClientForge.init(bus);
        }

        // Use Forge to bootstrap the Common mod.
        ShippingBin.init();

    }

    private void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.SELL_MULTIPLIER);
        event.add(EntityType.PLAYER, ModAttributes.CROP_SELL_MULTIPLIER);
        event.add(EntityType.PLAYER, ModAttributes.MEAT_SELL_MULTIPLIER);
        event.add(EntityType.PLAYER, ModAttributes.GEM_SELL_MULTIPLIER);
        event.add(EntityType.PLAYER, ModAttributes.WOOD_SELL_MULTIPLIER);
    }

    private void onSleep(SleepFinishedTimeEvent event) {
        long newTime = event.getNewTime();
        Level level = (Level) event.getLevel();
        ShippingBin.onSleep(level,newTime);
    }

    private void serverTick(TickEvent.ServerTickEvent event) {
        MinecraftServer server = event.getServer();
        if (event.phase == TickEvent.Phase.START && server.overworld().getDayTime() % 24000 == 18000) {
            ShippingBin.sellItems(server);
        }
    }

    private void reloadListener(AddReloadListenerEvent event) {
        TradeManager tradeManager = new TradeManager();
        ShippingBin.setTradeManager(tradeManager);
        event.addListener(tradeManager);
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