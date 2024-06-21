package tfar.shippingbin;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.shippingbin.client.ModClientForge;
import tfar.shippingbin.datagen.ModDatagen;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.level.ShippingBinInventories;
import tfar.shippingbin.trades.Trade;
import tfar.shippingbin.trades.TradeManager;
import tfar.shippingbin.trades.TradeMatcher;

import java.util.*;
import java.util.function.Supplier;

@Mod(ShippingBin.MOD_ID)
public class ShippingBinForge {

    public static ShippingBinForge instance;

    public ShippingBinForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
        bus.addListener(ModDatagen::gather);
        bus.addListener(this::register);
        bus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::reloadListener);
        MinecraftForge.EVENT_BUS.addListener(this::serverTick);

        if (FMLEnvironment.dist.isClient()) {
            ModClientForge.init(bus);
        }
        instance = this;

        // Use Forge to bootstrap the Common mod.
        ShippingBin.init();

    }

    public TradeManager tradeManager;

    private void serverTick(TickEvent.ServerTickEvent event) {
        MinecraftServer server = event.getServer();
        if (event.phase == TickEvent.Phase.START && server.overworld().getDayTime() % 24000 == 18000) {
            sellItems(server);
        }
    }

    private void sellItems(MinecraftServer server) {
        ShippingBinInventories shippingBinInventories = ShippingBinInventories.getOrCreateInstance(server);
        for (Map.Entry<UUID,Pair<CommonHandler,CommonHandler>> entry : shippingBinInventories.getHandlerMap().entrySet()) {
            Pair<CommonHandler,CommonHandler> invs = entry.getValue();
            CommonHandler input = invs.getKey();
            CommonHandler output = invs.getValue();

            TradeMatcher tradeMatcher = new TradeMatcher();

            for (int i = 0; i < input.$getSlotCount();i++) {
                ItemStack stack = input.$getStack(i);
                if (!stack.isEmpty()) {
                    tradeMatcher.account(stack);
                }
            }


            Map<ResourceLocation,Integer> counts = new HashMap<>();



            for (Map.Entry<ResourceLocation, Trade> tradeEntry : tradeManager.getTrades().entrySet()) {
                ResourceLocation resourceLocation = tradeEntry.getKey();
                Trade trade = tradeEntry.getValue();
                int countTrades = tradeMatcher.countTrades(trade,false);
                if (countTrades > 0) {
                    tradeMatcher.fillOutputs(trade,countTrades);
                    counts.put(resourceLocation,countTrades);
                }
            }

            tradeMatcher.trySellItems(input,output,counts,tradeManager.getTrades());

        }
    }

    private void reloadListener(AddReloadListenerEvent event) {
        event.addListener(tradeManager = new TradeManager());
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