package tfar.shippingbin.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import tfar.shippingbin.init.ModMenuTypes;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.platform.Services;

public class ShippingBinMenu<H extends CommonHandler>  extends AbstractContainerMenu {

    H handler;

    public ShippingBinMenu(int id, Inventory inventory) {
        this(id, inventory, Services.PLATFORM.makeDummy(54));
    }

    public ShippingBinMenu(int id,Inventory inventory,H handler) {
        this(ModMenuTypes.SHIPPING_BIN, id,inventory,handler);
    }

    public ShippingBinMenu(MenuType<?> type,int id,Inventory inventory,H handler) {
        super(type,id);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
