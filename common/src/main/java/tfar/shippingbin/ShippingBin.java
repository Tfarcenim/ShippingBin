package tfar.shippingbin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.shippingbin.client.ModClient;
import tfar.shippingbin.init.*;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.level.ShippingBinInventories;
import tfar.shippingbin.network.PacketHandler;
import tfar.shippingbin.platform.Services;
import tfar.shippingbin.trades.Trade;
import tfar.shippingbin.trades.TradeManager;
import tfar.shippingbin.trades.TradeMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class ShippingBin {

    public static final String MOD_ID = "shippingbin";
    public static final String MOD_NAME = "ShippingBin";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final String FOLDER = "trades";

    private static TradeManager serverTradeManager;

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        Services.PLATFORM.registerAll(ModBlocks.class, BuiltInRegistries.BLOCK, Block.class);
        Services.PLATFORM.registerAll(ModBlockEntityTypes.class, BuiltInRegistries.BLOCK_ENTITY_TYPE, BlockEntityType.class);
        Services.PLATFORM.registerAll(ModMenuTypes.class,BuiltInRegistries.MENU, MenuType.class);
        Services.PLATFORM.registerAll(ModItems.class,BuiltInRegistries.ITEM, Item.class);
        Services.PLATFORM.registerAll(ModAttributes.class,BuiltInRegistries.ATTRIBUTE, Attribute.class);
        Services.PLATFORM.registerAll(ModSounds.class,BuiltInRegistries.SOUND_EVENT, SoundEvent.class);

        PacketHandler.registerPackets();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID,path);
    }

    public static void onSleep(Level level, long newTime) {
        long oldTime = level.getDayTime();
        long oldDay = oldTime / 24000L;
        long newDay = newTime / 24000L;
        if (newDay > oldDay) {
            sellItems(level.getServer());
        }
    }

    public static void sellItems(MinecraftServer server) {
        ShippingBinInventories shippingBinInventories = ShippingBinInventories.getOrCreateInstance(server);
        for (Map.Entry<UUID, Pair<CommonHandler,CommonHandler>> entry : shippingBinInventories.getHandlerMap().entrySet()) {
            Pair<CommonHandler,CommonHandler> invs = entry.getValue();
            CommonHandler input = invs.getKey();
            CommonHandler output = invs.getValue();

            UUID uuid = entry.getKey();

            ServerPlayer player = server.getPlayerList().getPlayer(uuid);

            double multiplier =(player != null) ? player.getAttribute(ModAttributes.SELL_MULTIPLIER).getValue() :1;

            TradeMatcher tradeMatcher = new TradeMatcher();

            for (int i = 0; i < input.$getSlotCount();i++) {
                ItemStack stack = input.$getStack(i);
                if (!stack.isEmpty()) {
                    tradeMatcher.account(stack.copy());
                }
            }


            Map<ResourceLocation,Integer> counts = new HashMap<>();



            for (Map.Entry<ResourceLocation, Trade> tradeEntry : serverTradeManager.getTrades().entrySet()) {
                ResourceLocation resourceLocation = tradeEntry.getKey();
                Trade trade = tradeEntry.getValue();
                int countTrades = tradeMatcher.countTrades(trade,false);
                if (countTrades > 0) {
                    counts.put(resourceLocation,countTrades);
                }
            }

            tradeMatcher.trySellItems(input,output,counts, serverTradeManager.getTrades(),player,multiplier);

        }
    }

    public static TradeManager getTradeManager() {
        if (serverTradeManager == null) {
            return ModClient.getTradeManager();
        } else {
            return serverTradeManager;
        }
    }

    public static void setTradeManager(TradeManager tradeManager) {
        serverTradeManager = tradeManager;
    }

}