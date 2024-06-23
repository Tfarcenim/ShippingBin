package tfar.shippingbin.trades;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record CompletedTrade(Component message, ItemStack icon) {

    public void write(FriendlyByteBuf buf) {
        buf.writeComponent(message);
        buf.writeItem(icon);
    }

    public static CompletedTrade read(FriendlyByteBuf buf) {
        Component message = buf.readComponent();
        ItemStack icon = buf.readItem();
        return new CompletedTrade(message,icon);
    }

}
