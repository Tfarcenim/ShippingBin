package tfar.shippingbin.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfar.shippingbin.platform.Services;

public interface CommonHandler {
    static int SLOTS = 54;
    int $getSlotCount();
    ItemStack $getStack(int slot);
    CompoundTag $serialize();
    void $deserialize(CompoundTag invTag);
    Slot addInvSlot(int slot, int x, int y);

     static <H extends CommonHandler> H create(int i) {
         return Services.PLATFORM.makeDummy(i);
     }
}
