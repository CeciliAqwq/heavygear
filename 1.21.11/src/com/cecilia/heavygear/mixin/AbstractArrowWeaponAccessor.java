package com.cecilia.heavygear.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Lets the heavy arrow record the bow / crossbow it was fired from and read its base damage. */
@Mixin(AbstractArrow.class)
public interface AbstractArrowWeaponAccessor {
    @Accessor("field_51620")
    void heavygear$setFiredFromWeapon(ItemStack stack);

    @Accessor("field_7571")
    double heavygear$getBaseDamage();
}
