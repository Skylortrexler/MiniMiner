package website.skylorbeck.miniminer.entity;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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
import website.skylorbeck.miniminer.Declarar;
import website.skylorbeck.miniminer.Miniminer;
import website.skylorbeck.miniminer.screen.MiniMinerScreenHandler;

import java.util.concurrent.atomic.AtomicReference;

import static website.skylorbeck.minecraft.skylorlib.storage.StorageUtils.canMergeItems;
import static website.skylorbeck.minecraft.skylorlib.storage.StorageUtils.transfer;

public class MiniMinerBlockEntity extends BlockEntity implements IAnimatable, Inventory, NamedScreenHandlerFactory {

    public MiniMinerBlockEntity(BlockPos pos, BlockState state) {
        super(Declarar.MINI_MINER_BLOCK_ENTITY_TYPE, pos, state);
    }

    private final AnimationFactory factory = new AnimationFactory(this);
    private boolean MachineStateOn = false;
    protected DefaultedList<ItemStack> fuelInventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int fuelAmount = 0;
    private int fuelAmountMax = 0;
    private int digAmount = 0;
    private int totalMined = 0;
    private Text customName;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        public int get(int index) {
            return switch (index) {
                case 0 -> MiniMinerBlockEntity.this.fuelAmount;
                case 1 -> MiniMinerBlockEntity.this.digAmount;
                case 2 -> MiniMinerBlockEntity.this.totalMined;
                case 3 -> MiniMinerBlockEntity.this.fuelAmountMax;
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                case 0 -> MiniMinerBlockEntity.this.fuelAmount = value;
                case 1 -> MiniMinerBlockEntity.this.digAmount = value;
                case 2 -> MiniMinerBlockEntity.this.totalMined = value;
                case 3 -> MiniMinerBlockEntity.this.fuelAmountMax = value;
            }

        }

        public int size() {
            return 4;
        }
    };


    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        MiniMinerBlockEntity entity = (MiniMinerBlockEntity) blockEntity;
        Item item = entity.fuelInventory.get(0).getItem();
        Integer fuel = FuelRegistry.INSTANCE.get(item);
        int fuelAmount = entity.getFuelAmount();

        if (fuel != null && fuelAmount == 0 && fuel > 0) {
            entity.setFuelAmount(FuelRegistry.INSTANCE.get(item));
            entity.setFuelAmountMax(FuelRegistry.INSTANCE.get(item));
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
                Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(pos, Miniminer.config.mining_radius, Miniminer.config.mining_radius, Miniminer.config.mining_radius);
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

                if (reward.get().equals(ItemStack.EMPTY)) {
                    entity.setDigAmount(0);
                    return;
                }
                BlockPos upOne = pos.up(1);
                if (world.getBlockEntity(upOne) != null && world.getBlockState(upOne).getBlock() instanceof ChestBlock chestBlock) {
                    Inventory chestInventory = ChestBlock.getInventory(chestBlock, world.getBlockState(upOne), world, upOne, false);
                    for (int i = 0; i < chestInventory.size(); ++i) {
                        if (canMergeItems(chestInventory.getStack(i), reward.get())) {
                            transfer(chestInventory, reward.get(), i, Direction.UP);
                            break;
                        }
                        if (chestInventory.getStack(i).isEmpty()) {
                            transfer(chestInventory, reward.get(), i, Direction.UP);
                            break;
                        }
                    }
                } else if (!world.isClient) {//necessary?
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), reward.get());
                    itemEntity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                    itemEntity.setVelocity(world.random.nextFloat(-0.5f, 0.5f), 0, world.random.nextFloat(-0.5f, 0.5f));
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
        nbt.putInt("FuelAmountMax", getFuelAmountMax());
        nbt.putInt("DigAmount", getDigAmount());
        nbt.putInt("TotalMined", getTotalMined());
        Inventories.writeNbt(nbt, getFuelInventory(), isEmpty());
        nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        setMachineStateOn(nbt.getBoolean("MachineStateOn"));
        setFuelAmount(nbt.getInt("FuelAmount"));
        setFuelAmountMax(nbt.getInt("FuelAmountMax"));
        setDigAmount(nbt.getInt("DigAmount"));
        setTotalMined(nbt.getInt("TotalMined"));
        Inventories.readNbt(nbt, this.fuelInventory);
        if (nbt.contains("CustomName", 8)) {
            this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
        }
        super.readNbt(nbt);
    }


    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return FuelRegistry.INSTANCE.get(stack.getItem()) != null && Inventory.super.isValid(slot, stack);
    }

    public int getFuelAmountMax() {
        return fuelAmountMax;
    }

    public void setFuelAmountMax(int fuelAmountMax) {
        this.fuelAmountMax = fuelAmountMax;
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
                new AnimationController<>(this, "controller", 10, this::predicate));
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
        for (ItemStack itemStack : this.fuelInventory) {
            if (itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot < 0 || slot >= this.fuelInventory.size()) {
            return ItemStack.EMPTY;
        }
        return this.fuelInventory.get(slot);
    }


    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.fuelInventory, slot, amount);
        if (!itemStack.isEmpty()) {
            this.markDirty();
        }
        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack itemStack = this.fuelInventory.get(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.fuelInventory.set(slot, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.fuelInventory.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        this.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        fuelInventory.clear();
    }





    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    public Text getDisplayName() {
        return this.customName != null ? this.customName : new TranslatableText("container.miniminer");
    }

    public boolean hasCustomName(){
        return this.customName != null;
    }

    public void setCustomName(@Nullable Text customName) {
        this.customName = customName;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new MiniMinerScreenHandler(syncId, inv, this, propertyDelegate);
    }
}