package tfar.shippingbin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.shippingbin.client.ModClient;
import tfar.shippingbin.init.*;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.level.ShippingBinInventories;
import tfar.shippingbin.network.PacketHandler;
import tfar.shippingbin.network.client.S2CCompletedTradesPacket;
import tfar.shippingbin.platform.Services;
import tfar.shippingbin.trades.CompletedTrade;
import tfar.shippingbin.trades.Trade;
import tfar.shippingbin.trades.TradeManager;

import java.util.*;

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
        long nextTime = getNextSellTime(oldTime);

        if (nextTime< newTime) {
            sellItems(level.getServer());
        }
    }

    static long getNextSellTime(long oldTime) {
        long period = ShippingBinConfig.Server.SELLING_INTERVAL.get();
        long offset = ShippingBinConfig.Server.SELLING_INTERVAL_OFFSET.get();
        long time = offset;
        while (time < oldTime) {
            time += period;
        }
        return time;
    }

    public static void sellItems(MinecraftServer server) {
        ShippingBinInventories shippingBinInventories = ShippingBinInventories.getOrCreateInstance(server);
        for (Map.Entry<UUID, Pair<CommonHandler,CommonHandler>> entry : shippingBinInventories.getHandlerMap().entrySet()) {
            Pair<CommonHandler, CommonHandler> invs = entry.getValue();
            CommonHandler inputInv = invs.getKey();
            CommonHandler outputInv = invs.getValue();

            UUID uuid = entry.getKey();

            ServerPlayer player = server.getPlayerList().getPlayer(uuid);

            double baseMultiplier = player != null ? player.getAttribute(ModAttributes.SELL_MULTIPLIER).getValue() : 1;


            List<CompletedTrade> completedTrades = new ArrayList<>();

            TradeManager tradeManager = serverTradeManager;
            Map<ResourceLocation,Integer> tradeCount = new HashMap<>();

            for (Map.Entry<ResourceLocation, Trade> entry1 : tradeManager.getTrades().entrySet()) {
                if (inputInv.isEmpty()) break;

                Trade trade = entry1.getValue();
                int loops = 0;
                while (loops < 1728) {

                    if (hasTradeInputs(inputInv,trade)) {
                        ItemStack tradeOutput = trade.output();
                        @Nullable Attribute tradeAttribute = trade.attribute();

                        double attributeMultiplier = player != null && tradeAttribute != null  && player.getAttribute(tradeAttribute) != null ?
                                player.getAttribute(tradeAttribute).getValue() : 1;
                        double totalMultiplier = attributeMultiplier * baseMultiplier;
                        ItemStack actualOutput = tradeOutput.copyWithCount((int) (tradeOutput.getCount() *totalMultiplier));

                        if (canOutputFit(outputInv,actualOutput)) {
                            takeTradeInputs(inputInv, trade);
                            putTradeOutputs(outputInv,actualOutput);
                            tradeCount.put(entry1.getKey(),1 + tradeCount.getOrDefault(entry1.getKey(), 0));
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                    loops++;
                }
            }

            if (player != null && !tradeCount.isEmpty()) {
                for (Map.Entry<ResourceLocation,Integer> entry1 : tradeCount.entrySet()) {
                    ResourceLocation resourceLocation = entry1.getKey();
                    Trade trade = tradeManager.getTrades().get(resourceLocation);
                    ItemStack tradeOutput = trade.output();
                    ItemStack[] soldItems = trade.input().getItems();
                    completedTrades.add(new CompletedTrade(
                            //"Sold %s %s for %s %s"
                            Component.translatable("shippingbin.toast.trade",
                                    entry1.getValue() * trade.count(),soldItems.length == 0 ?
                                            ItemStack.EMPTY :soldItems[0].copyWithCount(1).getHoverName(),
                                    tradeOutput.getCount(), tradeOutput.getHoverName()),
                            soldItems.length == 0 ? ItemStack.EMPTY :soldItems[0].copyWithCount(1)));
                }
                Services.PLATFORM.sendToClient(new S2CCompletedTradesPacket(completedTrades),player);
            }

            LOG.info(tradeCount.toString());
        }
    }


    public static boolean canOutputFit(CommonHandler commonHandler,ItemStack stack) {
        return commonHandler.$slotlessInsertStack(stack,stack.getCount(),true).isEmpty();
    }

    static void putTradeOutputs(CommonHandler commonHandler,ItemStack stack) {
        commonHandler.$slotlessInsertStack(stack,stack.getCount(),false);
    }

    static List<ItemStack> takeTradeInputs(CommonHandler input,Trade trade) {
        List<ItemStack> takenStacks =  new ArrayList<>();
        Ingredient requiredInput = trade.input();
        int remainingCount = trade.count();
        for (int i = 0; i <= input.$getSlotCount(); i++) {
            ItemStack stack = input.$getStack(i);
            if (requiredInput.test(stack)) {
                int toTake = Math.min(stack.getCount(), remainingCount);
                remainingCount -= toTake;
                List<ItemStack> itemStacks = input.$slotlessExtractStack(requiredInput, toTake, false);
                takenStacks.addAll(itemStacks);
                if (remainingCount == 0) {
                    break;
                }
            }
        }
        return takenStacks;
    }


    static boolean hasTradeInputs(CommonHandler input,Trade trade) {
        Ingredient requiredInput = trade.input();
        int requiredCount = trade.count();
        int totalCount = 0;
        for (int i = 0; i <= input.$getSlotCount(); i++) {
            ItemStack stack = input.$getStack(i);
            if (requiredInput.test(stack)) {
                totalCount += stack.getCount();
                if (totalCount >= requiredCount) {
                    return true;
                }
            }
        }
        return false;
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