package tfar.shippingbin.init;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ModAttributes {

    public static final Attribute SELL_MULTIPLIER = new RangedAttribute("attribute.shippingbin.sell_multiplier", 1, 0, 64).setSyncable(true);

    public static final Attribute CROP_SELL_MULTIPLIER = new RangedAttribute("attribute.shippingbin.crop_sell_multiplier", 1, 0, 64).setSyncable(true);
    public static final Attribute MEAT_SELL_MULTIPLIER = new RangedAttribute("attribute.shippingbin.meat_sell_multiplier", 1, 0, 64).setSyncable(true);
    public static final Attribute GEM_SELL_MULTIPLIER = new RangedAttribute("attribute.shippingbin.gem_sell_multiplier", 1, 0, 64).setSyncable(true);
    public static final Attribute WOOD_SELL_MULTIPLIER = new RangedAttribute("attribute.shippingbin.wood_sell_multiplier", 1, 0, 64).setSyncable(true);
}
