package com.cecilia.heavygear.item;

import com.cecilia.heavygear.dive.DiveTracker;
import com.cecilia.heavygear.enchant.HeavyGearEnchantments;
import com.cecilia.heavygear.mixin.FireworkRocketAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.phys.AABB;

/**
 * Heavy spear. The dive bonus is folded into the vanilla smash calculation by the
 * DiveSmashDamageMixin so damage enchantments (Smite, Sharpness, Density) keep working.
 */
public class HeavySpearItem extends MaceItem {
    public HeavySpearItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean diving = attacker.isFallFlying()
                && HeavyGearEnchantments.diveBombingLevel(stack) > 0
                && DiveTracker.descended(attacker) >= 1.5;
        double savedFallDistance = attacker.fallDistance;
        if (diving) {
            cancelFireworkBoost(attacker);
            attacker.fallDistance = Math.min(DiveTracker.descended(attacker), 20.0);
        }
        super.hurtEnemy(stack, target, attacker);
        attacker.fallDistance = savedFallDistance;
    }

    private static void cancelFireworkBoost(LivingEntity attacker) {
        if (!(attacker.level() instanceof ServerLevel level)) {
            return;
        }
        AABB box = attacker.getBoundingBox().inflate(8.0);
        for (FireworkRocketEntity rocket : level.getEntitiesOfClass(FireworkRocketEntity.class, box)) {
            if (((FireworkRocketAccessor) rocket).heavygear$attachedTo() == attacker) {
                rocket.discard();
            }
        }
    }
}
