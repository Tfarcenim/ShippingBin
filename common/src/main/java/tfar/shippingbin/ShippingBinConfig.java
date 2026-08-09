package tfar.shippingbin;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShippingBinConfig {


    public static class Server {
        public static final ForgeConfigSpec SPEC;

        public static final ForgeConfigSpec.LongValue SELLING_INTERVAL;
        public static final ForgeConfigSpec.LongValue SELLING_INTERVAL_OFFSET;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            builder.push("general");
            SELLING_INTERVAL =builder.defineInRange("selling_interval",24000L,1,Long.MAX_VALUE);
            SELLING_INTERVAL_OFFSET =builder.defineInRange("selling_interval_offset",18000L,0,Long.MAX_VALUE);
            builder.pop();

            SPEC = builder.build();
        }
    }

    public static class Client {
        public static final ForgeConfigSpec SPEC;

        public static final ForgeConfigSpec.BooleanValue DISPLAY_SELL_TOAST;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            builder.push("general");
            DISPLAY_SELL_TOAST = builder.define("display_sell_toast",true);
            builder.pop();

            SPEC = builder.build();
        }
    }
}
