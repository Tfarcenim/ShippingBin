package tfar.shippingbin.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import tfar.shippingbin.platform.Services;

public interface CommonHandler {
    static int SLOTS = 54;
    int $getSlotCount();
    ItemStack $getStack(int slot);
    void $setStack(int slot,ItemStack stack);
    CompoundTag $serialize();
    void $deserialize(CompoundTag invTag);
    Slot addInvSlot(int slot, int x, int y);
    int $getMaxStackSize(int slot);
    ItemStack $remove(int slot,int amount);

    ItemStack $insertStack(int slot, @NotNull ItemStack stack, boolean simulate);
    ItemStack $slotlessInsertStack(@NotNull ItemStack stack, boolean simulate);
    ItemStack $extractStack(int slot, int amount, boolean simulate);

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
