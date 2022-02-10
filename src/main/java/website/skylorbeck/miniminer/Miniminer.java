package website.skylorbeck.miniminer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.GeckoLib;
import website.skylorbeck.minecraft.skylorlib.Registrar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        //todo abstract this to skylib
        if (!Files.exists(Paths.get("config/miniminer.json"))) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            config.add(
                    new MinerOreRewardMap(
                            "minecraft:deepslate_diamond_ore",
                            4,
                            "minecraft:deepslate",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:diamond",5),
                                    createReward("minecraft:cobbled_deepslate",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:diamond_ore",
                            4,
                            "minecraft:stone",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:diamond",5),
                                    createReward("minecraft:cobblestone",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:deepslate_lapis_ore",
                            6,
                            "minecraft:deepslate",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:lapis_lazuli",5),
                                    createReward("minecraft:cobbled_deepslate",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:lapis_ore",
                            6,
                            "minecraft:stone",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:lapis_lazuli",5),
                                    createReward("minecraft:cobblestone",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:deepslate_emerald_ore",
                            4,
                            "minecraft:deepslate",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:emerald",5),
                                    createReward("minecraft:cobbled_deepslate",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:emerald_ore",
                            4,
                            "minecraft:stone",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:emerald",5),
                                    createReward("minecraft:cobblestone",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:deepslate_redstone_ore",
                            6,
                            "minecraft:deepslate",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:redstone",5),
                                    createReward("minecraft:cobbled_deepslate",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:redstone_ore",
                            6,
                            "minecraft:stone",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:redstone",5),
                                    createReward("minecraft:cobblestone",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:deepslate_gold_ore",
                            4,
                            "minecraft:deepslate",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:raw_gold",5),
                                    createReward("minecraft:cobbled_deepslate",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:gold_ore",
                            4,
                            "minecraft:stone",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:raw_gold",5),
                                    createReward("minecraft:cobblestone",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:deepslate_iron_ore",
                            4,
                            "minecraft:deepslate",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:raw_iron",5),
                                    createReward("minecraft:cobbled_deepslate",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:iron_ore",
                            4,
                            "minecraft:stone",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:raw_iron",5),
                                    createReward("minecraft:cobblestone",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:deepslate_copper_ore",
                            8,
                            "minecraft:deepslate",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:raw_copper",5),
                                    createReward("minecraft:cobbled_deepslate",3)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:copper_ore",
                            8,
                            "minecraft:stone",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:raw_copper",5),
                                    createReward("minecraft:cobblestone",3)
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
                            0,
                            "minecraft:air",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:cobbled_deepslate", 9),
                                    createReward("minecraft:coal", 1)
                            }),
                    new MinerOreRewardMap(
                            "minecraft:stone",
                            0,
                            "minecraft:air",
                            new MinerOreRewardMap.WeightedReward[]{
                                    createReward("minecraft:cobblestone", 9),
                                    createReward("minecraft:coal", 1)
                            })
            );
            try {
                Files.write(Paths.get("config/miniminer.json"), gson.toJson(config).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                config = gson.fromJson(Files.readString(Paths.get("config/miniminer.json")), Config.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Config{
       public ArrayList<MinerOreRewardMap> minerOreRewardMap = new ArrayList<>();
        public void add(MinerOreRewardMap... minerOreRewardMap){
            this.minerOreRewardMap.addAll(Arrays.asList(minerOreRewardMap));
        }
    }

    public static class MinerOreRewardMap{
        String ore;
        int depleteAt;
        String depleted;
        WeightedReward[] reward;
        public MinerOreRewardMap(String ore,int depleteAt,String depleted, WeightedReward[] reward) {
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

        public static class WeightedReward{
            String item;
            int weight;
            public WeightedReward(String item, int weight) {
                this.item = item;
                this.weight = weight;
            }
            public static WeightedReward createReward(String item, int weight){
                return new WeightedReward(item,weight);
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
