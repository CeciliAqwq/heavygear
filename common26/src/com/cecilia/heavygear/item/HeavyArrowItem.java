package com.cecilia.heavygear.item;

import com.cecilia.heavygear.HeavyGearMod;
import com.cecilia.heavygear.entity.HeavyArrow;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/** The heavy arrow item: spawns a {@link HeavyArrow} so the smash and the recoloured entity apply. */
public class HeavyArrowItem extends ArrowItem {
    public HeavyArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        return new HeavyArrow(HeavyGearMod.HEAVY_ARROW_TYPE, shooter, level,
                ammo.copyWithCount(1), weapon);
    }

    /** Declared under the intermediary name, like {@code LonginusSpearItem}: the mapping table has no
     * {@code Item#appendHoverText} row, so a source-level override would not compile or override. */
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                             Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable("tooltip.heavygear.heavy_arrow.smash")
                .withStyle(ChatFormatting.GRAY));
    }
}
