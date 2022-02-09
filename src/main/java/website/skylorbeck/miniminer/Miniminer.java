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
            config.minerOreRewardMap.add(
                    new MinerOreRewardMap(
                            "minecraft:coal_ore",
                            4,
                            "minecraft:stone",
                            new MinerOreRewardMap.WeightedReward[]{
                                    new MinerOreRewardMap.WeightedReward("minecraft:coal", 5),
                                    new MinerOreRewardMap.WeightedReward("minecraft:cobblestone", 5)
                            }));
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
