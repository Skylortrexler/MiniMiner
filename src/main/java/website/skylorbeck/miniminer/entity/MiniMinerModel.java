package website.skylorbeck.miniminer.entity;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import website.skylorbeck.miniminer.Miniminer;

public class MiniMinerModel extends AnimatedGeoModel<MiniMinerBlockEntity> {
    @Override
    public Identifier getAnimationFileLocation(MiniMinerBlockEntity entity) {
        return Miniminer.getId("animations/miner.animation.json");
    }

    @Override
    public Identifier getModelLocation(MiniMinerBlockEntity animatable) {
        return Miniminer.getId("geo/miniminer.geo.json");
    }

    @Override
    public Identifier getTextureLocation(MiniMinerBlockEntity entity) {
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
