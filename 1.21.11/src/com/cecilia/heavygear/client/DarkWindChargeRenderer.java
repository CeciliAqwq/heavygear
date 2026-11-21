package com.cecilia.heavygear.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.WindChargeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WindChargeRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/** Renders a wind charge with its own texture; used for the darkened compressed variants. */
public class DarkWindChargeRenderer extends WindChargeRenderer {
    private final WindChargeModel heavygear$model;
    private final ResourceLocation heavygear$texture;

    public DarkWindChargeRenderer(EntityRendererProvider.Context context, ResourceLocation texture) {
        super(context);
        this.heavygear$model = new WindChargeModel(context.bakeLayer(ModelLayers.WIND_CHARGE));
        this.heavygear$texture = texture;
    }

    @Override
    public void method_3936(net.minecraft.class_10017 state, PoseStack pose,
                             net.minecraft.class_11659 buffers, net.minecraft.class_12075 camera) {
        RenderType type = net.minecraft.class_12249.method_75967(this.heavygear$texture,
                this.xOffset(state.field_53328) % 1.0F, 0.0F);
        buffers.method_73489(this.heavygear$model, state, pose, type,
                state.field_61820, OverlayTexture.NO_OVERLAY, state.field_61821, null);
    }
}