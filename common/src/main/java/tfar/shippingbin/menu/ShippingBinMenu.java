package tfar.shippingbin.menu;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import tfar.shippingbin.init.ModMenuTypes;
import tfar.shippingbin.init.ModSounds;
import tfar.shippingbin.inventory.CommonHandler;

import java.util.Optional;
import java.util.function.Predicate;

public class ShippingBinMenu<H extends CommonHandler> extends AbstractContainerMenu {


    private final H inputWrapper;
    private final H outputWrapper;
    public ShippingBinMenu(int id, Inventory inventory) {
        this(id, inventory, CommonHandler.create(CommonHandler.SLOTS), CommonHandler.create(CommonHandler.SLOTS));
    }

    public ShippingBinMenu(int id, Inventory inventory, H input, H output) {
        this(ModMenuTypes.SHIPPING_BIN, id, inventory, input, output);
    }


    public class HandlerWrapper implements CommonHandler {

        protected final H wrapped;

        public HandlerWrapper(H wrapped) {

            this.wrapped = wrapped;
        }

        @Override
        public int $getSlotCount() {
            return wrapped.$getSlotCount();
        }

        @Override
        public ItemStack $getStack(int slot) {
            return wrapped.$getStack(slot);
        }

        @Override
        public void $setStack(int slot, ItemStack stack) {
            wrapped.$setStack(slot, stack);
        }

        @Override
        public CompoundTag $serialize() {
            return null;
        }

        @Override
        public void $deserialize(CompoundTag invTag) {

        }

        @Override
        public int $getMaxStackSize(int slot) {
            return wrapped.$getMaxStackSize(slot);
        }

        @Override
        public ItemStack $insertStack(int slot, @NotNull ItemStack stack, boolean simulate) {
            return wrapped.$insertStack(slot, stack, simulate);
        }

        @Override
        public ItemStack $slotlessInsertStack(@NotNull ItemStack stack, int amount, boolean simulate) {
            return wrapped.$slotlessInsertStack(stack, amount, simulate);
        }

        @Override
        public ItemStack $extractStack(int slot, int amount, boolean simulate) {
            return wrapped.$extractStack(slot, amount, simulate);
        }

        @Override
        public boolean $isValid(ItemStack stack) {
            return wrapped.$isValid(stack);
        }

        @Override
        public void $setPredicate(Predicate<ItemStack> predicate) {
            wrapped.$setPredicate(predicate);
        }

