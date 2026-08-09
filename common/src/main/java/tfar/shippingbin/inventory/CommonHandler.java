package tfar.shippingbin.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import tfar.shippingbin.platform.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface CommonHandler {
    static int SLOTS = 27;

    boolean isEmpty();
    int $getSlotCount();
    ItemStack $getStack(int slot);
    void $setStack(int slot,ItemStack stack);
    CompoundTag $serialize();
    void $deserialize(CompoundTag invTag);
    Slot addInvSlot(int slot, int x, int y);
    int $getMaxStackSize(int slot);
    ItemStack $insertStack(int slot, @NotNull ItemStack stack, boolean simulate);
    default ItemStack $slotlessInsertStack(@NotNull ItemStack stack,int amount, boolean simulate){
        if (amount<= 0) return stack;
        if (!$isValid(stack))return stack;
        ItemStack copy = stack.copy();
        ItemStack split = copy.split(amount);
        for (int i = 0; i < $getSlotCount();i++) {
            split = $insertStack(i,split,simulate);
            if (split.isEmpty()) break;
        }
        if (!copy.isEmpty()) {
            copy.grow(split.getCount());
            return copy;
        } else {
            return split;
        }
    }

    ItemStack $extractStack(int slot, int amount, boolean simulate);
    default List<ItemStack> $slotlessExtractStack(Ingredient ingredient, int amount, boolean simulate){
        if (amount<= 0) return List.of();
        List<ItemStack> stacks = new ArrayList<>();
        int remainder = amount;
        for (int i = 0; i < $getSlotCount();i++) {
            ItemStack stack = $getStack(i);
            if (stack.isEmpty() || !ingredient.test(stack)) continue;
            ItemStack extract = $extractStack(i,remainder,simulate);
            if (extract.isEmpty()) continue;
            stacks.add(extract);
            remainder -= extract.getCount();
            if (remainder <= 0) break;
        }
        return stacks;
    }



    boolean $isValid(ItemStack stack);
    void $setInputPredicate(Predicate<ItemStack> predicate);

    default CompoundTag serializeNoAir() {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < $getSlotCount(); i++) {
            ItemStack stack = $getStack(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                stack.save(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        return nbt;
    }

     static <H extends CommonHandler> H create(int i) {
         return Services.PLATFORM.makeDummy(i);
     }
}
