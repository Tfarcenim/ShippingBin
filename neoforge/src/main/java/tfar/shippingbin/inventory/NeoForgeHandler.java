package tfar.shippingbin.inventory;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class NeoForgeHandler extends ItemStackHandler implements CommonHandler {

    protected Predicate<ItemStack> predicate = stack -> true;

    public NeoForgeHandler(int slots) {
        super(slots);
    }

    public Boolean isEmpty;

    @Override
    public int $getSlotCount() {
        return getSlots();
    }

    @Override
    public ItemStack $getStack(int slot) {
        return getStackInSlot(slot);
    }

    @Override
    public void $setStack(int slot, ItemStack stack) {
        isEmpty = null;
        setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack $insertStack(int slot, @NotNull ItemStack stack, boolean simulate) {
        isEmpty = null;
        return insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack $extractStack(int slot, int amount, boolean simulate) {
        isEmpty = null;
        return extractItem(slot, amount, simulate);
    }

    @Override
    public int $getMaxStackSize(int slot) {
        return getSlotLimit(slot);
    }

    @Override
    public boolean $isValid(ItemStack stack) {
        return predicate.test(stack);
    }

    @Override
    public boolean isEmpty() {
        if (isEmpty == null) {
            isEmpty = stacks.stream().allMatch(ItemStack::isEmpty);
        }
        return isEmpty;
    }

    @Override
    public void $setInputPredicate(Predicate<ItemStack> predicate) {
        this.predicate = predicate;
    }
}
