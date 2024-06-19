package tfar.shippingbin.init;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import tfar.shippingbin.menu.ShippingBinMenu;

public class ModMenuTypes {

    public static final MenuType<ShippingBinMenu> SHIPPING_BIN = new MenuType<>(ShippingBinMenu::new, FeatureFlags.VANILLA_SET);

}
