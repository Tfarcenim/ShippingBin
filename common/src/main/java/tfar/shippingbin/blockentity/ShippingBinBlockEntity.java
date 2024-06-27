package tfar.shippingbin.blockentity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import tfar.shippingbin.ShippingBin;
import tfar.shippingbin.init.ModBlockEntityTypes;
import tfar.shippingbin.inventory.CommonHandler;
import tfar.shippingbin.level.ShippingBinInventories;
import tfar.shippingbin.menu.ShippingBinMenu;
import tfar.shippingbin.platform.Services;

import java.util.UUID;

public class ShippingBinBlockEntity<H extends CommonHandler> extends BlockEntity implements MenuProvider {

    protected UUID owner = Util.NIL_UUID;

    public ShippingBinBlockEntity(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    public ShippingBinBlockEntity(BlockPos $$1, BlockState $$2) {
        super(ModBlockEntityTypes.SHIPPING_BIN, $$1, $$2);
    }

    public final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            ShippingBinBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
           // ShippingBinBlockEntitythis.updateBlockState(state, true);
        }

        protected void onClose(Level level, BlockPos pos, BlockState state) {
            ShippingBinBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
           // ShippingBinBlockEntity.this.updateBlockState(state, false);
        }

        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int previousCount, int newCount) {
        }

        protected boolean isOwnContainer(Player player) {
                return true;
        }
    };

    public static BlockEntityType.BlockEntitySupplier<ShippingBinBlockEntity<?>> shippingBin() {
        return (pPos, pState) -> Services.PLATFORM.blockEntity(ModBlockEntityTypes.SHIPPING_BIN,pPos,pState);
    }

    public Pair<H,H> getServerInventory() {
        if (this.level.isClientSide) {
            return Pair.of(CommonHandler.create(CommonHandler.SLOTS),CommonHandler.create(CommonHandler.SLOTS));
        } else return (Pair<H, H>) ShippingBinInventories.getOrCreateInstance(this.level.getServer()).getInventory(owner);
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
        setChanged();
    }

    public UUID getOwner() {
        return owner;
    }

    void playSound(BlockState pState, SoundEvent pSound) {
        Vec3i vec3i = pState.getValue(BarrelBlock.FACING).getNormal();
        double d0 = this.worldPosition.getX() + 0.5D + vec3i.getX() / 2.0D;
        double d1 = this.worldPosition.getY() + 0.5D + vec3i.getY() / 2.0D;
        double d2 = this.worldPosition.getZ() + 0.5D + vec3i.getZ() / 2.0D;
        this.level.playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }


    public void startOpen(Player pPlayer) {
        if (!isRemoved() && !pPlayer.isSpectator()) {
            openersCounter.incrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void stopOpen(Player pPlayer) {
        if (!isRemoved() && !pPlayer.isSpectator()) {
            openersCounter.decrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void recheckOpen() {
        if (!isRemoved()) {
            openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putUUID("owner",owner);
    }


    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        owner = tag.getUUID("owner");
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Shipping Bin");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        Pair<H,H> handlers = getServerInventory();
        return new ShippingBinMenu<>(id,inventory,handlers.getKey(),handlers.getValue());
    }
}
