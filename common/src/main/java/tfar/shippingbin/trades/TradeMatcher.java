package tfar.shippingbin.trades;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import tfar.shippingbin.inventory.CommonHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TradeMatcher {

    public List<ItemStack> inputItems = new ArrayList<>();
    public List<ItemStack> outputItems = new ArrayList<>();

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
                    stack.shrink(trade.count);
                }
            }
        }
        return c;
    }

    public void fillOutputs(Trade trade,int count) {
        for (ItemStack ex : outputItems)  {
            if (ItemStack.isSameItemSameTags(ex,trade.getOutput())) {
                ex.grow(trade.getOutput().getCount());
                return;
            }
        }
        outputItems.add(trade.getOutput());
    }

    public boolean canOutputFit(CommonHandler commonHandler,ItemStack stack) {
        return commonHandler.$slotlessInsertStack(stack,stack.getCount(),true).isEmpty();
    }

    public void trySellItems(CommonHandler input, CommonHandler output, Map<ResourceLocation, Integer> counts, Map<ResourceLocation, Trade> trades) {
        for (Map.Entry<ResourceLocation,Integer> entry : counts.entrySet()) {
            Trade trade = trades.get(entry.getKey());
            int tradeCount = entry.getValue();
            if (trade != null) {
                ItemStack tradeOutput = getTradeOutput(trade,tradeCount);
                if (canOutputFit(output,tradeOutput)) {
                    removeMatchingItems(input,trade.input,trade.count * tradeCount);
                    output.$slotlessInsertStack(tradeOutput,tradeOutput.getCount(),false);
                }
            }
        }
    }

    public static void removeMatchingItems(CommonHandler handler, Ingredient ingredient,int count) {
        int remaining = count;
        for (int i = handler.$getSlotCount() - 1 ; i > -1;i--) {
            ItemStack stack = handler.$getStack(i);
            if (ingredient.test(stack)) {
                if (remaining <= stack.getCount()) {
                    stack.shrink(remaining);
                    return;
                } else {
                    remaining -= stack.getCount();
                    handler.$setStack(i,ItemStack.EMPTY);
                }
            }
        }
    }


    public ItemStack getTradeOutput(Trade trade,int count) {
        if (count == 0) return ItemStack.EMPTY;
        ItemStack copy = trade.getOutput().copy();
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
