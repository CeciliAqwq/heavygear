package com.cecilia.heavygear.item;

import net.minecraft.world.entity.player.Player;

/**
 * Remembers whether the player has ever held an ecaM, so the tooltip hint can change from the
 * cryptic line to the real one once the item has actually been crafted.
 */
public final class EcamKnown {
    private static boolean known;

    private EcamKnown() {
    }

    public static boolean isKnown(Player player) {
        if (known) {
            return true;
        }
        if (player == null) {
            return false;
        }
        if (player.getInventory().contains(stack -> stack.is(HeavyGear.ECAM))) {
            known = true;
            return true;
        }
        return false;
    }

    public static void mark() {
        known = true;
    }
}