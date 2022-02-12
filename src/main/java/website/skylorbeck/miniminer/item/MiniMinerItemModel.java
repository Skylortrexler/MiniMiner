package website.skylorbeck.miniminer.item;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import website.skylorbeck.miniminer.Miniminer;

public class MiniMinerItemModel extends AnimatedGeoModel<MiniMinerItem> {
    @Override
    public Identifier getAnimationFileLocation(MiniMinerItem entity) {
        return Miniminer.getId("animations/miner.animation.json");
    }

    @Override
    public Identifier getModelLocation(MiniMinerItem animatable) {
        return Miniminer.getId("geo/miniminer.geo.json");
    }

    @Override
    public Identifier getTextureLocation(MiniMinerItem entity) {
        String name = entity.getName().asString();
        if (name.equalsIgnoreCase("asthetic")){
            return Miniminer.getId("textures/asthetic.png");
        } else if (name.equalsIgnoreCase("citrus")){
            return Miniminer.getId("textures/citrus.png");
        } else if (name.equalsIgnoreCase("fancy")){
            return Miniminer.getId("textures/fancy.png");
        } else if (name.equalsIgnoreCase("icecream")){
            return Miniminer.getId("textures/icecream.png");
        } else if (name.equalsIgnoreCase("maliwat")){
            return Miniminer.getId("textures/maliwat.png");
        } else
        return Miniminer.getId("textures/main.png");
    }

    @Override
    public void setLivingAnimations(MiniMinerItem entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }

}
