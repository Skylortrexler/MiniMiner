package website.skylorbeck.miniminer;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.GeckoLib;
import website.skylorbeck.minecraft.skylorlib.Registrar;

import java.rmi.registry.Registry;

public class Miniminer implements ModInitializer {
    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        Registrar.regBlock("miniminer",Declarar.MINIMINER,MODID);
        Registrar.regItem("miniminer",Declarar.MINIMINER_ITEM,MODID);
    }

    public static String MODID = "miniminer";
    public static Identifier getId(String name) {
        return new Identifier(MODID, name);
    }
}
