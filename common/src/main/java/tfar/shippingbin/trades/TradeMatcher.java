package tfar.shippingbin.trades;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.network.client.S2CCompletedTradesPacket;
import tfar.shippingbin.platform.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TradeMatcher {

    public List<ItemStack> inputItems = new ArrayList<>();

    public void account(ItemStack stack) {
        for (ItemStack ex : inputItems)  {
            if (ItemStack.isSameItemSameTags(ex,stack)) {
                ex.grow(stack.getCount());
                return;
            }
        }
        inputItems.add(stack);
    }

    public int countTrades(Trade trade,boolean consume) {
        List<ItemStack> copy = deepCopy(inputItems);
        int c = 0;
        for (ItemStack stack : copy) {
            if (trade.matches(stack)) {
                while (trade.matches(stack)) {
                    c++;
                    stack.shrink(trade.count());
                }
            }
        }
        return c;
    }

    public boolean canOutputFit(CommonHandler commonHandler,ItemStack stack) {
        return commonHandler.$slotlessInsertStack(stack,stack.getCount(),true).isEmpty();
    }

    public void trySellItems(CommonHandler input, CommonHandler output, Map<ResourceLocation, Integer> counts, Map<ResourceLocation, Trade> trades, @Nullable ServerPlayer player, double multiplier) {
        List<CompletedTrade> completedTrades = new ArrayList<>();
        for (Map.Entry<ResourceLocation,Integer> entry : counts.entrySet()) {
            Trade trade = trades.get(entry.getKey());
            int tradeCount = entry.getValue();
            if (trade != null) {
                ItemStack tradeOutput = getTradeOutput(trade,tradeCount);

                applyMultipliers(tradeOutput,player,multiplier,trade.attribute());

                if (!tradeOutput.isEmpty() && canOutputFit(output,tradeOutput)) {

                    List<ItemStack> soldItems = removeMatchingItems(input, trade.input(), trade.count() * tradeCount);

                    MutableComponent listComponent = Component.empty();
                    boolean isFirst = true;
                    for (ItemStack stack : soldItems) {
                        if (!isFirst) {
                            listComponent.append(",");
                        }
                        listComponent.append(stack.getHoverName());
                        isFirst = false;
                    }

                        completedTrades.add(new CompletedTrade(
                                Component.translatable("shippingbin.toast.trade", tradeCount * trade.count(), listComponent, tradeOutput.getCount(), tradeOutput.getHoverName()), soldItems.isEmpty() ? ItemStack.EMPTY :soldItems.get(0).copyWithCount(1)));

                    output.$slotlessInsertStack(tradeOutput,tradeOutput.getCount(),false);
                }
            }
        }
        if (player != null && !completedTrades.isEmpty()) {
            Services.PLATFORM.sendToClient(new S2CCompletedTradesPacket(completedTrades),player);
        }
    }

    void applyMultipliers(ItemStack stack, @Nullable Player player, double multiplier,@Nullable Attribute attribute) {
        double attributeMultiplier = player != null && attribute != null  && player.getAttribute(attribute) != null  ? player.getAttribute(attribute).getValue() : 1;
        double totalMultiplier = attributeMultiplier * multiplier;
        stack.setCount((int) (stack.getCount() *totalMultiplier));
    }

    public static List<ItemStack> removeMatchingItems(CommonHandler handler, Ingredient ingredient,int count) {
        int remaining = count;
        List<ItemStack> removed = new ArrayList<>();
        for (int i = handler.$getSlotCount() - 1 ; i > -1;i--) {
            ItemStack stack = handler.$getStack(i);
            if (ingredient.test(stack)) {
                if (remaining <= stack.getCount()) {
                    account(stack.copyWithCount(remaining),removed);
                    stack.shrink(remaining);
                    return removed;
                } else {
                    account(stack,removed);
                    remaining -= stack.getCount();
                    handler.$setStack(i,ItemStack.EMPTY);
                }
            }
        }
        return removed;
    }

    public static void account(ItemStack stack,List<ItemStack> inputItems) {
        for (ItemStack ex : inputItems)  {
            if (ItemStack.isSameItemSameTags(ex,stack)) {
                ex.grow(stack.getCount());
                return;
            }
        }
        inputItems.add(stack);
    }


    public ItemStack getTradeOutput(Trade trade,int count) {
        if (count == 0) return ItemStack.EMPTY;
        ItemStack copy = trade.output().copy();
        copy.setCount(copy.getCount() * count);
        return copy;
    }

    static List<ItemStack> deepCopy(List<ItemStack> list) {
        List<ItemStack> copy = new ArrayList<>(list.size());
        for (ItemStack stack : list) {
            copy.add(stack.copy());
        }
        return copy;
    }

}
