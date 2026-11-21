package com.cecilia.heavygear.item;

import com.cecilia.heavygear.HeavyGearMod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;

import java.util.List;

/**
 * Heavy variants of the netherite loadout: sword, axe, shovel, pickaxe and hoe plus the
 * four armour pieces. Every number (durability, damage, mining speed, defence, toughness,
 * knockback resistance, enchantability, repair ingredient, fire resistance) is taken from
 * the vanilla netherite equivalent, so the only difference is the model, the name and the
 * armour texture.
 *
 * <p>1.21.11 dropped {@code SwordItem} / {@code PickaxeItem} / {@code ArmorItem}; those items
 * are plain {@link Item}s whose components are baked by the {@code Item.Properties} factories
 * that vanilla itself uses ({@code Item.Properties#sword}, {@code #pickaxe}, {@code #humanoidArmor}).
 * The factories were not part of the 1.21.1 mapping table, so they are referenced by their
 * intermediary ids here (the mod is compiled against a Mojang-named client jar in which those
 * members keep their intermediary names).</p>
 */
public final class HeavyGear {
    private HeavyGear() {
    }

    /** {@code assets/heavygear/equipment/heavy.json}: the custom armour layer texture set. */
    private static final ResourceKey<net.minecraft.class_10394> EQUIPMENT_ASSET = ResourceKey.create(
            net.minecraft.class_10191.field_55214,
            ResourceLocation.fromNamespaceAndPath(HeavyGearMod.MOD_ID, "heavy"));

    /** Netherite armour material, re-skinned with our own equipment asset. */
    private static final ArmorMaterial HEAVY_ARMOR_MATERIAL = new ArmorMaterial(
            ArmorMaterials.field_21977.comp_3166(),
            ArmorMaterials.field_21977.defense(),
            ArmorMaterials.field_21977.comp_3167(),
            ArmorMaterials.field_21977.equipSound(),
            ArmorMaterials.field_21977.toughness(),
            ArmorMaterials.field_21977.knockbackResistance(),
            ArmorMaterials.field_21977.comp_2301(),
            EQUIPMENT_ASSET);

    public static final ResourceKey<Item> SWORD_KEY = key("heavy_sword");
    public static final ResourceKey<Item> AXE_KEY = key("heavy_axe");
    public static final ResourceKey<Item> SHOVEL_KEY = key("heavy_shovel");
    public static final ResourceKey<Item> PICKAXE_KEY = key("heavy_pickaxe");
    public static final ResourceKey<Item> HOE_KEY = key("heavy_hoe");
    public static final ResourceKey<Item> HELMET_KEY = key("heavy_helmet");
    public static final ResourceKey<Item> CHESTPLATE_KEY = key("heavy_chestplate");
    public static final ResourceKey<Item> LEGGINGS_KEY = key("heavy_leggings");
    public static final ResourceKey<Item> BOOTS_KEY = key("heavy_boots");

    // 26.3 vanilla tool numbers, verbatim from Items (netherite_*).
    public static final Item HEAVY_SWORD = new Item(base(SWORD_KEY)
            .method_66333(ToolMaterial.NETHERITE, 3.0F, -2.4F));
    public static final Item HEAVY_AXE = new AxeItem(ToolMaterial.NETHERITE, 5.0F, -3.0F, base(AXE_KEY));
    public static final Item HEAVY_SHOVEL = new ShovelItem(ToolMaterial.NETHERITE, 1.5F, -3.0F, base(SHOVEL_KEY));
    public static final Item HEAVY_PICKAXE = new Item(base(PICKAXE_KEY)
            .method_66330(ToolMaterial.NETHERITE, 1.0F, -2.8F));
    public static final Item HEAVY_HOE = new HoeItem(ToolMaterial.NETHERITE, -4.0F, 0.0F, base(HOE_KEY));

    public static final Item HEAVY_HELMET = new HeavyArmorItem(armor(HELMET_KEY, net.minecraft.class_8051.field_41934));
    public static final Item HEAVY_CHESTPLATE = new HeavyArmorItem(armor(CHESTPLATE_KEY, net.minecraft.class_8051.field_41935));
    public static final Item HEAVY_LEGGINGS = new HeavyArmorItem(armor(LEGGINGS_KEY, net.minecraft.class_8051.field_41936));
    public static final Item HEAVY_BOOTS = new HeavyArmorItem(armor(BOOTS_KEY, net.minecraft.class_8051.field_41937));

    public static final ResourceKey<Item> BOW_KEY = key("heavy_bow");
    public static final ResourceKey<Item> CROSSBOW_KEY = key("heavy_crossbow");
    public static final ResourceKey<Item> FISHING_ROD_KEY = key("heavy_fishing_rod");
    public static final ResourceKey<Item> ARROW_KEY = key("heavy_arrow");
    public static final ResourceKey<Item> ECAM_KEY = key("ecam");
    public static final ResourceKey<Item> COMPRESSED_WIND_CHARGE_KEY = key("compressed_wind_charge");
    public static final ResourceKey<Item> POTION_KEY = key("heavy_potion");
    public static final ResourceKey<Item> SPLASH_POTION_KEY = key("heavy_splash_potion");
    public static final ResourceKey<Item> LINGERING_POTION_KEY = key("heavy_lingering_potion");

