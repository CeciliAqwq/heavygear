# Heavy Gear · 沉重装备

> 把下界合金推到极限：会坠落猛击的重型武器与护甲、双模式命运之矛、把一切武器都变沉重的药水。
> Netherite pushed to its limit: falling-strike weapons and armour, a two-mode Spear of Destiny, and a potion that makes everything heavy.

`1.0.0` · Fabric · MC **1.21.11 / 26.1.2 / 26.2 / 26.3** · MIT · by **CeciliAqwq_**

---

# 中文

## 这是什么

**沉重装备**是一套以「重量」为核心的装备扩展。它不给你更高的面板伤害，而是把《我的世界》里的**坠落**变成武器：只要你穿着沉重护甲、拿着沉重武器，或者命中正在下坠的箭矢，落地/落点那一刻就会触发**与重锤同源的猛击**——同一套伤害曲线、同一个伤害类型、同样吃密度和破甲附魔。

一句话概括：**重锤的猛击机制，现在属于你身上所有装备。**

## 核心机制：沉重猛击

| 来源 | 触发条件 | 伤害占比 |
|---|---|---|
| 沉重护甲 | 下坠 ≥ 3 格后砸到生物 | **每件 25%**（四件 = 与同高度重锤完全一致） |
| 重箭 / 重弓 / 重弩 | 箭矢自最高点下落 ≥ 1.5 格后命中 | 25% |
| 重钓竿浮标 | 浮标下落 1.5 格后砸中钓到的生物 | 同高度猛击的 50% |
| 沉重药水 | 效果期间任意落地 | 视作**穿满整套**沉重护甲 |
| 俯冲轰炸 | 鞘翅滑翔状态下砸击 | 按滑翔下坠距离另行计算 |

- 伤害完全走原版 `MaceItem` 逻辑（`4×d`、`12+2(d−1)`、`22+d−3` 分段曲线 + 密度 + 破甲），并复刻重锤的音效、粒子和击退。
- 一次坠落对同一只生物只会猛击一次；长坠落到高个子生物（比如监守者）不会连击。

## 物品一览

### 武器与工具（锻造而来，可用猛击）

| 物品 | 说明 |
|---|---|
| 重剑 / 重斧 / 重镐 / 重锹 / 重锄 | 下界合金级别的重型工具，跳劈时触发与原版重锤相同的猛击（同一坠落曲线、吃密度、同音效与击退），可附密度、破甲、风爆 |
| 重矛 | 新武器类别；俯冲时的额外伤害并入原版猛击公式，锋利/亡灵杀手/密度都能吃到 |

### 护甲

| 物品 | 说明 |
|---|---|
| 沉重头盔 / 胸甲 / 护腿 / 之靴 | 每件提供 25% 坠落猛击；可附密度、破甲、风爆（靴子）、俯冲轰炸（头盔） |

### 远程

| 物品 | 说明 |
|---|---|
| 重弓 / 重弩 | 发射重箭；弩上弦动画与双手持弩姿势与原版一致 |
| 重箭 | 发射后下坠命中即造成 25% 猛击伤害 |

### 特殊

| 物品 | 说明 |
|---|---|
| **命运之矛** | 重矛 + 三叉戟锻造而成。两种模式（普通 / 三叉戟）一键切换，可投掷；击杀讯息为「被…改写了命运」 |
| **重钓竿** | 浮标砸中钓到的生物；配风爆会把猎物再抛上天 |
| **锤重 (ecaM)** | 反过来的重锤：你打出去的伤害由**自己**承担，并被弹开……「目的在于惩罚那些不会合成的人」 |
| 压缩风弹 | 4 个风弹合成；爆炸击退 ×5，落回起跳高度不摔伤 |
| 二重压缩风弹 | 4 个压缩风弹合成；能把你自己弹上 65 格 |

### 药水

**沉重药水**（粗制药水 + 沉重核心）：3 分钟内任何武器都按沉重武器计算——木剑也能砸地、箭矢会猛击、浮标会砸人，落地视作穿满沉重护甲。加红石可得 8 分钟长效版本；喷溅型、滞留型、沉重之箭一并可用。

## 怎么获得

**两套升级模板**

| 模板 | 用途 | 获取 |
|---|---|---|
| 沉重升级 | 下界合金装备 + 沉重核心 → 沉重装备（14 条锻造配方） | 不祥宝库独有池 ≈ **16.7%**；也可用「风弹棒 + 模板 + 沉重核心」自己复制 |
| 三叉戟升级 | 重矛 + 三叉戟 → 命运之矛 | 远古守卫者 **50%**、末地城宝箱 **10%**；可用「海晶碎片 + 模板 + 海洋之心」复制 |

**工作台配方**：压缩风弹（2×2 风弹）、二重压缩风弹（2×2 压缩风弹）、锤重（风弹棒 + 沉重核心）、两种模板复制。

