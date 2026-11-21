# heavygear 迁移到 26.1.2 / 26.2

新增两个版本目录（与 `1.21.11/`、`26.3/` 同级，结构相同）：

```
26.1.2/  src/ resources/ stub/ heavygear-1.0.0+26.1.2.jar
26.2/    src/ resources/ stub/ heavygear-1.0.0+26.2.jar
```

代码基线是 `26.3/src`（它已经是 26.x 命名：`Identifier`、`projectile.arrow.*`），
26.1.2 与 26.2 的 API 在这批代码上用到的部分几乎一致，实际差异只有 4 处；
数据侧（战利品表 / 附魔 / 成就）差别更大，因为**26.3 改了这几类 JSON 的字段名**。

## 源码差异（4 处，两版相同）

| 26.3 写法 | 26.1.2 / 26.2 写法 | 位置 |
|---|---|---|
| `living.setInvulnerableTime(0); living.damageCooldownTime = 0;` | `living.invulnerableTime = 0;`（`Entity.invulnerableTime` 仍是 public，`damageCooldownTime` 字段还不存在） | `HeavyArrow`（2 处）、`SmashTopUp`、`ArrowEffectSmashMixin`、`EcamAttackMixin` |
| `placeItemBackInInventory(stack, Prediction.SERVER_ONLY)` | `placeItemBackInInventory(stack)`（没有 `Prediction` 枚举） | `ThrownLonginus` |
| `submitModel(model, state, pose, type, light, overlay, 0)` | 末尾多一个 `CrumblingOverlay` 参数 → `..., 0, null)` | `DarkWindChargeRenderer` |
| `InputConstants.Type.KEYBOARD` | `InputConstants.Type.KEYSYM` | `HeavyGearClient` |

其余 71 个源文件原样可用，`javac` 零错误（JDK 25，两个版本各自对着自己的游戏 jar 编译）。

## 数据差异

| 数据 | 26.3 | 26.1.2 / 26.2 | 处理 |
|---|---|---|---|
| 战利品表 | `condition` / `modifier` / 条件对象用 `type` | 旧格式 `conditions` / `functions` / 条件对象用 `condition` | 以**各版本原版表**为基底重新生成三张表（`port_tables.py`），mod 的池子按旧格式追加 |
| 附魔 | 实体谓词用裸键（`type`、`location`、`flags`、`movement`） | 命名空间键（`minecraft:entity_type`、`minecraft:location`、`minecraft:flags`、`minecraft:movement`） | 取 1.21.11 jar 里的 5 个附魔覆盖文件（本来就是旧效果/需求格式），只重命名谓词键（`port_enchants.py`） |
| 成就 `final_offering` | `location` 是单个对象 + 裸键 | `location` 必须是旧条件**数组**（`[{"condition": "minecraft:match_tool", …}]`） | 换成 1.21.11 jar 里的同名文件 |

其余数据（`data/heavygear/` 的配方、其它成就、标签、`dive_bombing` 附魔）跨版本通用，直接沿用。
服务端启动日志里已无任何 `Couldn't parse data file`。

## 构建 / 测试工具（`.hgtest/port/`）

```powershell
$py = <bundled python>
$py .hgtest/port/build.py 26.2      # 编译（stub + 75 个源文件）→ .hgtest/port/work-26.2/classes
$py .hgtest/port/package.py 26.2    # 打包 → 26.2/heavygear-1.0.0+26.2.jar
$py .hgtest/port/run_server.py 26.2 # 起 Fabric 服务端（用客户端 jar，26.x 未混淆，含 net.minecraft.server.Main）
```

- `port_tables.py` / `port_enchants.py`：生成上面说的两版数据。
- `port_edits.py`：把 26.3 源码的 4 处 API 差异改写成 26.1.2/26.2 版本。
- 测试服需要 `pause-when-empty-seconds=0`，否则无玩家 60 秒后服务器会暂停 ticking（箭会悬空不动）。
- 26.2 测试实例带 fabric-api 0.160.0+26.2 + loader 0.19.5；
  26.1.2 实例带 fabric-api 0.155.3+26.1.2 + loader 0.20.1。

