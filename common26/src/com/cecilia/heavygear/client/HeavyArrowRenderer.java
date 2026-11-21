package com.cecilia.heavygear.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.client.renderer.entity.state.TippableArrowRenderState;
import net.minecraft.resources.Identifier;

/**
 * Draws the heavy arrow with its own projectile texture.
 *
 * <p>26.3's {@code ArrowRenderer} asks subclasses for the texture through
 * {@code getTextureLocation(S state)}; {@code TippableArrowRenderer} overrides that with the tipped
 * arrow texture, so this subclass overrides it once more to always return the heavy texture.</p>
 */
public class HeavyArrowRenderer extends TippableArrowRenderer {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            "heavygear", "textures/entity/projectiles/heavy_arrow.png");

    public HeavyArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier getTextureLocation(TippableArrowRenderState state) {
        return TEXTURE;
    }
}
