package website.skylorbeck.miniminer.block;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.miniminer.Declarar;
import website.skylorbeck.miniminer.entity.MiniMinerBlockEntity;

public class MiniMinerBlock extends Block implements BlockEntityProvider {
    public MiniMinerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MiniMinerBlockEntity miniMiner) {//todo open inventory here
                Integer fuel = FuelRegistry.INSTANCE.get(player.getStackInHand(hand).getItem());
                if (fuel!=null && fuel>0 && miniMiner.getFuelAmount()==0 && miniMiner.isEmpty()) {
                    miniMiner.setStack(0,new ItemStack(player.getStackInHand(hand).getItem(),1));
                    player.getStackInHand(hand).decrement(1);
                }
                return ActionResult.SUCCESS;
            }
        return super.onUse(state, world, pos, player, hand, hit);
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
}