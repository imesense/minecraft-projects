package org.imesense.dynamicspawncontrol.core.script.processor;

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
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class AIZombieHasShieldNBT
{
    private static final String SHIELD_HEALTH = "ShieldHealth";
    private static final String SHIELD_COOLDOWN = "ShieldCooldown";
    private static final String LAST_HIT_TIME = "LastHitTime";

    public static class Config
    {
        public static float MAX_SHIELD_HEALTH = 5.0F;
        public static float BLOCK_PERCENTAGE = 0.66F;
        public static int SHIELD_BREAK_COOLDOWN = 100;
        public static int BLOCK_COOLDOWN = 20;
        public static long RECHARGE_DELAY = 60;
        public static float RECHARGE_RATE = 0.05F;
        public static boolean REQUIRE_FRONT_ATTACK = true;
        public static float KNOCKBACK_STRENGTH = 0.5F;
        public static float SHIELD_DAMAGE_MULTIPLIER = 2.0F;
        public static boolean ENABLE_PARTICLES = true;
        public static boolean ENABLE_SOUNDS = true;
        public static boolean DESTROY_SHIELD_ON_BREAK = true;
        public static boolean ALLOW_SHIELD_REPAIR = false;
        public static boolean USE_RANDOM_SHIELD_STATE = true;
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntity();
            initShieldData(zombie);

            if (Config.USE_RANDOM_SHIELD_STATE)
            {
                ItemStack mainHand = zombie.getHeldItem(EnumHand.MAIN_HAND);

                if (!mainHand.isEmpty() && mainHand.getItem() instanceof ItemShield)
                {
                    int max = mainHand.getMaxDamage();
                    int remaining = 1 + zombie.getRNG().nextInt(max);

                    mainHand.setItemDamage(max - remaining);

                    return;
                }

                ItemStack offHand = zombie.getHeldItem(EnumHand.OFF_HAND);

                if (!offHand.isEmpty() && offHand.getItem() instanceof ItemShield)
                {
                    int max = offHand.getMaxDamage();
                    int remaining = 1 + zombie.getRNG().nextInt(max);
                    offHand.setItemDamage(max - remaining);
                }
            }
        }
    }

    private void initShieldData(EntityZombie zombie)
    {
        NBTTagCompound data = zombie.getEntityData();

        if (!data.hasKey("DynamicSpawnControl"))
        {
            data.setTag("DynamicSpawnControl", new NBTTagCompound());
        }

        NBTTagCompound dscData = data.getCompoundTag("DynamicSpawnControl");

        if (!dscData.hasKey(SHIELD_HEALTH))
        {
            dscData.setFloat(SHIELD_HEALTH, Config.MAX_SHIELD_HEALTH);
            dscData.setInteger(SHIELD_COOLDOWN, 0);
            dscData.setLong(LAST_HIT_TIME, 0);
        }
    }

    private NBTTagCompound getDSCData(EntityZombie zombie)
    {
        NBTTagCompound data = zombie.getEntityData();

        if (!data.hasKey("DynamicSpawnControl"))
        {
            initShieldData(zombie);
        }

        return data.getCompoundTag("DynamicSpawnControl");
    }

    private ItemStack getShieldItem(EntityZombie zombie)
    {
        ItemStack mainHand = zombie.getHeldItem(EnumHand.MAIN_HAND);

        if (!mainHand.isEmpty() && mainHand.getItem() instanceof ItemShield)
        {
            return mainHand;
        }

        ItemStack offHand = zombie.getHeldItem(EnumHand.OFF_HAND);

        if (!offHand.isEmpty() && offHand.getItem() instanceof ItemShield)
        {
            return offHand;
        }

        return null;
    }

    private void checkAndRemoveBrokenShield(EntityZombie zombie, ItemStack shield)
    {
        if (shield == null || shield.isEmpty()) return;

        if (shield.getItemDamage() >= shield.getMaxDamage())
        {
            if (zombie.getHeldItem(EnumHand.MAIN_HAND) == shield)
            {
                zombie.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
            }
            else if (zombie.getHeldItem(EnumHand.OFF_HAND) == shield)
            {
                zombie.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);
            }

            zombie.world.playSound(null, zombie.posX, zombie.posY, zombie.posZ,
                    net.minecraft.init.SoundEvents.ENTITY_ITEM_BREAK,
                    SoundCategory.HOSTILE, 1.0F, 1.0F);

            NBTTagCompound dscData = getDSCData(zombie);
            dscData.setFloat(SHIELD_HEALTH, 0);
            dscData.setInteger(SHIELD_COOLDOWN, Config.SHIELD_BREAK_COOLDOWN);

            if (Config.DESTROY_SHIELD_ON_BREAK)
            {
                shield.setCount(0);
            }
        }
    }

    private void repairShieldIfNeeded(EntityZombie zombie, ItemStack shield, float shieldHealth)
    {
        if (shield == null || shield.isEmpty()) return;

        if (Config.ALLOW_SHIELD_REPAIR && shieldHealth >= Config.MAX_SHIELD_HEALTH * 0.9F)
        {
            int currentDamage = shield.getItemDamage();

            if (currentDamage > 0)
            {
                int repairAmount = Math.min(currentDamage, 10);
                shield.setItemDamage(currentDamage - repairAmount);
            }
        }
    }

    private boolean isAttackFromFront(EntityZombie zombie, DamageSource source)
    {
        if (!Config.REQUIRE_FRONT_ATTACK)
        {
            return true;
        }

        Entity attacker = source.getImmediateSource();

        if (attacker == null)
        {
            return true;
        }

        Vec3d zombieLook = zombie.getLookVec();
        Vec3d toAttacker = new Vec3d(attacker.posX - zombie.posX,
                0, attacker.posZ - zombie.posZ).normalize();

        double dot = zombieLook.dotProduct(toAttacker);
        boolean isFromFront = dot < 0;

        return isFromFront;
    }

    private void knockbackAttacker(EntityZombie zombie, DamageSource source)
    {
        if (Config.KNOCKBACK_STRENGTH <= 0)
        {
            return;
        }

        Entity attacker = source.getImmediateSource();

        if (attacker instanceof EntityLivingBase && attacker != zombie)
        {
            EntityLivingBase livingAttacker = (EntityLivingBase) attacker;

            livingAttacker.knockBack(zombie, Config.KNOCKBACK_STRENGTH,
                    MathHelper.sin(livingAttacker.rotationYaw * 0.017453292f),
                    -MathHelper.cos(livingAttacker.rotationYaw * 0.017453292f));
        }
    }

    private void showShieldParticles(EntityZombie zombie, float shieldPercentage)
    {
        if (!Config.ENABLE_PARTICLES) return;

        World world = zombie.world;
        if (world.isRemote) return;

        EnumParticleTypes particleType;
        int particleCount;

        if (shieldPercentage > 0.7F)
        {
            particleType = EnumParticleTypes.ENCHANTMENT_TABLE;
            particleCount = 1;
        }
        else if (shieldPercentage > 0.3F)
        {
            particleType = EnumParticleTypes.CRIT_MAGIC;
            particleCount = 1;
        }
        else
        {
            particleType = EnumParticleTypes.SMOKE_NORMAL;
            particleCount = 1;
        }

        for (int i = 0; i < particleCount; i++)
        {
            double offsetX = (world.rand.nextDouble() - 0.5) * zombie.width;
            double offsetY = world.rand.nextDouble() * zombie.height;
            double offsetZ = (world.rand.nextDouble() - 0.5) * zombie.width;

            world.spawnParticle(particleType, zombie.posX + offsetX,
                    zombie.posY + offsetY, zombie.posZ + offsetZ,
                    0, 0, 0);
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event)
    {
        if (!(event.getEntityLiving() instanceof EntityZombie)) return;

        EntityZombie zombie = (EntityZombie) event.getEntityLiving();
        ItemStack shield = getShieldItem(zombie);

        checkAndRemoveBrokenShield(zombie, shield);

        shield = getShieldItem(zombie);

        if (shield == null)
        {
            return;
        }

        NBTTagCompound dscData = getDSCData(zombie);
        float shieldHealth = dscData.getFloat(SHIELD_HEALTH);
        int cooldown = dscData.getInteger(SHIELD_COOLDOWN);

        if (cooldown > 0)
        {
            return;
        }

        if (shieldHealth <= 0)
        {
            if (Config.DESTROY_SHIELD_ON_BREAK)
            {
                checkAndRemoveBrokenShield(zombie, shield);
            }

            return;
        }

        if (!isAttackFromFront(zombie, event.getSource()))
        {
            return;
        }

        float damage = event.getAmount();
        float maxBlock = shieldHealth;
        float blocked = Math.min(damage * Config.BLOCK_PERCENTAGE, maxBlock);

        shieldHealth -= blocked;
        dscData.setFloat(SHIELD_HEALTH, shieldHealth);

        if (shieldHealth <= 0)
        {
            dscData.setInteger(SHIELD_COOLDOWN, Config.SHIELD_BREAK_COOLDOWN);

            if (Config.ENABLE_SOUNDS)
            {
                zombie.world.playSound(null, zombie.posX, zombie.posY, zombie.posZ,
                        net.minecraft.init.SoundEvents.ENTITY_ITEM_BREAK,
                        SoundCategory.HOSTILE, 1.0F, 1.0F);
            }

            if (Config.DESTROY_SHIELD_ON_BREAK)
            {
                checkAndRemoveBrokenShield(zombie, shield);
            }
        }
        else
        {
            dscData.setInteger(SHIELD_COOLDOWN, Config.BLOCK_COOLDOWN);
            dscData.setLong(LAST_HIT_TIME, zombie.world.getTotalWorldTime());

            playBlockEffects(zombie, blocked / damage);
        }

        float newDamage = damage - blocked;
        event.setAmount(newDamage);

        knockbackAttacker(zombie, event.getSource());

        int shieldDamage = (int) (blocked * Config.SHIELD_DAMAGE_MULTIPLIER);
        shield.damageItem(shieldDamage, zombie);

        checkAndRemoveBrokenShield(zombie, shield);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (!(event.getEntityLiving() instanceof EntityZombie)) return;

        EntityZombie zombie = (EntityZombie) event.getEntityLiving();
        ItemStack shield = getShieldItem(zombie);

        checkAndRemoveBrokenShield(zombie, shield);

        shield = getShieldItem(zombie);

        NBTTagCompound dscData = getDSCData(zombie);

        int cooldown = dscData.getInteger(SHIELD_COOLDOWN);

        if (cooldown > 0)
        {
            dscData.setInteger(SHIELD_COOLDOWN, cooldown - 1);
        }

        float shieldHealth = dscData.getFloat(SHIELD_HEALTH);

        if (cooldown <= 0 && shieldHealth < Config.MAX_SHIELD_HEALTH)
        {
            long lastHit = dscData.getLong(LAST_HIT_TIME);
            long currentTime = zombie.world.getTotalWorldTime();
            long timeSinceHit = currentTime - lastHit;

            if (timeSinceHit > Config.RECHARGE_DELAY)
            {
                shieldHealth = Math.min(Config.MAX_SHIELD_HEALTH, shieldHealth + Config.RECHARGE_RATE);
                dscData.setFloat(SHIELD_HEALTH, shieldHealth);

                if (shield != null)
                {
                    repairShieldIfNeeded(zombie, shield, shieldHealth);
                }

                if (Config.ENABLE_PARTICLES && zombie.world.rand.nextFloat() < 0.1F)
                {
                    showRechargeParticles(zombie);
                }
            }
        }

        if (shieldHealth > 0 && shield != null && Config.ENABLE_PARTICLES)
        {
            showShieldParticles(zombie, shieldHealth / Config.MAX_SHIELD_HEALTH);
        }
    }

    private void showRechargeParticles(EntityZombie zombie)
    {
        if (!Config.ENABLE_PARTICLES) return;

        World world = zombie.world;

        for (int i = 0; i < 3; i++)
        {
            double offsetX = (world.rand.nextDouble() - 0.5) * zombie.width * 0.5;
            double offsetY = world.rand.nextDouble() * zombie.height * 0.5;
            double offsetZ = (world.rand.nextDouble() - 0.5) * zombie.width * 0.5;

            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
                    zombie.posX + offsetX,
                    zombie.posY + offsetY + 0.5,
                    zombie.posZ + offsetZ,
                    0, 0.1, 0);
        }
    }

    private void playBlockEffects(EntityZombie zombie, float blockPercentage)
    {
        if (!Config.ENABLE_SOUNDS && !Config.ENABLE_PARTICLES) return;

        World world = zombie.world;

        if (Config.ENABLE_SOUNDS)
        {
            world.playSound(null, zombie.posX, zombie.posY, zombie.posZ,
                    net.minecraft.init.SoundEvents.ITEM_SHIELD_BLOCK,
                    SoundCategory.HOSTILE,
                    blockPercentage, 0.8F + zombie.getRNG().nextFloat() * 0.4F);
        }

        if (Config.ENABLE_PARTICLES)
        {
            for (int i = 0; i < 5; i++)
            {
                world.spawnParticle(EnumParticleTypes.CRIT,
                        zombie.posX + (zombie.getRNG().nextDouble() - 0.5) * zombie.width,
                        zombie.posY + zombie.getRNG().nextDouble() * zombie.height,
                        zombie.posZ + (zombie.getRNG().nextDouble() - 0.5) * zombie.width,
                        0, 0.1, 0);
            }
        }
    }
}