## 实机验证（两台真服务端，RCON 驱动）

| 项目 | 26.2 | 26.1.2 | 预期 |
|---|---|---|---|
| mod 加载 | heavygear 1.0.0 ✓ | heavygear 1.0.0 ✓ | — |
| 重箭伤害：仅箭（落距 0.3） | 1.00 | 1.00 | 箭伤害 |
| 重箭伤害：落距 ≈ 4 | 5.60 | 5.60 | 箭 + smash 3.5 |
| 重箭伤害：落距 ≈ 7.8 | 7.35 | 7.35 | 箭 + smash 5.4 |
| 远古守卫者 → 三叉戟升级 | 177/360 = 49.2% | 161/300 = 53.7% | 50% |
| 不祥宝库 → 沉重升级 | 119/650 = 18.3% | 113/750 = 15.1% | 2/12 = 16.7% |

重箭三档与 26.3 修复版完全一致（1.00 / 5.60 / 7.35），说明两段伤害（箭 + smash）在 26.1.2/26.2 上也是叠加的。

## 客户端崩溃修复（`ThrownTridentRendererMixin`）

首次给客户端时崩在初始化阶段：

```
Mixin transformation of net.minecraft.client.renderer.entity.ThrownTridentRenderer failed
Critical injection failure: Argument modifier method heavygear$renderTypeTexture(Identifier)Identifier
... failed injection check, (0/1) succeeded. Scanned 0 target(s).
```

原因：26.3 的 `ThrownTridentRenderer.submit` 把「贴图 + 闪光」合成一个 render type
（`RenderTypes.entitySolidGlint(Identifier)`），26.3 的 mixin 就是改这个调用的第 0 个参数；
**26.1.2 / 26.2 还没有这个写法**——它们先用 `Identifier` 直接 `submitModel(...)`，再单独来一遍闪光层：

| 版本 | solid 层 | 闪光层 |
|---|---|---|
| 26.3 | `RenderTypes.entitySolidGlint(tex)` + `submitModel(RenderType, …)` 7 参 | — |
| 26.1.2 / 26.2 | `submitModel(Model, Unit, Pose, Identifier, light, overlay, outlineColor, CrumblingOverlay)` 8 参 | 26.1.2: `model.renderType(tex)` + `ItemFeatureRenderer.getFoilRenderType`；26.2: `RenderTypes.entityGlint()` |

所以旧 mixin 在 26.1.2/26.2 上找不到注入点，直接判定失败。修法：

- 主注入点改成 8 参 `OrderedSubmitNodeCollector.submitModel(..., Identifier, III, CrumblingOverlay)` 的 `index = 3`；
- 26.1.2 的闪光层还从贴图构造 render type，于是再挂一个 `TridentModel.renderType(Identifier)` 的 `@ModifyArg(index = 0)`；
  26.2 没有这个调用，所以该注入标 `require = 0`（可选，不匹配也不报错）。

## 客户端冒烟测试

用本地 Minecraft 实例的 jar / 库 / 资源，把工作目录指到测试目录（不写实例目录本身），实际启动了两次客户端：

| 版本 | 结果 |
|---|---|
| 26.1.2（fabric-api 0.155.3 + loader 0.20.1） | 通过：`OpenAL initialized` / `Sound engine started`，无 mixin 报错（修复前就是死在这一步） |
| 26.2（fabric-api 0.160.0 + loader 0.19.5） | 通过：同上，无 mixin 报错 |

日志存档：`.hgtest/logs/client-26.1.2-smoketest.log`、`.hgtest/logs/client-26.2-smoketest.log`。
另外 `audit_mixins.py` 会静态核对每个客户端 mixin 的 `@At` 目标是否真的出现在目标类里
（26.1.2 全绿；26.2 只有那个标了 `require = 0` 的可选注入点）。

## 部署

- jar 直接放进对应实例的 `mods/`（两个实例本来就有 fabric-api 和 loader，无需额外依赖）。
- **26.2 实例里还留着旧的 `heavyspear-1.2.4+26.2.jar`**：它和 heavygear 是同一个模组的不同世代，
  物品 id 会重复注册，装新 jar 前必须把它删掉（或移出 mods）。
