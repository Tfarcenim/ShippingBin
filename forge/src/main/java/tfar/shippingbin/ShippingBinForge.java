package tfar.shippingbin;

import net.minecraftforge.fml.common.Mod;

@Mod(ShippingBin.MOD_ID)
public class ShippingBinForge {
    
    public ShippingBinForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        ShippingBin.init();
        
    }
}