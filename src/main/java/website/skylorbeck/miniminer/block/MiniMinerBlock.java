package website.skylorbeck.miniminer.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.skylorlib.storage.ExtraShulkerEntity;
import website.skylorbeck.miniminer.Declarar;
import website.skylorbeck.miniminer.entity.MiniMinerBlockEntity;
import website.skylorbeck.miniminer.screen.MiniMinerScreenHandler;

public class MiniMinerBlock extends Block implements BlockEntityProvider {
    public MiniMinerBlock(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient)
        return ActionResult.SUCCESS;
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MiniMinerBlockEntity miniMiner) {
                NamedScreenHandlerFactory factory = this.createScreenHandlerFactory(state, world, pos);
                player.openHandledScreen(factory);
                return ActionResult.CONSUME;
            }
        return super.onUse(state, world, pos, player, hand, hit);
    }
    @Override
    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        MiniMinerBlockEntity blockEntity = (MiniMinerBlockEntity) world.getBlockEntity(pos);
        return new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> new MiniMinerScreenHandler(syncId, playerInventory, blockEntity, blockEntity.getPropertyDelegate()),blockEntity.getDisplayName());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return MiniMinerBlockEntity::tick;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return Declarar.MINI_MINER_BLOCK_ENTITY_TYPE.instantiate(pos, state);
    }
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof MiniMinerBlockEntity) {
            ((MiniMinerBlockEntity)blockEntity).setCustomName(itemStack.getName());
        }
    }
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MiniMinerBlockEntity MiniMinerBlockEntity) {
            if (!world.isClient ) {

                ItemStack itemStack = this.asItem().getDefaultStack();

                if (MiniMinerBlockEntity.hasCustomName()) {
                    itemStack.setCustomName(MiniMinerBlockEntity.getDisplayName());
                }

                ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
                itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, MiniMinerBlockEntity.getFuelInventory().get(0));
                world.spawnEntity(itemEntity);
            }
        }

        super.onBreak(world, pos, state, player);
    }
    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return createCuboidShape(3, 0, 3, 13, 14, 13);
    }
}