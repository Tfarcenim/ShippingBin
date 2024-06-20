package tfar.shippingbin.menu;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfar.shippingbin.init.ModMenuTypes;
import tfar.shippingbin.inventory.CommonHandler;

public class ShippingBinMenu<H extends CommonHandler>  extends AbstractContainerMenu {

    private final H input;
    private final H output;
    TabbedHandler handler = new TabbedHandler();

    public ShippingBinMenu(int id, Inventory inventory) {
        this(id, inventory, CommonHandler.create(CommonHandler.SLOTS),CommonHandler.create(CommonHandler.SLOTS));
    }

    public ShippingBinMenu(int id,Inventory inventory,H input,H output) {
        this(ModMenuTypes.SHIPPING_BIN, id,inventory,input,output);
    }


    public class TabbedHandler implements CommonHandler {
        H active;
        @Override
        public int $getSlotCount() {
            return active.$getSlotCount();
        }

        @Override
        public ItemStack $getStack(int slot) {
            return active.$getStack(slot);
        }


        //not used
        @Override
        public CompoundTag $serialize() {
            return null;
        }

        @Override
        public void $deserialize(CompoundTag invTag) {

        }

        @Override
        public Slot addInvSlot(int slot, int x, int y) {
            return active.addInvSlot(slot,x,y);
        }

        public void setActive(H active) {
            this.active = active;
        }
    }

    public ShippingBinMenu(MenuType<?> type,int id,Inventory inventory,H input,H output) {
        super(type,id);
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
        INPUT,OUTPUT;
        private static final ButtonAction[] VALUES = values();
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
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
