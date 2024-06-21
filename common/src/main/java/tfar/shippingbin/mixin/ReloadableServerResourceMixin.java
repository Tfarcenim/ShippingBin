package tfar.shippingbin.mixin;

import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourceMixin {

    @Inject(method = "<init>",at = @At("RETURN"))
    private void addTrades(RegistryAccess.Frozen $$0, FeatureFlagSet $$1, Commands.CommandSelection $$2, int $$3, CallbackInfo ci) {

    }

}
