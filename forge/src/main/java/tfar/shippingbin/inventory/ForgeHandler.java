package tfar.shippingbin.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ForgeHandler extends ItemStackHandler implements CommonHandler {

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
}
