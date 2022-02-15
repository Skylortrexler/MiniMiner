package website.skylorbeck.miniminer;

import com.google.common.collect.Lists;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.GeckoLib;
import website.skylorbeck.minecraft.skylorlib.ConfigFileHandler;
import website.skylorbeck.minecraft.skylorlib.DynamicRecipeLoader;
import website.skylorbeck.minecraft.skylorlib.Registrar;
import website.skylorbeck.miniminer.screen.MiniMinerScreenHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static website.skylorbeck.miniminer.Miniminer.MinerOreRewardMap.WeightedReward.createReward;

public class Miniminer implements ModInitializer {
    public static Config config = new Config();

    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        Registrar.regBlock("miniminer", Declarar.MINIMINER, MODID);
        Registrar.regItem("miniminer", Declarar.MINIMINER_ITEM, MODID);
        Declarar.MINIMINER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(Miniminer.getId("miniminer_screen"), ((syncId,playerInventory) -> new MiniMinerScreenHandler(syncId, playerInventory,new SimpleInventory(1))));

        //region config init
        config.add(
                new MinerOreRewardMap(
                        "minecraft:deepslate_diamond_ore",
                        4,
                        "minecraft:deepslate",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:diamond", 5),
                                createReward("minecraft:cobbled_deepslate", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:diamond_ore",
                        4,
                        "minecraft:stone",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:diamond", 5),
                                createReward("minecraft:cobblestone", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:deepslate_lapis_ore",
                        6,
                        "minecraft:deepslate",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:lapis_lazuli", 5),
                                createReward("minecraft:cobbled_deepslate", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:lapis_ore",
                        6,
                        "minecraft:stone",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:lapis_lazuli", 5),
                                createReward("minecraft:cobblestone", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:deepslate_emerald_ore",
                        4,
                        "minecraft:deepslate",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:emerald", 5),
                                createReward("minecraft:cobbled_deepslate", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:emerald_ore",
                        4,
                        "minecraft:stone",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:emerald", 5),
                                createReward("minecraft:cobblestone", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:deepslate_redstone_ore",
                        6,
                        "minecraft:deepslate",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:redstone", 5),
                                createReward("minecraft:cobbled_deepslate", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:redstone_ore",
                        6,
                        "minecraft:stone",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:redstone", 5),
                                createReward("minecraft:cobblestone", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:deepslate_gold_ore",
                        4,
                        "minecraft:deepslate",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:raw_gold", 5),
                                createReward("minecraft:cobbled_deepslate", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:gold_ore",
                        4,
                        "minecraft:stone",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:raw_gold", 5),
                                createReward("minecraft:cobblestone", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:deepslate_iron_ore",
                        4,
                        "minecraft:deepslate",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:raw_iron", 5),
                                createReward("minecraft:cobbled_deepslate", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:iron_ore",
                        4,
                        "minecraft:stone",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:raw_iron", 5),
                                createReward("minecraft:cobblestone", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:deepslate_copper_ore",
                        8,
                        "minecraft:deepslate",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:raw_copper", 5),
                                createReward("minecraft:cobbled_deepslate", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:copper_ore",
                        8,
                        "minecraft:stone",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:raw_copper", 5),
                                createReward("minecraft:cobblestone", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:deepslate_coal_ore",
                        4,
                        "minecraft:deepslate",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:coal", 5),
                                createReward("minecraft:cobbled_deepslate", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:coal_ore",
                        4,
                        "minecraft:stone",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:coal", 5),
                                createReward("minecraft:cobblestone", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:deepslate",
                        20,
                        "minecraft:gravel",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:cobbled_deepslate", 9),
                                createReward("minecraft:coal", 1)
                        }),
                new MinerOreRewardMap(
                        "minecraft:stone",
                        20,
                        "minecraft:gravel",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:cobblestone", 9),
                                createReward("minecraft:coal", 1)
                        }),

                new MinerOreRewardMap(
                        "minecraft:granite",
                        0,
                        "minecraft:air",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:granite", 9),
                                createReward("minecraft:coal", 1)
                        }),
                new MinerOreRewardMap(
                        "minecraft:diorite",
                        0,
                        "minecraft:air",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:diorite", 9),
                                createReward("minecraft:coal", 1)
                        }),
                new MinerOreRewardMap(
                        "minecraft:andesite",
                        0,
                        "minecraft:air",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:andesite", 9),
                                createReward("minecraft:coal", 1)
                        }),
                new MinerOreRewardMap(
                        "minecraft:calcite",
                        0,
                        "minecraft:air",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:calcite", 24),
                                createReward("minecraft:amethyst_shard", 1)
                        }),
                new MinerOreRewardMap(
                        "minecraft:podzol",
                        20,
                        "minecraft:dirt",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:dirt", 2),
                                createReward("minecraft:brown_mushroom", 1),
                                createReward("minecraft:red_mushroom", 1),
                                createReward("minecraft:bone", 1)
                        }),
                new MinerOreRewardMap(
                        "minecraft:coarse_dirt",
                        20,
                        "minecraft:dirt",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:dirt", 2),
                                createReward("minecraft:gravel", 2),
                        }),
                new MinerOreRewardMap(
                        "minecraft:gravel",
                        20,
                        "minecraft:sand",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:gravel", 1),
                                createReward("minecraft:flint", 7)
                        }),
                new MinerOreRewardMap(
                        "minecraft:sandstone",
                        20,
                        "minecraft:sand",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:sand", 9),
                                createReward("minecraft:coal", 1),
                                createReward("minecraft:bone_meal", 1)
                        }),
                new MinerOreRewardMap(
                        "minecraft:red_sandstone",
                        20,
                        "minecraft:red_sand",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:red_sand", 9),
                                createReward("minecraft:coal", 1),
                                createReward("minecraft:bone_meal", 1)
                        }),
                new MinerOreRewardMap(
                        "minecraft:nether_gold_ore",
                        8,
                        "minecraft:netherrack",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:gold_nugget", 5),
                                createReward("minecraft:netherrack", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:nether_quartz_ore",
                        8,
                        "minecraft:netherrack",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:quartz", 5),
                                createReward("minecraft:netherrack", 3)
                        }),
                new MinerOreRewardMap(
                        "minecraft:snow_block",
                        0,
                        "minecraft:air",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:snowball", 1)
                        }),
                new MinerOreRewardMap(
                        "minecraft:dirt",
                        0,
                        "minecraft:air",
                        new MinerOreRewardMap.WeightedReward[]{
                                createReward("minecraft:dirt", 15),
                                createReward("minecraft:wheat_seeds", 2),
                                createReward("minecraft:pumpkin_seeds", 2),
                                createReward("minecraft:melon_seeds", 2),
                                createReward("minecraft:beetroot_seeds", 2),
                                createReward("minecraft:carrot", 1),
                                createReward("minecraft:potato", 1),
                                createReward("minecraft:poisonous_potato", 1),
                                createReward("minecraft:oak_sapling", 1),
                                createReward("minecraft:spruce_sapling", 1),
                                createReward("minecraft:birch_sapling", 1),
                                createReward("minecraft:jungle_sapling", 1),
                                createReward("minecraft:acacia_sapling", 1),
                                createReward("minecraft:dark_oak_sapling", 1),
                                createReward("minecraft:bone_meal", 1)
                        })
        );
//endregion

        try {
            config = ConfigFileHandler.initConfigFile("miniminer.json",config);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Declarar.MINIMINER_CRAFTING_RECIPE = DynamicRecipeLoader.createShapedRecipeJson(
                com.google.common.collect.Lists.newArrayList(Registry.ITEM.getId(Items.OBSIDIAN), Registry.ITEM.getId(Items.END_STONE), Registry.ITEM.getId(Items.ENDER_EYE), Registry.ITEM.getId(Items.BLAZE_ROD), Registry.ITEM.getId(Items.AMETHYST_SHARD)),//items
                com.google.common.collect.Lists.newArrayList(false, false,false,false,false),//type
                Lists.newArrayList("020", "131", " 4 "),//pattern
                Registry.ITEM.getId(Declarar.MINIMINER_ITEM),//result
                1//amount
        );
    }

    public static class Config {
        public int mining_radius = 5;
        public ArrayList<MinerOreRewardMap> minerOreRewardMap = new ArrayList<>();

        public void add(MinerOreRewardMap... minerOreRewardMap) {
            this.minerOreRewardMap.addAll(Arrays.asList(minerOreRewardMap));
        }
    }

    public static class MinerOreRewardMap {
        String ore;
        int depleteAt;
        String depleted;
        WeightedReward[] reward;

        public MinerOreRewardMap(String ore, int depleteAt, String depleted, WeightedReward[] reward) {
            this.ore = ore;
            this.depleteAt = depleteAt;
            this.reward = reward;
            this.depleted = depleted;
        }

        public String getOre() {
            return ore;
        }

        public WeightedReward[] getReward() {
            return reward;
        }

        public String getDepleted() {
            return depleted;
        }

        public int getDepleteAt() {
            return depleteAt;
        }

        public static class WeightedReward {
            String item;
            int weight;

            public WeightedReward(String item, int weight) {
                this.item = item;
                this.weight = weight;
            }

            public static WeightedReward createReward(String item, int weight) {
                return new WeightedReward(item, weight);
            }

            public String getItem() {
                return item;
            }

            public int getWeight() {
                return weight;
            }
        }
    }

    public static String MODID = "miniminer";

    public static Identifier getId(String name) {
        return new Identifier(MODID, name);
    }
}