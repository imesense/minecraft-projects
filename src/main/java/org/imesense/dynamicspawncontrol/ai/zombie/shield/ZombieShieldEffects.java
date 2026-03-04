package org.imesense.dynamicspawncontrol.ai.zombie.shield;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import static org.imesense.dynamicspawncontrol.ai.zombie.shield.ZombieShieldConfig.*;

public class ZombieShieldEffects
{
    public static void showShieldParticles(EntityZombie zombie, float shieldPercentage)
    {
        if (!ENABLE_PARTICLES) return;

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

    public static void showRechargeParticles(EntityZombie zombie)
    {
        if (!ENABLE_PARTICLES) return;

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

    public static void playBlockEffects(EntityZombie zombie, float blockPercentage)
    {
        if (!ENABLE_SOUNDS && !ENABLE_PARTICLES) return;

        World world = zombie.world;

        if (ENABLE_SOUNDS)
        {
            world.playSound(null, zombie.posX, zombie.posY, zombie.posZ,
                    net.minecraft.init.SoundEvents.ITEM_SHIELD_BLOCK,
                    SoundCategory.HOSTILE,
                    blockPercentage, 0.8F + zombie.getRNG().nextFloat() * 0.4F);
        }

        if (ENABLE_PARTICLES)
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