        @Override
        public Slot addInvSlot(int slot, int x, int y) {
            return new Slot(new SimpleContainer(0), slot, x, y) {

                @Override
                public ItemStack getItem() {
                    return $getStack(slot);
                }

                @Override
                public void set(ItemStack stack) {
                    $setStack(slot, stack);
                    setChanged();
                }

                @Override
                public void setChanged() {
                }

                @Override
                public int getMaxStackSize() {
                    return $getMaxStackSize(slot);
                }

                @Override
                @NotNull
                public ItemStack remove(int amount) {
                    return $extractStack(slot, amount,false);
                }

                @Override
                public ItemStack safeTake(int count, int decrement, Player player) {
                    Optional<ItemStack> $$3 = this.tryRemove(count, decrement, player);
                    $$3.ifPresent((stack) -> {
                        this.onTake(player, stack);
                    });
                    return $$3.orElse(ItemStack.EMPTY);
                }

                @Override
                public void setByPlayer(ItemStack stack) {
                    super.setByPlayer(stack);
                }

                @Override
                public Optional<ItemStack> tryRemove(int count, int decrement, Player player) {
                    ItemStack extract = $extractStack(slot, Math.min(count, decrement), false);
                    return Optional.of(extract);
                }

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return HandlerWrapper.this != outputWrapper && $isValid(stack);
                }

                @Override
                public ItemStack safeInsert(ItemStack stack, int amount) {
                    if (HandlerWrapper.this == outputWrapper) return stack;
                    ItemStack stack1 = $slotlessInsertStack(stack,amount , false);
                    return stack1;
                }
            };
        }
    }

    public class OutputWrapper extends HandlerWrapper {
        public OutputWrapper(H wrapped) {
            super(wrapped);
        }

        @Override
        public boolean $isValid(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack $insertStack(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }
    }

    public ShippingBinMenu(MenuType<?> type, int id, Inventory inventory, H input, H output) {
        super(type, id);

        this.inputWrapper = (H) new HandlerWrapper(input);
        this.outputWrapper = (H) new OutputWrapper(output);

        if (!inventory.player.level().isClientSide) {
            playSound(inventory.player, ModSounds.OPEN);
        }

            int containerX = 8;
        int containerY = 18;
        int height = 3;
        int width = 9;

        int playerX = 8;
        int playerY = 139+14;

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                addSlot(inputWrapper.addInvSlot(j + width * i, containerX + j * 18, containerY + i * 18));


        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                addSlot(outputWrapper.addInvSlot(j + width * i, containerX + j * 18, containerY + i * 18+ 54+13));


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, j * 18 + playerX, i * 18 + playerY));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, i * 18 + playerX, playerY + 58));
        }
    }

    void playSound(Player player, SoundEvent pSound) {
        Vec3i vec3i = Direction.UP.getNormal();
        double d0 = player.getX() + 0.5D + vec3i.getX() / 2.0D;
        double d1 = player.getY() + 0.5D + vec3i.getY() / 2.0D;
        double d2 = player.getZ() + 0.5D + vec3i.getZ() / 2.0D;
        player.level().playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, player.level().random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean canDragTo(Slot slot) {
        return slot.index >= inputWrapper.$getSlotCount() + outputWrapper.$getSlotCount();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        Slot slot = slots.get(slotId);
        ItemStack stack = ItemStack.EMPTY;
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (slotId < inputWrapper.$getSlotCount()) {
                ItemStack taken = inputWrapper.$extractStack(slotId,stack.getCount(),false);
                if (!taken.isEmpty()) {
                    slot.onTake(player, stack);
                    player.addItem(taken);
                    if (!taken.isEmpty()) {
                        player.drop(taken,false);
                    }
                }
                return taken;
            } else if (slotId-inputWrapper.$getSlotCount() < outputWrapper.$getSlotCount()) {
                ItemStack taken = outputWrapper.$extractStack(slotId-inputWrapper.$getSlotCount(),stack.getCount(),false);
                if (!taken.isEmpty()) {
                    slot.onTake(player, stack);
                    player.addItem(taken);
                    if (!taken.isEmpty()) {
                        player.drop(taken,false);
                    }
                }
                return taken;
            } else if (!this.moveItemStackTo(slotStack, 0, inputWrapper.$getSlotCount(), false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }
        return stack;
    }


    /**
     * Merges provided ItemStack with the first available one in the container/player inventor between minIndex
     * (included) and maxIndex (excluded). Args : stack, minIndex, maxIndex, negativDirection. [!] the Container
     * implementation do not check if the item is valid for the slot
     */
    @Override
    protected boolean moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
        boolean flag = false;
        int i = pStartIndex;
        if (pReverseDirection) {
            i = pEndIndex - 1;
        }

        if (pStack.isStackable()) {
            while(!pStack.isEmpty()) {
                if (pReverseDirection) {
                    if (i < pStartIndex) {
                        break;
                    }
                } else if (i >= pEndIndex) {
                    break;
                }

                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (!itemstack.isEmpty() && slot.mayPlace(pStack) && ItemStack.isSameItemSameTags(pStack, itemstack)) {
                    int j = itemstack.getCount() + pStack.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(), pStack.getMaxStackSize());
                    if (j <= maxSize) {
                        pStack.setCount(0);
                        itemstack.setCount(j);
                        slot.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        pStack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.setChanged();
                        flag = true;
                    }
                }

                if (pReverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!pStack.isEmpty()) {
            if (pReverseDirection) {
                i = pEndIndex - 1;
            } else {
                i = pStartIndex;
            }

            while(true) {
                if (pReverseDirection) {
                    if (i < pStartIndex) {
                        break;
                    }
                } else if (i >= pEndIndex) {
                    break;
                }

                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(pStack)) {
                    if (pStack.getCount() > slot1.getMaxStackSize()) {
                        slot1.setByPlayer(pStack.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.setByPlayer(pStack.split(pStack.getCount()));
                    }

                    slot1.setChanged();
                    flag = true;
                    break;
                }

                if (pReverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

    @Override
    public void removed(Player $$0) {
        super.removed($$0);
        if ($$0 instanceof ServerPlayer) {
            playSound($$0,ModSounds.CLOSE);
        }

    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
