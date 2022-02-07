package website.skylorbeck.miniminer;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import software.bernie.example.block.tile.BotariumTileEntity;
import software.bernie.example.registry.BlockRegistry;
import software.bernie.geckolib3.GeckoLib;
import website.skylorbeck.miniminer.block.MiniMinerBlock;
import website.skylorbeck.miniminer.entity.MiniMinerBlockEntity;
import website.skylorbeck.miniminer.item.MiniMinerItem;

public class Declarar {
    public static final ItemGroup MINIMINER_GROUP = FabricItemGroupBuilder.build(Miniminer.getId("category"),() -> new ItemStack(Declarar.MINIMINER_ITEM));
    public static final Block MINIMINER = new MiniMinerBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque().lightLevel(5));
    public static final Item MINIMINER_ITEM = new MiniMinerItem(MINIMINER, new FabricItemSettings().group(MINIMINER_GROUP));
    public static final BlockEntityType<MiniMinerBlockEntity> MINI_MINER_BLOCK_ENTITY_TYPE = Registry.register(
            Registry.BLOCK_ENTITY_TYPE, Miniminer.getId("miniminer_entity") ,
            FabricBlockEntityTypeBuilder.create(MiniMinerBlockEntity::new, MINIMINER).build(null));

}
