package tfar.shippingbin.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.commons.lang3.tuple.Pair;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.inventory.CommonHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShippingBinInventories extends SavedData {

    protected final ServerLevel level;
    Map<UUID, Pair<CommonHandler,CommonHandler>> handlerMap = new HashMap<>();

    public ShippingBinInventories(ServerLevel level) {
        this.level = level;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ListTag listTag = new ListTag();
        for (Map.Entry<UUID, Pair<CommonHandler, CommonHandler>> handlerEntry : handlerMap.entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("uuid",handlerEntry.getKey());
            Pair<CommonHandler,CommonHandler> pair = handlerEntry.getValue();
            tag.put("input",pair.getKey().$serialize());
            tag.put("output",pair.getValue().$serialize());
            listTag.add(tag);
        }

        compoundTag.put("contents",listTag);

        return compoundTag;
    }

    public Map<UUID, Pair<CommonHandler, CommonHandler>> getHandlerMap() {
        return handlerMap;
    }

    public Pair<CommonHandler,CommonHandler> getInventory(UUID uuid) {
        return handlerMap.computeIfAbsent(uuid,uuid1 -> Pair.of(CommonHandler.create(CommonHandler.SLOTS),CommonHandler.create(CommonHandler.SLOTS)));
    }

    protected void load(CompoundTag compoundTag) {
        ListTag listTag = compoundTag.getList("contents", Tag.TAG_COMPOUND);
        for (Tag tag : listTag) {
            CompoundTag compoundTag1 = (CompoundTag)tag;
            UUID uuid = compoundTag1.getUUID("uuid");
            CommonHandler commonHandler = CommonHandler.create(CommonHandler.SLOTS);
            commonHandler.$deserialize(compoundTag1.getCompound("input"));
            CommonHandler output = CommonHandler.create(CommonHandler.SLOTS);
            output.$deserialize(compoundTag1.getCompound("output"));
            handlerMap.put(uuid,Pair.of(commonHandler,output));
        }
    }

    @Override
    public void save(File file) {
        super.save(file);
    }

    public static ShippingBinInventories getOrCreateInstance(MinecraftServer server) {
        ServerLevel overworld = server.overworld();
        return overworld.getDataStorage().computeIfAbsent(compoundTag -> loadStatic(compoundTag,overworld), () -> new ShippingBinInventories(overworld), ShippingBin.MOD_ID);
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    public static ShippingBinInventories loadStatic(CompoundTag compoundTag, ServerLevel level) {
        ShippingBinInventories dankSavedData = new ShippingBinInventories(level);
        dankSavedData.load(compoundTag);
        return dankSavedData;
    }
}
