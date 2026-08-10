package tfar.shippingbin.trades;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record CompletedTrade(Component message, ItemStack icon) {

    public static final StreamCodec<RegistryFriendlyByteBuf,CompletedTrade> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.STREAM_CODEC,CompletedTrade::message,
            ItemStack.STREAM_CODEC,CompletedTrade::icon,
            CompletedTrade::new);
}
