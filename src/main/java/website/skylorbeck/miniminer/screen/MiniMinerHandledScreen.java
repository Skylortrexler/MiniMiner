package website.skylorbeck.miniminer.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import website.skylorbeck.miniminer.Miniminer;

public class MiniMinerHandledScreen extends HandledScreen<MiniMinerScreenHandler> {
    private static final Identifier TEXTURE = Miniminer.getId("textures/gui/miner.png");

    public MiniMinerHandledScreen(MiniMinerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 133;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        int k;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        if (this.handler.isBurning()) {
            k = this.handler.getFuelProgress();
            this.drawTexture(matrices, i + 66, j + 32 - k, 176, 12 - k, 14, k + 1);
        }
        k = this.handler.getCookProgress();
        this.drawTexture(matrices, i + 84, j + 16, 176, 14, k + 1, 18);
        if (k>=31)
        this.drawTexture(matrices, i + 118, j + 17, 176, 33, 17, 17);
    }
}
