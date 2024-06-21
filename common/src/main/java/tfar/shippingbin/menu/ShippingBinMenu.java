package tfar.shippingbin.menu;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import tfar.shippingbin.init.ModMenuTypes;
import tfar.shippingbin.inventory.CommonHandler;

import java.util.Optional;

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
                public ItemStack safeInsert(ItemStack stack, int amount) {
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
        public ItemStack $insertStack(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public ItemStack $slotlessInsertStack(@NotNull ItemStack stack, int amount, boolean simulate) {
            return stack;
        }
    }

    public class InputWrapper extends HandlerWrapper {

        public InputWrapper(H wrapped) {
            super(wrapped);
        }

        @Override
        public ItemStack $extractStack(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    }


    public ShippingBinMenu(MenuType<?> type, int id, Inventory inventory, H input, H output) {
        super(type, id);

        this.inputWrapper = (H) new HandlerWrapper(input);
        this.outputWrapper = (H) new OutputWrapper(output);

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

    public void doClick(int slotId, int button, ClickType clickType, Player player) {

        if (clickType ==  ClickType.QUICK_CRAFT) return;

        super.doClick(slotId, button, clickType, player);
        /*
        Slot slot = slotId >= 0 ? slots.get(slotId) : null;
        switch (clickType) {
            case PICKUP -> {
                if (slotId >= CommonHandler.SLOTS) {
                    super.doClick(slotId, button, clickType, player);
                } else {
                    int count;
                    ClickAction clickAction = button == 0 ? ClickAction.PRIMARY : ClickAction.SECONDARY;
                    if (slot == null) return;
                    ItemStack existing = slot.getItem();
                    ItemStack carried = getCarried();

                    if (existing.isEmpty()) {
                        for (int i = 0; i < CommonHandler.SLOTS; i++) {
                            Slot otherSlot = slots.get(i);
                            ItemStack otherExisting = otherSlot.getItem();

                            if (!carried.isEmpty()) {
                                count = clickAction == ClickAction.PRIMARY ? carried.getCount() : 1;
                                this.setCarried(otherSlot.safeInsert(carried, count));
                            }
                        }
                    } else {
                        if (slot.mayPickup(player)) {
                            if (carried.isEmpty()) {
                                count = clickAction == ClickAction.PRIMARY ? existing.getCount() : (existing.getCount() + 1) / 2;
                                Optional<ItemStack> optional = slot.tryRemove(count, Integer.MAX_VALUE, player);
                                optional.ifPresent(($$2x) -> {
                                    this.setCarried($$2x);
                                    slot.onTake(player, $$2x);
                                });
                            }
                        } else if (slot.mayPlace(carried)) {
                            if (ItemStack.isSameItemSameTags(existing, carried)) {
                                count = clickAction == ClickAction.PRIMARY ? carried.getCount() : 1;
                                this.setCarried(slot.safeInsert(carried, count));
                            } else if (carried.getCount() <= slot.getMaxStackSize(carried)) {
                                this.setCarried(existing);
                                slot.setByPlayer(carried);
                            }
                        } else if (ItemStack.isSameItemSameTags(existing, carried)) {
                            Optional<ItemStack> optional = slot.tryRemove(existing.getCount(), carried.getMaxStackSize() - carried.getCount(), player);
                            optional.ifPresent((stack) -> {
                                carried.grow(stack.getCount());
                                slot.onTake(player, stack);
                            });
                        }
                    }
                }
            }
            case QUICK_MOVE -> {
                super.doClick(slotId, button, clickType, player);
            }
            case SWAP -> {

            }

            case CLONE -> {
            }
            case THROW -> {
            }
            case QUICK_CRAFT -> {
            }
            case PICKUP_ALL -> {
            }
        }*/
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
                }
                return taken;
            } else if (slotId-inputWrapper.$getSlotCount() < outputWrapper.$getSlotCount()) {
                ItemStack taken = outputWrapper.$extractStack(slotId-inputWrapper.$getSlotCount(),stack.getCount(),false);
                if (!taken.isEmpty()) {
                    slot.onTake(player, stack);
                    player.addItem(taken);
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

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
