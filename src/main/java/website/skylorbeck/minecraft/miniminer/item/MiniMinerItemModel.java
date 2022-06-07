package website.skylorbeck.minecraft.miniminer.item;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import website.skylorbeck.minecraft.miniminer.Miniminer;

public class MiniMinerItemModel extends AnimatedGeoModel<MiniMinerItem> {
    @Override
    public Identifier getAnimationResource(MiniMinerItem entity) {
        return Miniminer.getId("animations/miner.animation.json");
    }

    @Override
    public Identifier getModelResource(MiniMinerItem animatable) {
        return Miniminer.getId("geo/miniminer.geo.json");
    }

    @Override
    public Identifier getTextureResource(MiniMinerItem entity) {
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
