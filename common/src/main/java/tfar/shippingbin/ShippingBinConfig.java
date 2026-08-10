package tfar.shippingbin;


import net.neoforged.neoforge.common.ModConfigSpec;

public class ShippingBinConfig {


    public static class Server {
        public static final ModConfigSpec SPEC;

        public static final ModConfigSpec.LongValue SELLING_INTERVAL;
        public static final ModConfigSpec.LongValue SELLING_INTERVAL_OFFSET;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
            builder.push("general");
            SELLING_INTERVAL =builder.defineInRange("selling_interval",24000L,1,Long.MAX_VALUE);
            SELLING_INTERVAL_OFFSET =builder.defineInRange("selling_interval_offset",18000L,0,Long.MAX_VALUE);
            builder.pop();

            SPEC = builder.build();
        }
    }

    public static class Client {
        public static final ModConfigSpec SPEC;

        public static final ModConfigSpec.BooleanValue DISPLAY_SELL_TOAST;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
            builder.push("general");
            DISPLAY_SELL_TOAST = builder.define("display_sell_toast",true);
            builder.pop();

            SPEC = builder.build();
        }
    }
}
