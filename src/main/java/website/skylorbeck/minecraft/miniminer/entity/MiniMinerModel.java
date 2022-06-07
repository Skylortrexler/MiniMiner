package website.skylorbeck.minecraft.miniminer.entity;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import website.skylorbeck.minecraft.miniminer.Miniminer;

public class MiniMinerModel extends AnimatedGeoModel<MiniMinerBlockEntity> {
    @Override
    public Identifier getAnimationResource(MiniMinerBlockEntity entity) {
        return Miniminer.getId("animations/miner.animation.json");
    }

    @Override
    public Identifier getModelResource(MiniMinerBlockEntity animatable) {
        return Miniminer.getId("geo/miniminer.geo.json");
    }

    @Override
    public Identifier getTextureResource(MiniMinerBlockEntity entity) {
        String name = entity.getDisplayName().getString();
        if (name.equalsIgnoreCase("NoobGamer")){
            return Miniminer.getId("textures/asthetic.png");
        } else if (name.equalsIgnoreCase("citrus")){
            return Miniminer.getId("textures/citrus.png");
        } else if (name.equalsIgnoreCase("halfrottenegg")){
            return Miniminer.getId("textures/fancy.png");
        } else if (name.equalsIgnoreCase("tigris")){
            return Miniminer.getId("textures/icecream.png");
        } else if (name.equalsIgnoreCase("maliwat")){
            return Miniminer.getId("textures/maliwat.png");
        } else if (name.equalsIgnoreCase("r2zoo")){
            return Miniminer.getId("textures/rickard.png");
        } else if (name.equalsIgnoreCase("jafjay")){
            return Miniminer.getId("textures/hyperiron.png");
        } else if (name.equalsIgnoreCase("SkylorBeck")){
            return Miniminer.getId("textures/skylor.png");
        } else if (name.equalsIgnoreCase("Striker")){
            return Miniminer.getId("textures/error.png");
        } else
        return Miniminer.getId("textures/main.png");
    }

    @Override
    public void setLivingAnimations(MiniMinerBlockEntity entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }

}