## 附魔支持

| 附魔 | 可附着的沉重装备 |
|---|---|
| 密度 / 破甲 | 全部沉重护甲、重弓、重弩、重钓竿、锤重（以及原版重锤类） |
| 风爆 | 沉重之靴、重弓、重弩、重钓竿、锤重 |
| 引雷 | 命运之矛 |
| 无限 | 重弓 |
| **俯冲轰炸**（自定义，最高 I 级） | 沉重头盔与所有沉重武器 —— 鞘翅滑翔砸击专用 |

## 成就（8 个）

「终极的终极奉献」「根本没有这种武器！」「别把气氛搞这么『沉重』！」「改写命运」「百发百中」「借过一下」「何必呢」「我要飞得更高」

## 安装

1. 安装 **Fabric Loader**（1.21.11：≥ 0.16.0；26.x：≥ 0.20.0）与 **Fabric API**；
2. 把对应版本的 `heavygear-1.0.0+<MC版本>.jar` 放进 `mods/`；
3. Java：1.21.11 用 Java 21，26.x 用 Java 25。

> 版本一一对应：`+1.21.11`、`+26.1.2`、`+26.2`、`+26.3`，装错版本会在启动时报依赖不匹配。

### 从源码构建

```bash
./gradlew build                # 四个版本全编
./gradlew :v26_3:build         # 只编 26.3
```

产物：`<版本目录>/build/libs/heavygear-1.0.0+<MC版本>.jar`。

- Gradle 项目名不能带点，所以目录与项目名对应关系是：`1.21.11 → v1_21_11`、`26.1.2 → v26_1_2`、`26.2 → v26_2`、`26.3 → v26_3`
- 需要 JDK 21（1.21.11）和 JDK 25（26.x），各子项目通过 toolchain 声明
- 首次构建需要联网下载 loom / Minecraft / Fabric API

### 仓库结构

四个版本共用同一份源码，只有真正不同的文件才按版本各存一份：

| 目录 | 内容 | 文件数 |
|---|---|---|
| `common/src` | 四个版本**逐字节相同**的 Java 源码 | 15 |
| `common/resources` | 四个版本相同的数据与资源（贴图、模型、配方等） | 150 |
| `common26/src` | 只有 26.1.2 / 26.2 / 26.3 相同（与 1.21.11 不同） | 55 |
| `common26/resources` | 同上 | 1 |
| `1.21.11/` | 1.21.11 独有的 62 个源码 + 11 个资源 | — |
| `26.1.2/` `26.2/` `26.3/` | 各自独有的 10 个源码 + 10~12 个资源 | — |

每个版本的 `build.gradle` 通过
`sourceSets.main.java.srcDirs = ['../common/src', '../common26/src', 'src']`
把共享目录拼进来：改共享代码会同时作用于对应版本，只有版本差异文件需要单独维护。

---

# English

## What it is

**Heavy Gear** is an equipment expansion built around one idea: **weight**. It does not hand you bigger numbers — it turns *falling* into a weapon. Wear heavy armour, swing a heavy weapon, or land a falling arrow, and the impact triggers a **mace smash**: the exact vanilla damage curve, the exact damage type, and it still scales with Density and Breach.

In one line: **the mace's smash mechanic, now on everything you wear and swing.**

## Core mechanic: heavy smash

| Source | Trigger | Share of a mace smash |
|---|---|---|
| Heavy armour | Land on a creature after falling ≥ 3 blocks | **25% per piece** (full set = a real mace smash at that height) |
| Heavy arrow / bow / crossbow | Arrow has dropped ≥ 1.5 blocks from its apex when it hits | 25% |
| Heavy fishing bobber | Bobber has dropped 1.5 blocks when it hooks a creature | 50% of an equal-height smash |
| Potion of Heaviness | Any landing while the effect is active | Counts as a **full set** of heavy armour |
| Dive Bombing | Smash while gliding with an elytra | Scaled from the dive distance |

- Damage goes through the vanilla `MaceItem` pipeline (`4×d`, `12+2(d−1)`, `22+d−3` tiers, plus Density and Breach), and the smash sound, particles and knockback are reused verbatim.
- One fall smashes a given creature only once, so a long drop onto a tall mob (a Warden, say) will not double-dip.

## Items

### Weapons and tools (smithing, smash-capable)

| Item | Notes |
|---|---|
| Heavy Sword / Axe / Pickaxe / Shovel / Hoe | Netherite-tier heavy tools that smash exactly like the mace on a falling attack (same fall curve, Density included, same sound and knockback); they take Density, Breach and Wind Burst |
| Heavy Spear | New weapon type; its dive bonus is folded into the vanilla smash formula, so Sharpness / Smite / Density all apply |

### Armour

