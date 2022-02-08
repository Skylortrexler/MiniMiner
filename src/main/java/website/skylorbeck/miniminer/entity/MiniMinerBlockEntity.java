package website.skylorbeck.miniminer.entity;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.impl.transfer.item.InventoryStorageImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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

public class MiniMinerBlockEntity extends BlockEntity implements IAnimatable, Inventory {
    public MiniMinerBlockEntity( BlockPos pos, BlockState state) {
        super(Declarar.MINI_MINER_BLOCK_ENTITY_TYPE, pos, state);
    }

    private final AnimationFactory factory = new AnimationFactory(this);
    private boolean MachineStateOn = false;
    protected DefaultedList<ItemStack> fuelInventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int fuelAmount = 0;
    private int digAmount = 0;

    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        MiniMinerBlockEntity entity = (MiniMinerBlockEntity) blockEntity;//(MiniMinerBlockEntity) world.getBlockEntity(pos);
        Item item = entity.fuelInventory.get(0).getItem();//get the item in the inventory
        Integer fuel = FuelRegistry.INSTANCE.get(item);//get the fuel value of the item
        int fuelAmount = entity.getFuelAmount();//get the fuel amount

        if (fuel != null && fuelAmount==0 && fuel > 0 ) {//if the item is a fuel item and has a fuel value
            entity.setFuelAmount(FuelRegistry.INSTANCE.get(item));//returns the fuel value of the item
            entity.setMachineStateOn(true);//turn on the machine
            entity.removeStack(0, 1);//remove the item from the inventory
        }
        if (fuelAmount > 0) {//if the fuel amount is greater than 0
            entity.setDigAmount(entity.getDigAmount() + 1);//add 1 to the dig amount
            entity.setFuelAmount(--fuelAmount);//decrement the fuel amount and set it
            if (fuelAmount == 0) {//if the fuel amount is 0
                entity.setMachineStateOn(false);//turn off the machine
            }
            ItemStack reward = new ItemStack(world.getBlockState(pos.down()).getBlock());

            if (entity.getDigAmount() >= 200) {//if the dig amount is 20
                BlockPos upOne = pos.up(1);//get the block above the block
                if (world.getBlockEntity(upOne) != null && world.getBlockState(upOne).getBlock() instanceof ChestBlock chestBlock) {//if the block above is a chest
                    Inventory chestInventory = ChestBlock.getInventory(chestBlock, world.getBlockState(upOne), world, upOne, false);//get the chest inventory
                    for (int i = 0; i < chestInventory.size(); i++) {//for each slot in the chest inventory
                        if (StorageUtils.canInsert(chestInventory, reward, i, Direction.UP)) {//if the item can be inserted into the chest
                            StorageUtils.transfer(chestInventory, reward, i, Direction.UP);//transfer the item to the chest
                            entity.setDigAmount(0);//set the dig amount to 0

                            break;
                        }
                    }
                } else {
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), reward);//create a new item entity
                    itemEntity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);//set the position of the item entity
                    itemEntity.setVelocity(world.random.nextFloat(-1f, 1f), 0, world.random.nextFloat(-1f, 1f));//set the velocity of the item entity to 0,1,0
                    world.spawnEntity(itemEntity);//spawn the item
                    entity.setDigAmount(0);//set the dig amount to 0
                }

            }
            if (!world.isClient)
            ((ServerWorld) world).getChunkManager().markForUpdate(pos);//mark the block for update
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
        Inventories.writeNbt(nbt, getFuelInventory(), isEmpty());
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        setMachineStateOn(nbt.getBoolean("MachineStateOn"));
        setFuelAmount(nbt.getInt("FuelAmount"));
        setDigAmount(nbt.getInt("DigAmount"));
        Inventories.readNbt(nbt, this.fuelInventory);
        super.readNbt(nbt);
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
}
