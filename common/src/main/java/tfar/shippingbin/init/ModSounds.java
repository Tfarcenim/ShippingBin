package tfar.shippingbin.init;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import tfar.shippingbin.ShippingBin;

public class ModSounds {

    public static final SoundEvent OPEN = SoundEvents.register(ShippingBin.id("block.shippingbin.open"));
    public static final SoundEvent CLOSE = SoundEvents.register(ShippingBin.id("block.shippingbin.close"));

    static {
    }

    public static void init() {}

}
