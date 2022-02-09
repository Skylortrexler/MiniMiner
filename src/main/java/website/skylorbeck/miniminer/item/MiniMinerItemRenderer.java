package website.skylorbeck.miniminer.item;

import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MiniMinerItemRenderer  extends GeoItemRenderer<MiniMinerItem> {
    public MiniMinerItemRenderer() {
        super(new MiniMinerItemModel());
    }
}