| Item | Notes |
|---|---|
| Heavy Helmet / Chestplate / Leggings / Boots | 25% of a smash each; Density, Breach, Wind Burst (boots) and Dive Bombing (helmet) |

### Ranged

| Item | Notes |
|---|---|
| Heavy Bow / Heavy Crossbow | Fire heavy arrows; the crossbow keeps the vanilla winding and two-handed hold animation |
| Heavy Arrow | Deals 25% of a mace smash once it has fallen |

### Special

| Item | Notes |
|---|---|
| **Spear of Destiny** | Heavy Spear + Trident. Two modes (normal / trident) on a hotkey, throwable; its kill message is "*had their fate rewritten by* …" |
| **Heavy Fishing Rod** | The bobber smashes whatever it hooks; with Wind Burst it also throws the catch into the air |
| **ecaM** | The reverse mace: the damage of your swing is paid by **you**, and you are knocked away — *"meant to punish those who cannot craft it…"* |
| Compressed Wind Charge | Crafted from 4 wind charges; ×5 blast knockback, no fall damage when you come back down to launch height |
| Double Compressed Wind Charge | Crafted from 4 compressed charges; launches you 65 blocks up |

### Potion

**Potion of Heaviness** (awkward potion + heavy core): for 3 minutes every weapon behaves like heavy gear — a wooden sword smashes the ground, arrows smash, bobbers smash, and any landing counts as a full heavy armour set. Add redstone for the 8-minute long version; splash, lingering and tipped-arrow variants included.

## How to get it

**Two smithing templates**

| Template | Use | Source |
|---|---|---|
| Heavy Upgrade | Netherite gear + heavy core → heavy gear (14 smithing recipes) | Ominous vault unique pool ≈ **16.7%**; duplicable with breeze rod + template + heavy core |
| Trident Upgrade | Heavy Spear + Trident → Spear of Destiny | Elder guardian **50%**, end city chests **10%**; duplicable with prismarine shard + template + heart of the sea |

**Crafting recipes**: compressed wind charge (2×2 wind charges), double compressed wind charge (2×2 compressed), ecaM (breeze rod + heavy core), and both template duplications.

## Enchantment support

| Enchantment | Heavy gear it fits |
|---|---|
| Density / Breach | All heavy armour, heavy bow, heavy crossbow, heavy fishing rod, ecaM (plus the vanilla mace family) |
| Wind Burst | Heavy boots, heavy bow, heavy crossbow, heavy fishing rod, ecaM |
| Channeling | Spear of Destiny |
| Infinity | Heavy bow |
| **Dive Bombing** (custom, max I) | Heavy helmet and every heavy weapon — for elytra dive smashes |

## Advancements (8)

"The Ultimate Ultimate Offering", "There Is No Such Weapon!", "No Need to Be So Heavy-Handed!", "Rewriting Fate", "Every Shot Tells", "Excuse Me, Coming Through", "Why Bother", "I Want to Fly Higher"

## Installation

1. Install **Fabric Loader** (1.21.11: ≥ 0.16.0; 26.x: ≥ 0.20.0) and **Fabric API**;
2. Drop the matching `heavygear-1.0.0+<MC version>.jar` into `mods/`;
3. Java: 21 for 1.21.11, 25 for 26.x.

> Builds are version specific: `+1.21.11`, `+26.1.2`, `+26.2`, `+26.3`. Loading the wrong one fails the dependency check at startup.

### Building from source

```bash
./gradlew build                # all four versions
./gradlew :v26_3:build         # just 26.3
```

The jar lands in `<version folder>/build/libs/heavygear-1.0.0+<MC version>.jar`.

- Gradle project names cannot contain dots, so the folders map to `1.21.11 → v1_21_11`, `26.1.2 → v26_1_2`, `26.2 → v26_2`, `26.3 → v26_3`
- JDK 21 (1.21.11) and JDK 25 (26.x) are required; each subproject declares its toolchain
- The first build downloads loom, Minecraft and Fabric API, so it needs network access

### Repository layout

All four versions share one source tree; only the files that really differ are stored per version:

| Folder | Contents | Files |
|---|---|---|
| `common/src` | Java sources **byte identical** in all four versions | 15 |
| `common/resources` | Data and assets identical in all four versions | 150 |
| `common26/src` | Sources identical in 26.1.2 / 26.2 / 26.3 only (they differ from 1.21.11) | 55 |
| `common26/resources` | Same | 1 |
| `1.21.11/` | 1.21.11 specific: 62 sources + 11 resources | — |
| `26.1.2/` `26.2/` `26.3/` | 10 sources + 10~12 resources each | — |

Each version's `build.gradle` pulls the shared folders in with
`sourceSets.main.java.srcDirs = ['../common/src', '../common26/src', 'src']`,
so a fix in shared code applies to every version that uses it and only the real deltas need
separate maintenance.
