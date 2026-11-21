# heavygear 四版本测试报告

> 内部开发笔记：文中提到的 .hgtest/port/* 脚本与日志是本机测试工具，没有随仓库发布。

测试时间：2026-11-21 15:30–16:40
被测 jar：`1.21.11/heavygear-1.0.0+1.21.11.jar`、`26.1.2/heavygear-1.0.0+26.1.2.jar`、`26.2/heavygear-1.0.0+26.2.jar`、`26.3/heavygear-1.0.0+26.3.jar`（含本次修复，见「发现的问题」）

## 测试方法

| 版本 | 方式 | 说明 |
|---|---|---|
| 26.1.2 / 26.2 / 26.3 | **真服务端 + RCON 自动脚本** | 26.x 客户端 jar 就是未混淆的，可直接当服务端跑；用 `/loot` `/summon` `/kill` `/item` `/enchant` `/data` 做功能验证 |
| 1.21.11 | **仅静态数据审计** | 1.21.11 是混淆版本，本机没有服务端 jar，也无网络下载；用「mod 数据 vs 该版本客户端 jar 里的原版数据」逐项比对 |

脚本：`.hgtest/port/{test_mod.py,test_brewing.py,test_rates.py,audit_data.py,audit_cross.py,audit_detail.py}`

## 一、总体结果

| 检查项 | 1.21.11 | 26.1.2 | 26.2 | 26.3 |
|---|---|---|---|---|
| mod 加载 / 数据包解析错误 | 静态 ✓ | 0 | 0 | 0 |
| 21 个物品全部注册 | 静态 ✓ | ✓ | ✓ | ✓ |
| 5 个工作台配方可合成 | — | ✓ | ✓ | ✓ |
| 14 个锻造配方（材料/产物存在） | 静态 ✓ | ✓ | ✓ | ✓ |
| 附魔可附（密度/破甲/风爆/无限/引雷） | 静态 ✓ | ✓ | ✓ | ✓ |
| 沉重药水：效果生效 | — | ✓ | ✓ | ✓ |
| 沉重药水：可酿造（短/长） | 静态/有 mixin | ✓ | ✓ | ✓ |
| 三叉戟模板：远古守卫者 | 静态 0.5 | 44.2% | 51.7% | 46.7% |
| 三叉戟模板：末地城 | 静态 0.1 | 11.5% | 11.0% | 8.5% |
| 沉重模板：不祥宝库 | 静态 16.7% | 17.0% | 16.0% | 18.8% |
| 不祥宝库内无三叉戟模板 | 静态 ✓ | 0/400 | 0/400 | 0/400 |

（爆率样本：守卫者 240 杀 / 末地城 200 次 / 宝库 400 次；理论值 50% / 10% / 16.67%）

## 二、发现的问题（已修复并复测）

### 1. 26.1.2 / 26.2 / 26.3：沉重药水**无法酿造**（只能指令/创造拿）

- 1.21.11 有 `PotionBrewingHeavyMixin`（粗制药水 + 沉重核心 → 沉重药水，再加红石 → 长效），
  迁移到 26.x 时这条链路**丢了**：26.x jar 里既没有酿造 mixin，也没有酿造配方 → 生存模式拿不到药水。
- 原因：26.3 把酿造改成了**数据驱动**（`minecraft:brewing` 配方，`PotionBrewing` 类被删），旧 mixin 无法直接编译。
- 修复：
  - 26.3：新增数据配方 `data/heavygear/recipe/brewing/heavy_potion{,_long}.json`；
  - 26.1.2 / 26.2：`PotionBrewing.addVanillaMixes` 注入（`addMix(粗制, 沉重核心, heavy)`、`addMix(heavy, 红石, heavy_long)`）。
- 复测：三个版本各跑完整酿造链（水→粗制→沉重→长效），**3/3 通过**。

### 2. 26.x：缺少 `RiptideLungeGuardMixin`（激流冲刺时仍触发 Lunge 附魔）

- 1.21.11 里这个 mixin 会在 `Player#postPiercingAttack` 时，如果玩家正在激流冲刺就取消——
  否则冲刺中每撞一个怪都会播突刺音效、推玩家、额外消耗耐久。
- 26.x 迁移时整条丢了（mixin 列表里没有）。
- 修复：26.x 的方法在 `LivingEntity#postPiercingAttack`，重新实现为
  `@Mixin(LivingEntity.class)` + `instanceof Player && isAutoSpinAttack()` 才取消（不影响怪物）。
- 复测：三个服务端都能正常启动（`defaultRequire: 1`，注入点找不到会直接报错），说明目标方法命中。

### 3. 附带确认（不是 bug）

- 1.21.11 的 `density`/`breach` 附魔 JSON 多了 `primary_items` 字段（26.x 才有），1.21.11 原版没有该字段；
  Codec 会忽略未知字段，**不影响功能**，属于无害冗余。
- 宝库 `reward_ominous_unique` 里没有 `bonus_rolls`（原版有，缺省 0），无影响。

## 三、静态审计（1.21.11 与全部版本对比）

- 引用的 26 个 `minecraft:*` 物品 id 在四个版本里**全部存在**（含 1.21.11 的 `netherite_spear` 等，
  1.21.11 确实有矛）。
- 19 个配方在四个版本里**完全一致**（工作台 5 + 锻造 14）。
- 五个附魔的 `supported_items` 四版本一致：
  - `density` / `breach` → `#heavygear:enchantable/smash_armor`
  - `wind_burst` → `#heavygear:enchantable/wind_burst`
  - `channeling` → `#minecraft:enchantable/trident`（含长枪）
  - `infinity` → `#minecraft:enchantable/bow`（含重弓）
- 战利品表 schema 与各版本原版一致（1.21.11 用 `conditions`/`condition` + `random_chance`；
  26.x 用新格式），概率条件：守卫者 0.5、末地城 0.1、宝库沉重 weight 2 / 总 12。

## 四、能自动测 / 不能自动测

已验证（服务端可判定）：物品注册、配方可合成（用 crafter 自动合成）、附魔可否附上、药水效果与酿造、战利品爆率、数据包解析。

**无法在无头环境判定，需要你手动确认**：

1. 附魔的**实际效果数值**（密度加成、风爆触发、破甲减甲、引雷落雷）——要在玩家攻击/坠落流程里才体现；
2. 沉重药水的**玩法效果**（任何武器视为重武器、砸地、顶部补伤）——要玩家操作；
3. 锻造台 GUI 的实际合成（我这只验证了「配方存在 + 材料/产物物品都存在」，无法点 GUI）；
4. 客户端表现：重弩上弦动画、三叉戟渲染、tooltip 等。

## 五、交付

| 文件 | 版本 | 本次改动 |
|---|---|---|
| `26.3/heavygear-1.0.0+26.3.jar` | 26.3 | 沉重药水酿造配方（数据驱动）、激流突刺守卫 mixin |
| `26.2/heavygear-1.0.0+26.2.jar` | 26.2 | 沉重药水酿造 mixin、激流突刺守卫 mixin |
| `26.1.2/heavygear-1.0.0+26.1.2.jar` | 26.1.2 | 同上 |
| `1.21.11/heavygear-1.0.0+1.21.11.jar` | 1.21.11 | 未改动（静态审计通过） |

四个 jar 的版本号已统一改为 **1.0.0**（文件名 `heavygear-1.0.0+<MC版本>.jar`），旧的 2.4.43 / 2.5.3 / 2.5.4 文件已删除，直接替换实例里的旧 jar 即可；26.2 实例记得先删 `heavyspear-1.2.4+26.2.jar`。
