package org.imesense.dynamicspawncontrol.core.script.processor;

import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@TODO(value = "Merge this class in AI Tasks for Zombie entity", showOnce = false, priority = TODO.TodoPriority.HIGH)
public class AIZombieHasShieldNBT {

    // NBT теги для хранения данных
    private static final String SHIELD_HEALTH = "ShieldHealth";
    private static final String SHIELD_COOLDOWN = "ShieldCooldown";
    private static final String LAST_HIT_TIME = "LastHitTime";
    private static final boolean DEBUG = true; // Включить/выключить логи

    // Конфигурационные константы
    public static class Config {
        public static float MAX_SHIELD_HEALTH = 5.0F;
        public static float BLOCK_PERCENTAGE = 0.66F; // % блокируемого урона
        public static int SHIELD_BREAK_COOLDOWN = 100; // тиков (5 секунд)
        public static int BLOCK_COOLDOWN = 20; // тиков (1 секунда)
        public static long RECHARGE_DELAY = 60; // тиков до начала перезарядки (3 секунды)
        public static float RECHARGE_RATE = 0.05F; // здоровья в тик
        public static boolean REQUIRE_FRONT_ATTACK = true; // Требовать атаку спереди
        public static float KNOCKBACK_STRENGTH = 0.5F; // Сила отбрасывания
        public static float SHIELD_DAMAGE_MULTIPLIER = 2.0F; // Множитель урона щиту
        public static boolean ENABLE_PARTICLES = true; // Включить частицы
        public static boolean ENABLE_SOUNDS = true; // Включить звуки
        public static boolean DESTROY_SHIELD_ON_BREAK = true; // Удалять щит при поломке
        public static boolean ALLOW_SHIELD_REPAIR = false; // Позволить восстановление щита после перезарядки
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityZombie) {
            EntityZombie zombie = (EntityZombie) event.getEntity();
            initShieldData(zombie);
        }
    }

    private void initShieldData(EntityZombie zombie) {
        NBTTagCompound data = zombie.getEntityData();
        if (!data.hasKey("DynamicSpawnControl")) {
            data.setTag("DynamicSpawnControl", new NBTTagCompound());
            if (DEBUG) Log.write(0, "[AIZombieShield] Создан новый тег DynamicSpawnControl для зомби " + zombie.getEntityId());
        }

        NBTTagCompound dscData = data.getCompoundTag("DynamicSpawnControl");
        if (!dscData.hasKey(SHIELD_HEALTH)) {
            dscData.setFloat(SHIELD_HEALTH, Config.MAX_SHIELD_HEALTH); // Полный щит
            dscData.setInteger(SHIELD_COOLDOWN, 0);
            dscData.setLong(LAST_HIT_TIME, 0);

            if (DEBUG) {
                Log.write(0, String.format("[AIZombieShield] Инициализирован щит для зомби %s: здоровье=%.1f, кулдаун=%d",
                        zombie.getEntityId(), Config.MAX_SHIELD_HEALTH, 0));
            }
        }
    }

    private NBTTagCompound getDSCData(EntityZombie zombie) {
        NBTTagCompound data = zombie.getEntityData();
        if (!data.hasKey("DynamicSpawnControl")) {
            initShieldData(zombie);
        }
        return data.getCompoundTag("DynamicSpawnControl");
    }

    private ItemStack getShieldItem(EntityZombie zombie) {
        // Проверяем основную руку
        ItemStack mainHand = zombie.getHeldItem(EnumHand.MAIN_HAND);
        if (!mainHand.isEmpty() && mainHand.getItem() instanceof ItemShield) {
            if (DEBUG && zombie.ticksExisted % 100 == 0) Log.write(3, "[AIZombieShield] Зомби " + zombie.getEntityId() + " имеет щит в основной руке, прочность: " + mainHand.getItemDamage() + "/" + mainHand.getMaxDamage());
            return mainHand;
        }

        // Проверяем вторую руку
        ItemStack offHand = zombie.getHeldItem(EnumHand.OFF_HAND);
        if (!offHand.isEmpty() && offHand.getItem() instanceof ItemShield) {
            if (DEBUG && zombie.ticksExisted % 100 == 0) Log.write(3, "[AIZombieShield] Зомби " + zombie.getEntityId() + " имеет щит в дополнительной руке, прочность: " + offHand.getItemDamage() + "/" + offHand.getMaxDamage());
            return offHand;
        }

        if (DEBUG && zombie.ticksExisted % 100 == 0) Log.write(3, "[AIZombieShield] Зомби " + zombie.getEntityId() + " не имеет щита");
        return null;
    }

    private void checkAndRemoveBrokenShield(EntityZombie zombie, ItemStack shield) {
        if (shield == null || shield.isEmpty()) return;

        // Проверяем, сломан ли щит (прочность достигла максимума)
        if (shield.getItemDamage() >= shield.getMaxDamage()) {
            if (DEBUG) Log.write(1, String.format("[AIZombieShield] Щит зомби %s полностью сломан (прочность: %d/%d), удаляем из инвентаря",
                    zombie.getEntityId(), shield.getItemDamage(), shield.getMaxDamage()));

            // Удаляем щит из руки
            if (zombie.getHeldItem(EnumHand.MAIN_HAND) == shield) {
                zombie.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
            } else if (zombie.getHeldItem(EnumHand.OFF_HAND) == shield) {
                zombie.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);
            }

            // Эффект разрушения предмета
            zombie.world.playSound(null, zombie.posX, zombie.posY, zombie.posZ,
                    net.minecraft.init.SoundEvents.ENTITY_ITEM_BREAK,
                    SoundCategory.HOSTILE, 1.0F, 1.0F);

            // Сбрасываем данные щита в NBT
            NBTTagCompound dscData = getDSCData(zombie);
            dscData.setFloat(SHIELD_HEALTH, 0);
            dscData.setInteger(SHIELD_COOLDOWN, Config.SHIELD_BREAK_COOLDOWN);

            if (Config.DESTROY_SHIELD_ON_BREAK) {
                // Уничтожаем предмет полностью
                shield.setCount(0);
            }
        }
    }

    private void repairShieldIfNeeded(EntityZombie zombie, ItemStack shield, float shieldHealth) {
        if (shield == null || shield.isEmpty()) return;

        // Если щит имеет прочность и мы позволяем восстановление
        if (Config.ALLOW_SHIELD_REPAIR && shieldHealth >= Config.MAX_SHIELD_HEALTH * 0.9F) {
            // Восстанавливаем немного прочность щита
            int currentDamage = shield.getItemDamage();
            if (currentDamage > 0) {
                int repairAmount = Math.min(currentDamage, 10); // Восстанавливаем до 10 прочности
                shield.setItemDamage(currentDamage - repairAmount);

                if (DEBUG && zombie.ticksExisted % 200 == 0) {
                    Log.write(2, String.format("[AIZombieShield] Щит зомби %s частично восстановлен: %d -> %d прочности",
                            zombie.getEntityId(), currentDamage, shield.getItemDamage()));
                }
            }
        }
    }

    private boolean isAttackFromFront(EntityZombie zombie, DamageSource source) {
        // Если не требуется атака спереди, всегда возвращаем true
        if (!Config.REQUIRE_FRONT_ATTACK) {
            if (DEBUG && zombie.ticksExisted % 200 == 0) Log.write(3, "[AIZombieShield] Проверка направления отключена в конфиге");
            return true;
        }

        Entity attacker = source.getImmediateSource();
        if (attacker == null) {
            if (DEBUG) Log.write(3, "[AIZombieShield] Атака без источника, считаем спереди");
            return true; // Если нет атакующего, считаем что спереди
        }

        Vec3d zombieLook = zombie.getLookVec();
        Vec3d toAttacker = new Vec3d(
                attacker.posX - zombie.posX,
                0,
                attacker.posZ - zombie.posZ
        ).normalize();

        // Угол между взглядом зомби и направлением на атакующего
        double dot = zombieLook.dotProduct(toAttacker);
        boolean isFromFront = dot < 0;

        if (DEBUG && isFromFront) {
            Log.write(2, String.format("[AIZombieShield] Проверка направления: зомби %s, атакующий %s, dot=%.2f, спереди=%s",
                    zombie.getEntityId(), attacker.getEntityId(), dot, isFromFront));
        }

        return isFromFront;
    }

    private void knockbackAttacker(EntityZombie zombie, DamageSource source) {
        if (Config.KNOCKBACK_STRENGTH <= 0) {
            if (DEBUG && zombie.ticksExisted % 200 == 0) Log.write(3, "[AIZombieShield] Отбрасывание отключено в конфиге");
            return;
        }

        Entity attacker = source.getImmediateSource();
        if (attacker instanceof EntityLivingBase && attacker != zombie) {
            EntityLivingBase livingAttacker = (EntityLivingBase) attacker;

            // Отбрасывание атакующего
            livingAttacker.knockBack(zombie, Config.KNOCKBACK_STRENGTH,
                    MathHelper.sin(livingAttacker.rotationYaw * 0.017453292F),
                    -MathHelper.cos(livingAttacker.rotationYaw * 0.017453292F));

            if (DEBUG) {
                Log.write(1, String.format("[AIZombieShield] Зомби %s отбросил атакующего %s силой %.2f",
                        zombie.getEntityId(), attacker.getEntityId(), Config.KNOCKBACK_STRENGTH));
            }
        }
    }

    private void showShieldParticles(EntityZombie zombie, float shieldPercentage) {
        if (!Config.ENABLE_PARTICLES) return;

        World world = zombie.world;
        if (world.isRemote) return; // Только на клиенте

        // Разные частицы в зависимости от состояния щита
        EnumParticleTypes particleType;
        int particleCount;

        if (shieldPercentage > 0.7F) {
            particleType = EnumParticleTypes.ENCHANTMENT_TABLE;
            particleCount = 1;
        } else if (shieldPercentage > 0.3F) {
            particleType = EnumParticleTypes.CRIT_MAGIC;
            particleCount = 1;
        } else {
            particleType = EnumParticleTypes.SMOKE_NORMAL;
            particleCount = 1;
        }

        // Спавним частицы вокруг зомби
        for (int i = 0; i < particleCount; i++) {
            double offsetX = (world.rand.nextDouble() - 0.5) * zombie.width;
            double offsetY = world.rand.nextDouble() * zombie.height;
            double offsetZ = (world.rand.nextDouble() - 0.5) * zombie.width;

            world.spawnParticle(particleType,
                    zombie.posX + offsetX,
                    zombie.posY + offsetY,
                    zombie.posZ + offsetZ,
                    0, 0, 0);
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof EntityZombie)) return;

        EntityZombie zombie = (EntityZombie) event.getEntityLiving();
        ItemStack shield = getShieldItem(zombie);

        // Проверяем и удаляем сломанный щит перед обработкой
        checkAndRemoveBrokenShield(zombie, shield);

        // Получаем щит снова (возможно, он был удален)
        shield = getShieldItem(zombie);

        if (shield == null) {
            if (DEBUG && zombie.ticksExisted % 100 == 0) Log.write(3, "[AIZombieShield] Зомби " + zombie.getEntityId() + " не имеет щита, пропускаем блокирование");
            return;
        }

        NBTTagCompound dscData = getDSCData(zombie);
        float shieldHealth = dscData.getFloat(SHIELD_HEALTH);
        int cooldown = dscData.getInteger(SHIELD_COOLDOWN);

        if (DEBUG) {
            Log.write(1, String.format("[AIZombieShield] Зомби %s получил урон %.2f, щит: здоровье=%.1f, кулдаун=%d, прочность предмета: %d/%d",
                    zombie.getEntityId(), event.getAmount(), shieldHealth, cooldown,
                    shield.getItemDamage(), shield.getMaxDamage()));
        }

        // Щит на перезарядке или сломан
        if (cooldown > 0) {
            if (DEBUG) Log.write(2, String.format("[AIZombieShield] Щит на кулдауне: %d тиков (%.1f сек)",
                    cooldown, cooldown / 20.0F));
            return;
        }

        if (shieldHealth <= 0) {
            if (DEBUG) Log.write(2, "[AIZombieShield] Щит сломан, здоровье: " + shieldHealth);

            // Если щит сломан, удаляем его
            if (Config.DESTROY_SHIELD_ON_BREAK) {
                checkAndRemoveBrokenShield(zombie, shield);
            }
            return;
        }

        // Проверяем направление атаки (только спереди)
        if (!isAttackFromFront(zombie, event.getSource())) {
            if (DEBUG) Log.write(2, "[AIZombieShield] Атака не спереди, блокирование невозможно");
            return;
        }

        // Блокируем часть урона
        float damage = event.getAmount();
        float maxBlock = shieldHealth;
        float blocked = Math.min(damage * Config.BLOCK_PERCENTAGE, maxBlock);

        if (DEBUG) {
            Log.write(0, String.format("[AIZombieShield] Попытка блокирования: урон=%.2f, макс.блок=%.1f, заблокировано=%.2f (%.0f%%)",
                    damage, maxBlock, blocked, Config.BLOCK_PERCENTAGE * 100));
        }

        // Обновляем здоровье щита
        shieldHealth -= blocked;
        dscData.setFloat(SHIELD_HEALTH, shieldHealth);

        // Если щит сломался
        if (shieldHealth <= 0) {
            dscData.setInteger(SHIELD_COOLDOWN, Config.SHIELD_BREAK_COOLDOWN);
            if (DEBUG) {
                Log.write(0, String.format("[AIZombieShield] Щит зомби %s сломан! Установлен кулдаун %d тиков (%.1f сек)",
                        zombie.getEntityId(), Config.SHIELD_BREAK_COOLDOWN, Config.SHIELD_BREAK_COOLDOWN / 20.0F));
            }

            // Визуальные эффекты слома
            if (Config.ENABLE_SOUNDS) {
                zombie.world.playSound(null, zombie.posX, zombie.posY, zombie.posZ,
                        net.minecraft.init.SoundEvents.ENTITY_ITEM_BREAK,
                        SoundCategory.HOSTILE, 1.0F, 1.0F);
            }

            // Удаляем сломанный щит
            if (Config.DESTROY_SHIELD_ON_BREAK) {
                checkAndRemoveBrokenShield(zombie, shield);
            }

        } else {
            dscData.setInteger(SHIELD_COOLDOWN, Config.BLOCK_COOLDOWN);
            dscData.setLong(LAST_HIT_TIME, zombie.world.getTotalWorldTime());

            if (DEBUG) {
                Log.write(1, String.format("[AIZombieShield] Успешный блок! Оставшееся здоровье щита: %.1f/%.1f, кулдаун: %d тиков",
                        shieldHealth, Config.MAX_SHIELD_HEALTH, Config.BLOCK_COOLDOWN));
            }

            // Эффекты успешного блока
            playBlockEffects(zombie, blocked / damage);
        }

        // Уменьшаем урон
        float newDamage = damage - blocked;
        event.setAmount(newDamage);

        if (DEBUG) {
            Log.write(0, String.format("[AIZombieShield] Урон изменен: было %.2f, стало %.2f (снижение на %.1f%%)",
                    damage, newDamage, (blocked / damage) * 100));
        }

        // Отбрасывание
        knockbackAttacker(zombie, event.getSource());

        // Повреждаем предмет щита
        int shieldDamage = (int) (blocked * Config.SHIELD_DAMAGE_MULTIPLIER);
        shield.damageItem(shieldDamage, zombie);

        // Проверяем, не сломался ли предмет щита
        checkAndRemoveBrokenShield(zombie, shield);

        if (DEBUG) {
            Log.write(1, String.format("[AIZombieShield] Щит поврежден на %d единиц (множитель %.1f), прочность: %d/%d",
                    shieldDamage, Config.SHIELD_DAMAGE_MULTIPLIER,
                    shield.getMaxDamage() - shield.getItemDamage(), shield.getMaxDamage()));
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityZombie)) return;

        EntityZombie zombie = (EntityZombie) event.getEntityLiving();
        ItemStack shield = getShieldItem(zombie);

        // Проверяем и удаляем сломанный щит
        checkAndRemoveBrokenShield(zombie, shield);

        // Получаем щит снова (возможно, он был удален)
        shield = getShieldItem(zombie);

        NBTTagCompound dscData = getDSCData(zombie);

        // Обновляем кулдаун
        int cooldown = dscData.getInteger(SHIELD_COOLDOWN);
        if (cooldown > 0) {
            int oldCooldown = cooldown;
            dscData.setInteger(SHIELD_COOLDOWN, cooldown - 1);

            if (DEBUG && oldCooldown % 20 == 0) { // Логируем каждую секунду
                Log.write(3, String.format("[AIZombieShield] Зомби %s: кулдаун щита %d -> %d тиков",
                        zombie.getEntityId(), oldCooldown, cooldown - 1));
            }
        }

        // Перезарядка щита
        float shieldHealth = dscData.getFloat(SHIELD_HEALTH);
        if (cooldown <= 0 && shieldHealth < Config.MAX_SHIELD_HEALTH) {
            long lastHit = dscData.getLong(LAST_HIT_TIME);
            long currentTime = zombie.world.getTotalWorldTime();
            long timeSinceHit = currentTime - lastHit;

            // Начинаем перезарядку через указанное время
            if (timeSinceHit > Config.RECHARGE_DELAY) {
                float oldHealth = shieldHealth;
                shieldHealth = Math.min(Config.MAX_SHIELD_HEALTH, shieldHealth + Config.RECHARGE_RATE);
                dscData.setFloat(SHIELD_HEALTH, shieldHealth);

                if (DEBUG && zombie.world.rand.nextFloat() < 0.01F) {
                    Log.write(3, String.format("[AIZombieShield] Зомби %s: перезарядка щита %.1f -> %.1f (задержка %d, скорость %.2f/тик)",
                            zombie.getEntityId(), oldHealth, shieldHealth, Config.RECHARGE_DELAY, Config.RECHARGE_RATE));
                }

                // Восстанавливаем прочность предмета щита, если нужно
                if (shield != null) {
                    repairShieldIfNeeded(zombie, shield, shieldHealth);
                }

                // Визуальный эффект перезарядки
                if (Config.ENABLE_PARTICLES && zombie.world.rand.nextFloat() < 0.1F) {
                    showRechargeParticles(zombie);
                }
            }
        }

        // Визуальная индикация щита
        if (shieldHealth > 0 && shield != null && Config.ENABLE_PARTICLES) {
            showShieldParticles(zombie, shieldHealth / Config.MAX_SHIELD_HEALTH);
        }

        // Периодическая проверка состояния щита
        if (zombie.ticksExisted % 40 == 0 && shield != null) { // Каждые 2 секунды
            if (DEBUG) {
                Log.write(3, String.format("[AIZombieShield] Состояние зомби %s: щит здоровье=%.1f, прочность предмета=%d/%d",
                        zombie.getEntityId(), shieldHealth, shield.getItemDamage(), shield.getMaxDamage()));
            }
        }
    }

    private void showRechargeParticles(EntityZombie zombie) {
        if (!Config.ENABLE_PARTICLES) return;

        World world = zombie.world;

        for (int i = 0; i < 3; i++) {
            double offsetX = (world.rand.nextDouble() - 0.5) * zombie.width * 0.5;
            double offsetY = world.rand.nextDouble() * zombie.height * 0.5;
            double offsetZ = (world.rand.nextDouble() - 0.5) * zombie.width * 0.5;

            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
                    zombie.posX + offsetX,
                    zombie.posY + offsetY + 0.5,
                    zombie.posZ + offsetZ,
                    0, 0.1, 0);
        }

        if (DEBUG && world.rand.nextFloat() < 0.1F) {
            Log.write(3, "[AIZombieShield] Показаны частицы перезарядки для зомби " + zombie.getEntityId());
        }
    }

    private void playBlockEffects(EntityZombie zombie, float blockPercentage) {
        if (!Config.ENABLE_SOUNDS && !Config.ENABLE_PARTICLES) return;

        World world = zombie.world;

        // Звук
        if (Config.ENABLE_SOUNDS) {
            world.playSound(null, zombie.posX, zombie.posY, zombie.posZ,
                    net.minecraft.init.SoundEvents.ITEM_SHIELD_BLOCK,
                    SoundCategory.HOSTILE,
                    blockPercentage, 0.8F + zombie.getRNG().nextFloat() * 0.4F);

            if (DEBUG) {
                Log.write(2, String.format("[AIZombieShield] Воспроизведен звук блокирования: громкость=%.2f, тон=%.2f",
                        blockPercentage, 0.8F + zombie.getRNG().nextFloat() * 0.4F));
            }
        }

        // Частицы
        if (Config.ENABLE_PARTICLES) {
            for (int i = 0; i < 5; i++) {
                world.spawnParticle(EnumParticleTypes.CRIT,
                        zombie.posX + (zombie.getRNG().nextDouble() - 0.5) * zombie.width,
                        zombie.posY + zombie.getRNG().nextDouble() * zombie.height,
                        zombie.posZ + (zombie.getRNG().nextDouble() - 0.5) * zombie.width,
                        0, 0.1, 0);
            }

            if (DEBUG && blockPercentage > 0.5F) {
                Log.write(2, "[AIZombieShield] Сильный блок! Дополнительные эффекты для зомби " + zombie.getEntityId());
            }
        }
    }

    // Метод для принудительной установки щита (можно вызывать из спавнера)
    public static void setupZombieWithShield(EntityZombie zombie, ItemStack shield) {
        if (shield == null || !(shield.getItem() instanceof ItemShield)) {
            Log.write(2, "[AIZombieShield] Попытка установить не-щит зомби " + zombie.getEntityId());
            return;
        }

        // Создаем копию щита с полной прочностью
        ItemStack shieldCopy = shield.copy();
        shieldCopy.setItemDamage(0); // Восстанавливаем прочность

        // Даем щит в основную руку
        zombie.setHeldItem(EnumHand.MAIN_HAND, shieldCopy);

        // Инициализируем данные щита
        AIZombieHasShieldNBT instance = new AIZombieHasShieldNBT();
        instance.initShieldData(zombie);

        // Получаем данные для начальной настройки
        NBTTagCompound dscData = instance.getDSCDataPrivate(zombie);
        dscData.setFloat(SHIELD_HEALTH, Config.MAX_SHIELD_HEALTH);
        dscData.setInteger(SHIELD_COOLDOWN, 0);
        dscData.setLong(LAST_HIT_TIME, 0);

        Log.write(0, String.format("[AIZombieShield] Зомби %s экипирован умным щитом (здоровье=%.1f, прочность: %d/%d)",
                zombie.getEntityId(), Config.MAX_SHIELD_HEALTH,
                shieldCopy.getItemDamage(), shieldCopy.getMaxDamage()));
    }

    private NBTTagCompound getDSCDataPrivate(EntityZombie zombie) {
        NBTTagCompound data = zombie.getEntityData();
        if (!data.hasKey("DynamicSpawnControl")) {
            data.setTag("DynamicSpawnControl", new NBTTagCompound());
        }
        return data.getCompoundTag("DynamicSpawnControl");
    }

    // Методы для изменения конфигурации в runtime
    public static void updateConfig(float maxHealth, float blockPercent, int breakCooldown,
                                    int blockCooldown, long rechargeDelay, float rechargeRate) {
        Config.MAX_SHIELD_HEALTH = maxHealth;
        Config.BLOCK_PERCENTAGE = blockPercent;
        Config.SHIELD_BREAK_COOLDOWN = breakCooldown;
        Config.BLOCK_COOLDOWN = blockCooldown;
        Config.RECHARGE_DELAY = rechargeDelay;
        Config.RECHARGE_RATE = rechargeRate;

        Log.write(0, "[AIZombieShield] Конфигурация обновлена:");
        Log.write(0, String.format("  MAX_SHIELD_HEALTH: %.1f", Config.MAX_SHIELD_HEALTH));
        Log.write(0, String.format("  BLOCK_PERCENTAGE: %.0f%%", Config.BLOCK_PERCENTAGE * 100));
        Log.write(0, String.format("  SHIELD_BREAK_COOLDOWN: %d тиков (%.1f сек)",
                Config.SHIELD_BREAK_COOLDOWN, Config.SHIELD_BREAK_COOLDOWN / 20.0F));
        Log.write(0, String.format("  BLOCK_COOLDOWN: %d тиков (%.1f сек)",
                Config.BLOCK_COOLDOWN, Config.BLOCK_COOLDOWN / 20.0F));
        Log.write(0, String.format("  RECHARGE_DELAY: %d тиков (%.1f сек)",
                Config.RECHARGE_DELAY, Config.RECHARGE_DELAY / 20.0F));
        Log.write(0, String.format("  RECHARGE_RATE: %.2f/тик", Config.RECHARGE_RATE));
        Log.write(0, String.format("  DESTROY_SHIELD_ON_BREAK: %s", Config.DESTROY_SHIELD_ON_BREAK));
        Log.write(0, String.format("  ALLOW_SHIELD_REPAIR: %s", Config.ALLOW_SHIELD_REPAIR));
    }

    // Метод для отладки - печать состояния щита
    public static void debugShieldState(EntityZombie zombie) {
        if (!DEBUG) return;

        AIZombieHasShieldNBT instance = new AIZombieHasShieldNBT();
        NBTTagCompound dscData = instance.getDSCDataPrivate(zombie);
        float health = dscData.getFloat(SHIELD_HEALTH);
        int cooldown = dscData.getInteger(SHIELD_COOLDOWN);
        long lastHit = dscData.getLong(LAST_HIT_TIME);
        long currentTime = zombie.world.getTotalWorldTime();

        Log.write(0, String.format("[AIZombieShield] Отладка зомби %s:", zombie.getEntityId()));
        Log.write(0, String.format("  Здоровье щита: %.1f/%.1f (%.0f%%)",
                health, Config.MAX_SHIELD_HEALTH, (health / Config.MAX_SHIELD_HEALTH) * 100));
        Log.write(0, String.format("  Кулдаун: %d тиков (%.1f сек)", cooldown, cooldown / 20.0F));
        Log.write(0, String.format("  Последний удар: %d тиков назад", currentTime - lastHit));
        Log.write(0, String.format("  Конфиг: блок=%.0f%%, перезарядка=%.2f/тик, уничтожать щит=%s",
                Config.BLOCK_PERCENTAGE * 100, Config.RECHARGE_RATE, Config.DESTROY_SHIELD_ON_BREAK));

        ItemStack shield = instance.getShieldItem(zombie);
        if (shield != null && !shield.isEmpty()) {
            Log.write(0, String.format("  Предмет щита: %s, прочность: %d/%d, сломан: %s",
                    shield.getDisplayName(),
                    shield.getItemDamage(), shield.getMaxDamage(),
                    shield.getItemDamage() >= shield.getMaxDamage() ? "ДА" : "НЕТ"));
        } else {
            Log.write(0, "  Щит не найден в руках");
        }
    }

    // Метод для получения текущей конфигурации
    public static String getConfigSummary() {
        return String.format(
                "MAX_SHIELD_HEALTH=%.1f, BLOCK_PERCENTAGE=%.0f%%, " +
                        "SHIELD_BREAK_COOLDOWN=%d, BLOCK_COOLDOWN=%d, " +
                        "RECHARGE_DELAY=%d, RECHARGE_RATE=%.2f, " +
                        "DESTROY_SHIELD=%s, REPAIR_SHIELD=%s",
                Config.MAX_SHIELD_HEALTH,
                Config.BLOCK_PERCENTAGE * 100,
                Config.SHIELD_BREAK_COOLDOWN,
                Config.BLOCK_COOLDOWN,
                Config.RECHARGE_DELAY,
                Config.RECHARGE_RATE,
                Config.DESTROY_SHIELD_ON_BREAK,
                Config.ALLOW_SHIELD_REPAIR
        );
    }
}