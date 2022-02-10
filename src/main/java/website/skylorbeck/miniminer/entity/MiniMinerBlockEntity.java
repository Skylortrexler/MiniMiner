package website.skylorbeck.miniminer.entity;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import website.skylorbeck.minecraft.skylorlib.storage.StorageUtils;
import website.skylorbeck.miniminer.Declarar;
import website.skylorbeck.miniminer.Miniminer;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MiniMinerBlockEntity extends BlockEntity implements IAnimatable, Inventory {
    public MiniMinerBlockEntity( BlockPos pos, BlockState state) {
        super(Declarar.MINI_MINER_BLOCK_ENTITY_TYPE, pos, state);
    }

    private final AnimationFactory factory = new AnimationFactory(this);
    private boolean MachineStateOn = false;
    protected DefaultedList<ItemStack> fuelInventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int fuelAmount = 0;
    private int digAmount = 0;
    private int totalMined = 0;

    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        MiniMinerBlockEntity entity = (MiniMinerBlockEntity) blockEntity;
        Item item = entity.fuelInventory.get(0).getItem();
        Integer fuel = FuelRegistry.INSTANCE.get(item);
        int fuelAmount = entity.getFuelAmount();

        if (fuel != null && fuelAmount == 0 && fuel > 0) {
            entity.setFuelAmount(FuelRegistry.INSTANCE.get(item));
            entity.setMachineStateOn(true);
            entity.removeStack(0, 1);
        }
        if (fuelAmount > 0) {
            entity.setDigAmount(entity.getDigAmount() + 1);
            entity.setFuelAmount(--fuelAmount);
            if (fuelAmount == 0) {
                entity.setMachineStateOn(false);
            }
            AtomicReference<ItemStack> reward = new AtomicReference<>(ItemStack.EMPTY);

            if (!world.isClient && entity.getDigAmount() >= 200) {
                Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(pos,Miniminer.config.mining_radius,Miniminer.config.mining_radius,Miniminer.config.mining_radius);
                for (int i = 0; i < Miniminer.config.minerOreRewardMap.size(); i++) {
                    Miniminer.MinerOreRewardMap minerOreRewardMap = Miniminer.config.minerOreRewardMap.get(i);
                    Identifier ore = new Identifier(minerOreRewardMap.getOre());
                    Miniminer.MinerOreRewardMap.WeightedReward[] weightedRewards = minerOreRewardMap.getReward();
                    boolean found = false;
                    for (BlockPos blockPos : blockPosIterable) {
                        if (blockPos.getY() < pos.getY()) {
                            if (world.getBlockState(blockPos).getBlock().asItem().getDefaultStack().isOf(Registry.ITEM.get(ore))) {
                                int totalWeight = 0;
                                for (Miniminer.MinerOreRewardMap.WeightedReward value : weightedRewards) {
                                    totalWeight += value.getWeight();
                                }
                                int random = world.random.nextInt(totalWeight) + 1;
                                int currentWeight = 0;
                                for (Miniminer.MinerOreRewardMap.WeightedReward weightedReward : weightedRewards) {
                                    if (currentWeight + weightedReward.getWeight() >= random) {
                                        reward.set(new ItemStack(Registry.ITEM.get(new Identifier(weightedReward.getItem()))));
                                        found = true;
                                        break;
                                    }
                                    currentWeight += weightedReward.getWeight();
                                }
                                entity.setTotalMined(entity.getTotalMined() + 1);
                                int depletesAt = minerOreRewardMap.getDepleteAt();
                                if (depletesAt > 0 && entity.getTotalMined() > depletesAt && world.random.nextInt(entity.getTotalMined()) > depletesAt) {
//                                    Logger.getGlobal().log(Level.SEVERE,"Depleted "+blockPos+" at "+entity.getTotalMined());
                                    entity.setTotalMined(0);
                                    world.setBlockState(blockPos, Registry.BLOCK.get(new Identifier(minerOreRewardMap.getDepleted())).getDefaultState());
                                    world.markDirty(blockPos);
                                }
                                break;
                            }
                        }
                    }
                    if (found) {
                        break;
                    }
                }

                if (reward.get().equals(ItemStack.EMPTY)){
                    entity.setDigAmount(0);
                    return;
                }
                BlockPos upOne = pos.up(1);
                if (world.getBlockEntity(upOne) != null && world.getBlockState(upOne).getBlock() instanceof ChestBlock chestBlock) {
                    Inventory chestInventory = ChestBlock.getInventory(chestBlock, world.getBlockState(upOne), world, upOne, false);
                    for (int i = 0; i < chestInventory.size(); ++i) {
                        if (StorageUtils.canMergeItems(chestInventory.getStack(i), reward.get())) {
                            transfer(chestInventory, reward.get(), i, Direction.UP);
                            break;
                        }
                        if (chestInventory.getStack(i).isEmpty()) {
                            transfer(chestInventory, reward.get(), i, Direction.UP);
                            break;
                        }
                    }
                } else if (!world.isClient){//necessary?
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), reward.get());
                    itemEntity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                    itemEntity.setVelocity(world.random.nextFloat(-1f, 1f), 0, world.random.nextFloat(-1f, 1f));
                    world.spawnEntity(itemEntity);
                }
                entity.setDigAmount(0);
            }
            if (!world.isClient)
                ((ServerWorld) world).getChunkManager().markForUpdate(pos);
            markDirty(world, pos, state);
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("MachineStateOn", isMachineStateOn());
        nbt.putInt("FuelAmount", getFuelAmount());
        nbt.putInt("DigAmount", getDigAmount());
        nbt.putInt("TotalMined", getTotalMined());
        Inventories.writeNbt(nbt, getFuelInventory(), isEmpty());
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        setMachineStateOn(nbt.getBoolean("MachineStateOn"));
        setFuelAmount(nbt.getInt("FuelAmount"));
        setDigAmount(nbt.getInt("DigAmount"));
        setTotalMined(nbt.getInt("TotalMined"));
        Inventories.readNbt(nbt, this.fuelInventory);
        super.readNbt(nbt);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return FuelRegistry.INSTANCE.get(stack.getItem())>0 && Inventory.super.isValid(slot, stack);
    }

    public int getTotalMined() {
        return totalMined;
    }

    public void setTotalMined(int totalMined) {
        this.totalMined = totalMined;
    }

    public DefaultedList<ItemStack> getFuelInventory() {
        return fuelInventory;
    }

    public void setFuelInventory(DefaultedList<ItemStack> fuelInventory) {
        this.fuelInventory = fuelInventory;
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

    @Override
    public int size() {
        return fuelInventory.size();
    }

    @Override
    public boolean isEmpty() {
        return fuelInventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return fuelInventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = fuelInventory.get(slot);
        itemStack.decrement(amount);
        return fuelInventory.set(slot, itemStack);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return fuelInventory.remove(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        fuelInventory.set(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        fuelInventory.clear();
    }
    private static void transfer(Inventory to, ItemStack stack, int slot, @Nullable Direction side) {
        ItemStack itemStack = to.getStack(slot);
        if (StorageUtils.canInsert(to, stack, slot, side)) {
            if (itemStack.isEmpty()) {
                to.setStack(slot, stack);
                stack = ItemStack.EMPTY;
            } else if (StorageUtils.canMergeItems(itemStack, stack)) {
                int i = stack.getMaxCount() - itemStack.getCount();
                int j = Math.min(stack.getCount(), i);
                stack.decrement(j);
                itemStack.increment(j);
            }
        }
    }
}