- `fabric.mod.json` 的 `depends`：26.2 → `minecraft >=26.2 <26.3`、`fabricloader >=0.19.5`；
  26.1.2 → `minecraft >=26.1.2 <26.2`、`fabricloader >=0.20.0`；java `>=25`。
- 26.3 的 jar 版本号已统一为 1.0.0，本次没有改动它（只改了 26.3 的 class 修复，版本号未变）。


## 重弩上弦动画僵硬（26.x 三个版本）

根因：26.x 判断"这是不是弩"用的是 `stack.is(Items.CROSSBOW)`，而 `ItemStack` 在 26.x 已经没有
`is(Item)` 了 —— 这个调用落到接口默认方法 `TypedInstance#is(T)`（擦除成 `is(Object)`），就是纯物品
身份比较，自定义重弩永远 false。于是所有"弩专属"的渲染分支都被跳过：第一人称的上弦变换、双手持弩
姿势、双手选择 —— 看起来就是举着不动。

旧的 `VanillaItemEquivalenceMixin` 只 hook 了 `ItemStack.is(Predicate)`，对这些调用无效。

修法（客户端 mixin，直接改调用点）：

| 版本 | 第一人称 | 第三人称姿势 |
|---|---|---|
| 26.3 | `FirstPersonHandsAndItemsRenderer.submitArmWithItem` | `AvatarRenderer.getArmPose(...)` |
| 26.2 | `ItemInHandRenderer.submitArmWithItem` + `isChargedCrossbow` | 同上 |
| 26.1.2 | `ItemInHandRenderer.renderArmWithItem` + `isChargedCrossbow` | 同上 |

即 `CrossbowFirstPersonMixin` + `CrossbowArmPoseMixin`（共用一个 `CrossbowEquivalence` 判定）。

试过但**放弃**的方案：给 `ItemStack` 合并一个 `is(Item)` 覆盖（一次覆盖所有调用点）——
在 26.3 客户端上会导致资源加载阶段 JVM 硬崩（jvm.dll 0xc0000005），已回退。

验证：三个版本各启动过一次客户端，都到 `Sound engine started`、无 mixin 报错
（`defaultRequire = 1`，说明两个 redirect 都命中了目标）。日志：`.hgtest/logs/client-*-crossbow.log`。
### 追加：把第一人称里**所有** is(CROSSBOW) 检查都接上（26.1.2 之前仍嫌僵硬的原因）

只改 `renderArmWithItem`/`submitArmWithItem` 不够 —— 26.1.2/26.2 的 `ItemInHandRenderer` 还有三处
`stack.is(Items.CROSSBOW)`：`evaluateWhichHandsToRender`（决定画哪只手）、
`selectionUsingItemWhileHoldingBowLike`（持弓类物品时的选择）、`isChargedCrossbow`（装填完成判定）。
它们没被接管时，左手上不来、装填状态判断错误，上弦看起来仍然僵。现在这些方法里的 `is(...)` 全部被
redirect 到 `CrossbowEquivalence`：

| 版本 | 被 redirect 的方法 |
|---|---|
| 26.1.2 | `ItemInHandRenderer`: `evaluateWhichHandsToRender`、`selectionUsingItemWhileHoldingBowLike`、`isChargedCrossbow`、`renderArmWithItem`；`AvatarRenderer.getArmPose` |
| 26.2 | 同上，末项为 `submitArmWithItem` |
| 26.3 | `FirstPersonHandsAndItemsRenderer.submitArmWithItem`、`FirstPersonHandsAndItems.evaluateWhichHandsToRender` + `isChargedCrossbow`、`AvatarRenderer.getArmPose` |

验证：26.1.2 客户端**进入世界**（quickPlay）跑通，`Preparing spawn area` → `Time elapsed` → 继续渲染十几秒，
无 `IllegalClassLoadError`/`InjectionError`（`require = 1`，说明每个 redirect 都命中了目标）。
`CrossbowEquivalence` 已移出 mixin 包（`com.cecilia.heavygear.client`），不会再触发上次那个类加载错误。