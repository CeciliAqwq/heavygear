package com.cecilia.heavygear.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.WindChargeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WindChargeRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/** Renders a wind charge with its own texture; used for the darkened compressed variants. */
public class DarkWindChargeRenderer extends WindChargeRenderer {
    private final WindChargeModel heavygear$model;
    private final Identifier heavygear$texture;

    public DarkWindChargeRenderer(EntityRendererProvider.Context context, Identifier texture) {
        super(context);
        this.heavygear$model = new WindChargeModel(context.bakeLayer(ModelLayers.WIND_CHARGE));
        this.heavygear$texture = texture;
    }

    @Override
    public void submit(EntityRenderState state, PoseStack pose, SubmitNodeCollector collector,
                       CameraRenderState camera) {
        RenderType type = RenderTypes.entityCutout(this.heavygear$texture);
        collector.submitModel(this.heavygear$model, state, pose, type,
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }
}
