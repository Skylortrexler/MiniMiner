package website.skylorbeck.miniminer.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.example.registry.TileRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import website.skylorbeck.miniminer.Declarar;
import website.skylorbeck.miniminer.Miniminer;

public class MiniMinerBlockEntity extends BlockEntity implements IAnimatable {
    public MiniMinerBlockEntity( BlockPos pos, BlockState state) {
        super(Declarar.MINI_MINER_BLOCK_ENTITY_TYPE, pos, state);
    }

    private final AnimationFactory factory = new AnimationFactory(this);
    private MachineState State = MachineState.IDLE;
    private int transitionTime = 0;

    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        MiniMinerBlockEntity entity = (MiniMinerBlockEntity) blockEntity;
        if (entity.getState() == MachineState.ACTIVATING && entity.getTransitionTime() == 0) {
            entity.setState(MachineState.WORKING);
        } else
        if (entity.getState() == MachineState.DEACTIVATING && entity.getTransitionTime() == 0) {
            entity.setState(MachineState.IDLE);
        }
        if (entity.getTransitionTime() > 0) {
            entity.setTransitionTime(entity.getTransitionTime()-1);
        }
    }

    public enum MachineState {
        IDLE,
        WORKING,
        ACTIVATING,
        DEACTIVATING
    }

    public MachineState getState() {
        return State;
    }

    public void setState(MachineState state) {
        State = state;
    }

    public int getTransitionTime() {
        return transitionTime;
    }

    public void setTransitionTime(int transitionTime) {
        this.transitionTime = transitionTime;
    }

    @SuppressWarnings("unchecked")
    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        MiniMinerBlockEntity entity = (MiniMinerBlockEntity) event.getAnimatable();
        switch (entity.getState()) {
            case IDLE -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("inactive-idle", true));
                return PlayState.CONTINUE;
            }
            case WORKING -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("running", true));
                return PlayState.CONTINUE;
            }
            case ACTIVATING -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("activate", false));
                return PlayState.CONTINUE;
            }
            case DEACTIVATING -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("deactivate", false));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(
                new AnimationController<MiniMinerBlockEntity>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
