package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.item.CompressedWindChargeMarker;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Compressed wind charges knock everything around five times as hard: the velocities the vanilla
 * explosion writes are snapshotted, and the delta it added is multiplied by five afterwards.
 */
@Mixin(WindCharge.class)
public abstract class CompressedWindChargeKnockbackMixin {
    @Unique
    private double heavygear$scale = 1.0D;
    @Unique
    private static final double HEAVYGEAR_SNAPSHOT_RADIUS = 8.0D;

    @Unique
    private List<Entity> heavygear$entities;
    @Unique
    private List<Vec3> heavygear$before;

    @Inject(method = "explode(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"))
    private void heavygear$snapshot(Vec3 position, CallbackInfo ci) {
        this.heavygear$entities = null;
        this.heavygear$before = null;
        WindCharge self = (WindCharge) (Object) this;
        this.heavygear$scale = ((CompressedWindChargeMarker) (Object) self).heavygear$impactScale();
        if (this.heavygear$scale <= 1.0D) {
            return;
        }
        AABB box = new AABB(position, position).inflate(HEAVYGEAR_SNAPSHOT_RADIUS);
        List<Entity> entities = self.level().getEntities(self, box);
        List<Entity> kept = new ArrayList<>(entities.size());
        List<Vec3> speeds = new ArrayList<>(entities.size());
        for (Entity entity : entities) {
            kept.add(entity);
            speeds.add(entity.getDeltaMovement());
        }
        this.heavygear$entities = kept;
        this.heavygear$before = speeds;
    }

    @Inject(method = "explode(Lnet/minecraft/world/phys/Vec3;)V", at = @At("RETURN"))
    private void heavygear$scale(Vec3 position, CallbackInfo ci) {
        if (this.heavygear$entities == null) {
            return;
        }
        for (int i = 0; i < this.heavygear$entities.size(); i++) {
            Entity entity = this.heavygear$entities.get(i);
            if (!entity.isAlive()) {
                continue;
            }
            Vec3 before = this.heavygear$before.get(i);
            Vec3 after = entity.getDeltaMovement();
            Vec3 delta = after.subtract(before);
            if (delta.lengthSqr() < 1.0E-8D) {
                continue;
            }
            entity.setDeltaMovement(before.add(delta.scale(this.heavygear$scale)));
            ((com.cecilia.heavygear.mixin.EntitySharedFlagAccessor) entity).heavygear$markHurt();
            if (entity instanceof net.minecraft.server.level.ServerPlayer player) {
                com.cecilia.heavygear.item.CompressedWindChargeImpulse.markLaunch(
                        player.getUUID(), player.getY());
                if (this.heavygear$scale > 3.0D) {
                    com.cecilia.heavygear.item.CompressedWindChargeImpulse.markDoubleLaunch(
                            player.getUUID(), player.getY());
                }
            }
        }
        this.heavygear$entities = null;
        this.heavygear$before = null;
    }
}