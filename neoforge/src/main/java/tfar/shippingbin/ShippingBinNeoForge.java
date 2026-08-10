package tfar.shippingbin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.Nullable;
import tfar.shippingbin.blockentity.ShippingBinBlockEntity;
import tfar.shippingbin.client.ModClientNeoForge;
import tfar.shippingbin.datagen.ModDatagen;
import tfar.shippingbin.init.ModAttributes;
import tfar.shippingbin.init.ModBlockEntityTypes;
import tfar.shippingbin.init.ModItems;
import tfar.shippingbin.init.ModMenuTypes;
import tfar.shippingbin.network.PacketHandlerNeoForge;
import tfar.shippingbin.trades.TradeManager;

@Mod(ShippingBin.MOD_ID)
public class ShippingBinNeoForge {

    public ShippingBinNeoForge(IEventBus bus, Dist dist, ModContainer container) {
        container.registerConfig(ModConfig.Type.SERVER,ShippingBinConfig.Server.SPEC);
        container.registerConfig(ModConfig.Type.CLIENT,ShippingBinConfig.Client.SPEC);
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.


        bus.addListener(ModDatagen::gather);
        bus.addListener(this::register);
        bus.addListener(this::setup);
        bus.addListener(this::addAttributes);
        bus.addListener(this::creativeTab);
        bus.addListener(PacketHandlerNeoForge::registerPackets);
        bus.addListener(this::registerCapabilities);
        NeoForge.EVENT_BUS.addListener(this::reloadListener);
        NeoForge.EVENT_BUS.addListener(this::serverTick);
        NeoForge.EVENT_BUS.addListener(this::onSleep);

        if (dist.isClient()) {
            ModClientNeoForge.init(bus);
        }

        // Use Forge to bootstrap the Common mod.
        ShippingBin.init();

    }

    void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntityTypes.SHIPPING_BIN,
                (object, context) -> (IItemHandler)object.getServerInventory());
    }

    private void creativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.SHIPPING_BIN);
        }
    }

    private void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.SELL_MULTIPLIER);
        event.add(EntityType.PLAYER, ModAttributes.CROP_SELL_MULTIPLIER);
        event.add(EntityType.PLAYER, ModAttributes.MEAT_SELL_MULTIPLIER);
        event.add(EntityType.PLAYER, ModAttributes.GEM_SELL_MULTIPLIER);
        event.add(EntityType.PLAYER, ModAttributes.WOOD_SELL_MULTIPLIER);
    }

    private void onSleep(SleepFinishedTimeEvent event) {
        Level level = (Level) event.getLevel();
        long newTime = event.getNewTime();
        ShippingBin.onSleep(level,newTime);
    }

    private void serverTick(ServerTickEvent.Pre event) {
        MinecraftServer server = event.getServer();
        if (server.overworld().getDayTime() % ShippingBinConfig.Server.SELLING_INTERVAL.get() ==
                ShippingBinConfig.Server.SELLING_INTERVAL_OFFSET.get()) {
            ShippingBin.sellItems(server);
        }
    }

    private void reloadListener(AddReloadListenerEvent event) {
        TradeManager tradeManager = new TradeManager(event.getRegistryAccess());
        ShippingBin.setTradeManager(tradeManager);
        event.addListener(tradeManager);
    }

    private void register(RegisterEvent e) {
        ShippingBin.register();
    }

    private void setup(FMLCommonSetupEvent event) {
    }
}