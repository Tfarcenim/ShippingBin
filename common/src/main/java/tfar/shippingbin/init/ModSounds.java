package tfar.shippingbin.init;

import net.minecraft.sounds.SoundEvent;
import tfar.shippingbin.ShippingBin;

public class ModSounds {

    public static final SoundEvent OPEN = SoundEvent.createVariableRangeEvent(ShippingBin.id("block.shippingbin.open"));
    public static final SoundEvent CLOSE = SoundEvent.createVariableRangeEvent(ShippingBin.id("block.shippingbin.close"));

}
