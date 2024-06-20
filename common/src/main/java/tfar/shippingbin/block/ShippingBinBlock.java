package tfar.shippingbin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import tfar.shippingbin.blockentity.ShippingBinBlockEntity;

public class ShippingBinBlock extends Block implements EntityBlock {

    protected final BlockEntityType.BlockEntitySupplier<ShippingBinBlockEntity<?>> supplier;

    public ShippingBinBlock(Properties $$0, BlockEntityType.BlockEntitySupplier<ShippingBinBlockEntity<?>> supplier) {
        super($$0);
        this.supplier = supplier;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ShippingBinBlockEntity shippingBinBlockEntity && placer instanceof ServerPlayer serverPlayer) {
            shippingBinBlockEntity.setOwner(serverPlayer.getUUID());
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult result) {
        if (!world.isClientSide) {
            MenuProvider tileEntity = getMenuProvider(state,world,pos);
            if (tileEntity != null) {
                player.openMenu(tileEntity);
            }
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.SUCCESS;
        }
    }


    @javax.annotation.Nullable
    public MenuProvider getMenuProvider(BlockState $$0, Level $$1, BlockPos $$2) {
        BlockEntity $$3 = $$1.getBlockEntity($$2);
        return $$3 instanceof MenuProvider ? (MenuProvider)$$3 : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return supplier.create(blockPos, blockState);
    }
}