    /** Vanilla bow numbers (durability 384, 1 durability per shot). */
    public static final Item HEAVY_BOW = new HeavyBowItem(base(BOW_KEY).durability(384));
    /** Vanilla crossbow numbers (durability 465, 1.25 s charge). */
    public static final Item HEAVY_CROSSBOW = new HeavyCrossbowItem(base(CROSSBOW_KEY).durability(465));
    /** Vanilla fishing rod numbers (durability 64). */
    public static final Item HEAVY_FISHING_ROD = new HeavyFishingRodItem(base(FISHING_ROD_KEY).durability(64));
    /** Ammo for the heavy bow / crossbow. */
    public static final Item HEAVY_ARROW = new HeavyArrowItem(base(ARROW_KEY).stacksTo(64));
    /** 压缩风弹: nine wind charges, five times the impact of one. */
    public static final Item COMPRESSED_WIND_CHARGE = new com.cecilia.heavygear.item.CompressedWindChargeItem(
            base(COMPRESSED_WIND_CHARGE_KEY).stacksTo(64).rarity(Rarity.COMMON), 10,
            (level) -> new com.cecilia.heavygear.entity.CompressedWindCharge(
                    com.cecilia.heavygear.HeavyGearMod.COMPRESSED_WIND_CHARGE_TYPE, level));

    /** 二重压缩风弹: four compressed wind charges, four times the impact of one wind charge. */
    public static final ResourceKey<Item> DOUBLE_COMPRESSED_WIND_CHARGE_KEY = key("double_compressed_wind_charge");
    public static final Item DOUBLE_COMPRESSED_WIND_CHARGE = new com.cecilia.heavygear.item.CompressedWindChargeItem(
            base(DOUBLE_COMPRESSED_WIND_CHARGE_KEY).stacksTo(64).rarity(Rarity.EPIC), 10,
            (level) -> new com.cecilia.heavygear.entity.DoubleCompressedWindCharge(
                    com.cecilia.heavygear.HeavyGearMod.DOUBLE_COMPRESSED_WIND_CHARGE_TYPE, level));

    /** 锤重 / ecaM: the reverse mace - the smash lands on the wielder. */
    public static final Item ECAM = new Item(ecamProperties());

    /** Drinkable heavy potion (potion bottle look, tinted with the effect colour). */
    public static final Item HEAVY_POTION = new HeavyPotionItem(base(POTION_KEY).stacksTo(1));
    /** Splash variant. */
    public static final Item HEAVY_SPLASH_POTION =
            new HeavyPotionItem(base(SPLASH_POTION_KEY).stacksTo(1));
    /** Lingering variant. */
    public static final Item HEAVY_LINGERING_POTION =
            new HeavyPotionItem(base(LINGERING_POTION_KEY).stacksTo(1));

    /** Creative tab {@code combat}: weapons and armour. */
    public static final List<Item> COMBAT = List.of(
            HEAVY_SWORD, HEAVY_AXE, HEAVY_HELMET, HEAVY_CHESTPLATE, HEAVY_LEGGINGS, HEAVY_BOOTS);
    /** Creative tab {@code tools}. */
    public static final List<Item> TOOLS = List.of(HEAVY_PICKAXE, HEAVY_SHOVEL, HEAVY_HOE);

    private record Entry(ResourceKey<Item> key, Item item) {
    }

    private static final List<Entry> ENTRIES = List.of(
            new Entry(SWORD_KEY, HEAVY_SWORD),
            new Entry(AXE_KEY, HEAVY_AXE),
            new Entry(SHOVEL_KEY, HEAVY_SHOVEL),
            new Entry(PICKAXE_KEY, HEAVY_PICKAXE),
            new Entry(HOE_KEY, HEAVY_HOE),
            new Entry(HELMET_KEY, HEAVY_HELMET),
            new Entry(CHESTPLATE_KEY, HEAVY_CHESTPLATE),
            new Entry(LEGGINGS_KEY, HEAVY_LEGGINGS),
            new Entry(BOOTS_KEY, HEAVY_BOOTS),
            new Entry(BOW_KEY, HEAVY_BOW),
            new Entry(CROSSBOW_KEY, HEAVY_CROSSBOW),
            new Entry(FISHING_ROD_KEY, HEAVY_FISHING_ROD),
            new Entry(ARROW_KEY, HEAVY_ARROW),
            new Entry(ECAM_KEY, ECAM),
            new Entry(COMPRESSED_WIND_CHARGE_KEY, COMPRESSED_WIND_CHARGE),
            new Entry(DOUBLE_COMPRESSED_WIND_CHARGE_KEY, DOUBLE_COMPRESSED_WIND_CHARGE),
            new Entry(POTION_KEY, HEAVY_POTION),
            new Entry(SPLASH_POTION_KEY, HEAVY_SPLASH_POTION),
            new Entry(LINGERING_POTION_KEY, HEAVY_LINGERING_POTION));

    private static ResourceKey<Item> key(String path) {
        return ResourceKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeavyGearMod.MOD_ID, path));
    }

    /** Mace attack damage / speed and mace enchantability, so Density, Breach and Wind Burst apply. */
    private static Item.Properties ecamProperties() {
        return base(ECAM_KEY).stacksTo(1)
                .attributes(net.minecraft.world.item.MaceItem.createAttributes())
                .component(net.minecraft.core.component.DataComponents.field_53695,
                        new net.minecraft.class_9889(15));
    }

    private static Item.Properties base(ResourceKey<Item> key) {
        return new Item.Properties()
                .setId(key)
                .rarity(Rarity.EPIC)
                .fireResistant();
    }

    private static Item.Properties armor(ResourceKey<Item> key, net.minecraft.class_8051 type) {
        return base(key).method_66332(HEAVY_ARMOR_MATERIAL, type);
    }

    /** Registers every heavy tool and armour piece into the item registry. */
    public static void register() {
        for (Entry entry : ENTRIES) {
            Registry.register(BuiltInRegistries.ITEM, entry.key(), entry.item());
        }
    }
}
