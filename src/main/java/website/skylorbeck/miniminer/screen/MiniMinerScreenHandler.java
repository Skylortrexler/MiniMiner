package website.skylorbeck.miniminer.screen;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import website.skylorbeck.miniminer.Declarar;

public class MiniMinerScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private final Inventory fuelInventory;
    private final PropertyDelegate propertyDelegate;

    public MiniMinerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        this(syncId, playerInventory, inventory, new ArrayPropertyDelegate(4));
    }

    public MiniMinerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Declarar.MINIMINER_SCREEN_HANDLER, syncId);
        this.context = ScreenHandlerContext.EMPTY;
        this.fuelInventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.addProperties(this.propertyDelegate);
        this.addSlot(new Slot(inventory, 0, 44, 18){
            @Override
            public boolean canInsert(ItemStack stack) {
                return FuelRegistry.INSTANCE.get(stack.getItem()) != null;
            }
        });
        int j;

        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, j * 18 + 51));
            }
        }
        for (j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 109));
        }


    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return MiniMinerScreenHandler.canUse(this.context, player, Declarar.MINIMINER);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < this.fuelInventory.size() ? !this.insertItem(itemStack2, this.fuelInventory.size(), this.slots.size(), true) : !this.insertItem(itemStack2, 0, this.fuelInventory.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return itemStack;
    }

    protected static boolean canUse(ScreenHandlerContext context, PlayerEntity player, Block block) {
        return context.get((world, pos) -> {
            if (!world.getBlockState(pos).isOf(block)) {
                return false;
            }
            return player.squaredDistanceTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) <= 64.0;
        }, true);
    }

    public boolean isBurning() {
        return propertyDelegate.get(0)>0;
    }

    public int getFuelProgress() {
        int i = propertyDelegate.get(0);
        int j = propertyDelegate.get(3);
        return i*13/j;
    }

    public int getCookProgress() {
        return this.propertyDelegate.get(1)*16/100;
    }
    public int getTotalMined() {
        return this.propertyDelegate.get(2);
    }
}