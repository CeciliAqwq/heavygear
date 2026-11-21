package com.cecilia.heavygear.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Draws the heavy arrow with its own projectile texture.
 *
 * <p>{@code ArrowRenderer} fetches the texture through {@code method_4120(state)} (the abstract
 * state-based accessor); {@code TippableArrowRenderer} implements it as {@code method_4130(state)}.
 * Both are overridden here so the heavy texture wins whichever one the render pipeline asks for.</p>
 */
public class HeavyArrowRenderer extends TippableArrowRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "heavygear", "textures/entity/projectiles/heavy_arrow.png");

    public HeavyArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected ResourceLocation method_4120(net.minecraft.class_10074 state) {
        return TEXTURE;
    }

    @Override
    protected ResourceLocation method_4130(net.minecraft.class_10074 state) {
        return TEXTURE;
    }
}
