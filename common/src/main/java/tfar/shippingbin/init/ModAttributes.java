package tfar.shippingbin.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import tfar.shippingbin.ShippingBin;

public class ModAttributes {

    public static final Holder<Attribute> SELL_MULTIPLIER = register("sell_multiplier",new RangedAttribute("attribute.shippingbin.sell_multiplier", 1, 0, 64).setSyncable(true));

    public static final Holder<Attribute> CROP_SELL_MULTIPLIER = register("crop_sell_multiplier",new RangedAttribute("attribute.shippingbin.crop_sell_multiplier", 1, 0, 64).setSyncable(true));
    public static final Holder<Attribute> MEAT_SELL_MULTIPLIER = register("meat_sell_multiplier",new RangedAttribute("attribute.shippingbin.meat_sell_multiplier", 1, 0, 64).setSyncable(true));
    public static final Holder<Attribute> GEM_SELL_MULTIPLIER = register("gem_sell_multiplier",new RangedAttribute("attribute.shippingbin.gem_sell_multiplier", 1, 0, 64).setSyncable(true));
    public static final Holder<Attribute> WOOD_SELL_MULTIPLIER = register("wood_sell_multiplier",new RangedAttribute("attribute.shippingbin.wood_sell_multiplier", 1, 0, 64).setSyncable(true));

    public static void init(){

    }

    private static Holder<Attribute> register(String pName, Attribute pAttribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, ShippingBin.id(pName), pAttribute);
    }
}
