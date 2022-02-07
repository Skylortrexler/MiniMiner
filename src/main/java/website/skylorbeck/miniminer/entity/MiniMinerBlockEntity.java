package website.skylorbeck.miniminer.entity;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import website.skylorbeck.miniminer.Declarar;

public class MiniMinerBlockEntity extends BlockEntity implements IAnimatable {
    public MiniMinerBlockEntity( BlockPos pos, BlockState state) {
        super(Declarar.MINI_MINER_BLOCK_ENTITY_TYPE, pos, state);
    }

    private final AnimationFactory factory = new AnimationFactory(this);
    private boolean MachineStateOn = false;
    private Inventory fuelInventory = new SimpleInventory(1);
    private Inventory resourceInventory = new SimpleInventory(9);
    private int fuelAmount = 0;
    private int digAmount = 0;

    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        MiniMinerBlockEntity entity = (MiniMinerBlockEntity) blockEntity;//(MiniMinerBlockEntity) world.getBlockEntity(pos);
        Item item = entity.getFuelInventory().getStack(0).getItem();//get the item in the inventory
        Integer fuel = FuelRegistry.INSTANCE.get(item);//get the fuel value of the item
        if (fuel != null && fuel > 0) {//if the item is a fuel item and has a fuel value
            entity.setFuelAmount(FuelRegistry.INSTANCE.get(item));//returns the fuel value of the item
            entity.setMachineStateOn(true);//turn on the machine
            entity.getFuelInventory().removeStack(0, 1);//remove the item from the inventory
        }
        int fuelAmount = entity.getFuelAmount();//get the fuel amount
        if (fuelAmount > 0) {//if the fuel amount is greater than 0
            entity.setDigAmount(entity.getDigAmount() + 1);//add 1 to the dig amount
            entity.setFuelAmount(--fuelAmount);//decrement the fuel amount and set it
            if (fuelAmount == 0) {//if the fuel amount is 0
                entity.setMachineStateOn(false);//turn off the machine
            }
            if (entity.getDigAmount()>=20) {//if the dig amount is 20
                ItemEntity itemEntity =new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(world.getBlockState(pos.down()).getBlock()));//create a new item entity
                itemEntity.setPos(pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5);//set the position of the item entity
//                itemEntity.setVelocity(0,1,0);//set the velocity of the item entity to 0,1,0
                world.spawnEntity(itemEntity);//spawn the item
                entity.setDigAmount(0);//set the dig amount to 0
            }
        }

    }

    public Inventory getResourceInventory() {
        return resourceInventory;
    }

    public void setResourceInventory(Inventory resourceInventory) {
        this.resourceInventory = resourceInventory;
    }

    public int getDigAmount() {
        return digAmount;
    }

    public void setDigAmount(int digAmount) {
        this.digAmount = digAmount;
    }

    public int getFuelAmount() {
        return fuelAmount;
    }

    public void setFuelAmount(int fuelAmount) {
        this.fuelAmount = fuelAmount;
    }

    public Inventory getFuelInventory() {
        return fuelInventory;
    }

    public void setFuelInventory(Inventory fuelInventory) {
        this.fuelInventory = fuelInventory;
    }

    public boolean isMachineStateOn() {
        return MachineStateOn;
    }

    public void setMachineStateOn(boolean machineStateOn) {
        MachineStateOn = machineStateOn;
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        MiniMinerBlockEntity entity = (MiniMinerBlockEntity) event.getAnimatable();
        if (!entity.isMachineStateOn()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("inactive-idle", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("running", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(
                new AnimationController<MiniMinerBlockEntity>(this, "controller", 10, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
