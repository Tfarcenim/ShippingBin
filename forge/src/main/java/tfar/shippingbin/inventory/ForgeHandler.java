package tfar.shippingbin.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ForgeHandler extends SortingItemStackHandler implements CommonHandler {

    public ForgeHandler(int slots) {
        super(slots);
    }

    @Override
    public int $getSlotCount() {
        return getSlots();
    }

    @Override
    public ItemStack $getStack(int slot) {
        return getStackInSlot(slot);
    }

    @Override
    public CompoundTag serializeNBT() {
        return serializeNoAir();
    }

    @Override
    public void $setStack(int slot, ItemStack stack) {
        setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack $insertStack(int slot, @NotNull ItemStack stack, boolean simulate) {
        return insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack $extractStack(int slot, int amount, boolean simulate) {
        return extractItem(slot, amount, simulate);
    }

    @Override
    public ItemStack $slotlessInsertStack(@NotNull ItemStack stack, boolean simulate) {
        for (int i = 0; i < $getSlotCount();i++) {
            stack = $insertStack(i,stack,simulate);
            if (stack.isEmpty()) break;
        }
        return stack;
    }

    @Override
    public ItemStack $remove(int slot, int amount) {
        return extractItem(slot,amount,false);
    }

    @Override
    public CompoundTag $serialize() {
        return serializeNBT();
    }

    @Override
    public void $deserialize(CompoundTag invTag) {
        deserializeNBT(invTag);
    }

    @Override
    public Slot addInvSlot(int slot, int x, int y) {
        return new SlotItemHandler(this,slot,x,y);
    }

    @Override
    public int $getMaxStackSize(int slot) {
        return getSlotLimit(slot);
    }
}
