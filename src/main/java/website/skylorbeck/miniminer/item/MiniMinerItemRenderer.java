package website.skylorbeck.miniminer.item;

import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import website.skylorbeck.miniminer.item.MiniMinerItem;
import website.skylorbeck.miniminer.item.MiniMinerItemModel;

public class MiniMinerItemRenderer  extends GeoItemRenderer<MiniMinerItem> {
    public MiniMinerItemRenderer() {
        super(new MiniMinerItemModel());
    }
}
