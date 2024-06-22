package tfar.shippingbin.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.Utils;
import tfar.shippingbin.init.ModAttributes;
import tfar.shippingbin.init.ModBlocks;

import java.util.function.Supplier;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, ShippingBin.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addDefaultBlock(() -> ModBlocks.SHIPPING_BIN);
        addTextComponent(Utils.TRADING,"Trading");
        add("category.shippingbin.trading.attribute","Bonus: %s");
        addAttribute(ModAttributes.SELL_MULTIPLIER,"Sell Multiplier");
        addAttribute(ModAttributes.WOOD_SELL_MULTIPLIER,"Wood Sell Multiplier");
        addAttribute(ModAttributes.GEM_SELL_MULTIPLIER,"Gem Sell Multiplier");
        addAttribute(ModAttributes.MEAT_SELL_MULTIPLIER,"Meat Sell Multiplier");
        addAttribute(ModAttributes.CROP_SELL_MULTIPLIER,"Crop Sell Multiplier");
    }

    protected void addTextComponent(MutableComponent component, String text) {
        ComponentContents contents = component.getContents();
        if (contents instanceof TranslatableContents translatableContents) {
            add(translatableContents.getKey(),text);
        } else {
            throw new UnsupportedOperationException(component +" is not translatable");
        }
    }

    protected void addAttribute(Attribute attribute,String key)  {
        add(attribute.getDescriptionId(),key);
    }


    protected void addDefaultItem(Supplier<? extends Item> supplier) {
        addItem(supplier,getNameFromItem(supplier.get()));
    }

    protected void addDefaultBlock(Supplier<? extends Block> supplier) {
        addBlock(supplier,getNameFromBlock(supplier.get()));
    }

    protected void addDefaultEnchantment(Supplier<? extends Enchantment> supplier) {
        addEnchantment(supplier,getNameFromEnchantment(supplier.get()));
    }

    protected void addDefaultEntityType(Supplier<EntityType<?>> supplier) {
        addEntityType(supplier,getNameFromEntity(supplier.get()));
    }

    public static String getNameFromItem(Item item) {
        return StringUtils.capitaliseAllWords(item.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromBlock(Block block) {
        return StringUtils.capitaliseAllWords(block.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromEnchantment(Enchantment enchantment) {
        return StringUtils.capitaliseAllWords(enchantment.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromEntity(EntityType<?> entity) {
        return StringUtils.capitaliseAllWords(entity.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

}
