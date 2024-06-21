package tfar.shippingbin.menu;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
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

    private final H input;
    private final H output;
    TabbedHandler handler = new TabbedHandler();

    public ShippingBinMenu(int id, Inventory inventory) {
        this(id, inventory, CommonHandler.create(CommonHandler.SLOTS), CommonHandler.create(CommonHandler.SLOTS));
    }

    public ShippingBinMenu(int id, Inventory inventory, H input, H output) {
        this(ModMenuTypes.SHIPPING_BIN, id, inventory, input, output);
    }


    public class TabbedHandler implements CommonHandler {
        H active;

        @Override
        public int $getSlotCount() {
            return active.$getSlotCount();
        }

        @Override
        public void $setStack(int slot, ItemStack stack) {
            active.$setStack(slot, stack);
        }

        @Override
        public ItemStack $getStack(int slot) {
            return active.$getStack(slot);
        }

        @Override
        public ItemStack $remove(int slot, int amount) {
            return active.$remove(slot, amount);
        }

        @Override
        public int $getMaxStackSize(int slot) {
            return active.$getMaxStackSize(slot);
        }

        //not used
        @Override
        public CompoundTag $serialize() {
            return null;
        }

        //not used
        @Override
        public void $deserialize(CompoundTag invTag) {

        }

        @Override
        public ItemStack $insertStack(int slot, @NotNull ItemStack stack, boolean simulate) {
            return active.$insertStack(slot,stack,simulate);
        }

        @Override
        public ItemStack $extractStack(int slot, int amount, boolean simulate) {
            return active.$extractStack(slot,amount,simulate);
        }

        @Override
        public ItemStack $slotlessInsertStack(@NotNull ItemStack stack, boolean simulate) {
            return active.$slotlessInsertStack(stack, simulate);
        }

        @Override
        public Slot addInvSlot(int slot, int x, int y) {
            return new Slot(new SimpleContainer(0), slot, x, y) {
                @Override
                public ItemStack getItem() {
                    return active.$getStack(slot);
                }

                @Override
                public void set(ItemStack $$0) {
                    active.$setStack(slot, $$0);
                    setChanged();
                }

                @Override
                public void setChanged() {

                }

                @Override
                public int getMaxStackSize() {
                    return active.$getMaxStackSize(slot);
                }

                @Override
                @NotNull
                public ItemStack remove(int amount) {
                    return active.$remove(slot, amount);
                }

                @Override
                public ItemStack safeTake(int count, int decrement, Player player) {
                    Optional<ItemStack> $$3 = this.tryRemove(count, decrement, player);
                    $$3.ifPresent(($$1x) -> {
                        this.onTake(player, $$1x);
                    });
                    return $$3.orElse(ItemStack.EMPTY);
                }

                @Override
                public void setByPlayer(ItemStack stack) {
                    super.setByPlayer(stack);
                }

                @Override
                public Optional<ItemStack> tryRemove(int count, int decrement, Player player) {
                    ItemStack extract = $extractStack(slot,Math.min(count,decrement),false);
                    return Optional.of(extract);
                }

                @Override
                public ItemStack safeInsert(ItemStack stack, int amount) {

                    ItemStack stack1 = $slotlessInsertStack(stack, false);
                    return stack1;

                   /* if (true) {
                        return super.safeInsert(stack, amount);
                    }

                        if (!stack.isEmpty() && this.mayPlace(stack)) {
                            for (int i = 0; i < $getSlotCount();i++) {
                                Slot otherSlot = slots.get(i);
                                ItemStack otherExisting = otherSlot.getItem();
                                int added = Math.min(Math.min(amount, stack.getCount()), otherSlot.getMaxStackSize(stack) - otherExisting.getCount());
                                if (otherExisting.isEmpty()) {
                                    otherSlot.setByPlayer(stack.split(added));
                                }
                            }
                            /*ItemStack existing = this.getItem();
                            int added = Math.min(Math.min(amount, stack.getCount()), this.getMaxStackSize(stack) - existing.getCount());
                            if (existing.isEmpty()) {
                                this.setByPlayer(stack.split(added));
                            } else if (ItemStack.isSameItemSameTags(existing, stack)) {
                                stack.shrink(added);
                                existing.grow(added);
                                this.setByPlayer(existing);
                            }*/
                        }
                   // return stack;
                };
          //  };
        }

        public void setActive(H active) {
            this.active = active;
        }
    }

    public ShippingBinMenu(MenuType<?> type, int id, Inventory inventory, H input, H output) {
        super(type, id);
        this.input = input;
        this.output = output;

        int containerX = 8;
        int containerY = 18;
        int height = 6;
        int width = 9;

        int playerX = 8;
        int playerY = 139;

        handler.setActive(input);

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                addSlot(handler.addInvSlot(j + width * i, containerX + j * 18, containerY + i * 18));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, j * 18 + playerX, i * 18 + playerY));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, i * 18 + playerX, playerY + 58));
        }
    }

    public enum ButtonAction {
        INPUT, OUTPUT;
        private static final ButtonAction[] VALUES = values();
    }

    public void doClick(int slotId, int button, ClickType clickType, Player player) {

        if (clickType ==  ClickType.QUICK_CRAFT) return;

        super.doClick(slotId, button, clickType, player);
        System.out.println(clickType);
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
    public boolean clickMenuButton(Player player, int id) {
        if (id < 0 || id >= ButtonAction.VALUES.length) return false;
        ButtonAction buttonAction = ButtonAction.VALUES[id];
        if (player instanceof ServerPlayer serverPlayer) {
            switch (buttonAction) {
                case INPUT -> {
                    this.handler.active = input;
                }
                case OUTPUT -> {
                    this.handler.active = output;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        Slot slot = slots.get(slotId);
        ItemStack stack = ItemStack.EMPTY;
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (slotId < CommonHandler.SLOTS) {
                ItemStack taken = handler.active.$extractStack(slotId,stack.getCount(),false);
                if (!taken.isEmpty()) {
                    slot.onTake(player, stack);
                    player.addItem(taken);
                }
                return taken;
            } else if (!this.moveItemStackTo(slotStack, 0, CommonHandler.SLOTS, false)) {
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

    protected boolean moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
        boolean flag = false;
        int i = pStartIndex;
        if (pReverseDirection) {
            i = pEndIndex - 1;
        }

        Slot slot;
        ItemStack itemstack;
        if (pStack.isStackable()) {
            while(!pStack.isEmpty()) {
                if (pReverseDirection) {
                    if (i < pStartIndex) {
                        break;
                    }
                } else if (i >= pEndIndex) {
                    break;
                }

                slot = this.slots.get(i);
                itemstack = slot.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(pStack, itemstack)) {
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

                slot = this.slots.get(i);
                itemstack = slot.getItem();
                if (itemstack.isEmpty() && slot.mayPlace(pStack)) {
                    if (pStack.getCount() > slot.getMaxStackSize()) {
                        slot.setByPlayer(pStack.split(slot.getMaxStackSize()));
                    } else {
                        slot.setByPlayer(pStack.split(pStack.getCount()));
                    }

                    slot.setChanged();
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
    public boolean stillValid(Player player) {
        return true;
    }
}
