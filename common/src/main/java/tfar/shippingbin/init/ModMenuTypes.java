package tfar.shippingbin.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.menu.ShippingBinMenu;

public class ModMenuTypes {

    public static final MenuType<ShippingBinMenu> SHIPPING_BIN = new MenuType<>(ShippingBinMenu::new, FeatureFlags.VANILLA_SET);

    static {
        Registry.register(BuiltInRegistries.MENU, ShippingBin.id("shipping_bin"), SHIPPING_BIN);
    }

    public static void init() {
    }
}
