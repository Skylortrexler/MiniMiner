package website.skylorbeck.minecraft.miniminer.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import website.skylorbeck.minecraft.miniminer.Declarar;
import website.skylorbeck.minecraft.miniminer.entity.MiniMinerRenderer;
import website.skylorbeck.minecraft.miniminer.item.MiniMinerItemRenderer;
import website.skylorbeck.minecraft.miniminer.screen.MiniMinerHandledScreen;

@Environment(EnvType.CLIENT)
public class MiniminerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(Declarar.MINI_MINER_BLOCK_ENTITY_TYPE,
                (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new MiniMinerRenderer());
        GeoItemRenderer.registerItemRenderer(Declarar.MINIMINER_ITEM,new MiniMinerItemRenderer());
        ScreenRegistry.register(Declarar.MINIMINER_SCREEN_HANDLER, MiniMinerHandledScreen::new);
    }
}
