package org.imesense.dynamicspawncontrol.satietymanager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.List;

public final class RespawnSatietyModule
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        if (event.player == null)
        {
            return;
        }

        if (event.player.world.isRemote)
        {
            return;
        }

        if (!(event.player instanceof EntityPlayerMP))
        {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.player;
        FoodStats foodStats = player.getFoodStats();

        foodStats.setFoodLevel(20);
        foodStats.setFoodSaturationLevel(5.0F);

        Log.write(0,"Max hunger set for player: " + player.getName() + " (Multiplayer ready)");
    }

    @SubscribeEvent
    public void onPlayerRespawn_0(PlayerEvent.PlayerRespawnEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.world;

        if (world.isRemote)
        {
            return;
        }

        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class,
                player.getEntityBoundingBox().grow(25.0D));

        for (Entity entity : entities)
        {
            if (entity == player)
            {
                continue;
            }

            if (isHostileMob(entity))
            {
                double angle = Math.random() * 2 * Math.PI;
                double distance = 50.0D + Math.random() * 10.0D;

                double teleportX = player.posX + Math.cos(angle) * distance;
                double teleportZ = player.posZ + Math.sin(angle) * distance;

                int teleportY = world.getHeight((int)teleportX, (int)teleportZ) + 1;

                if (teleportY > 0 && teleportY < world.getActualHeight())
                {
                    entity.setPositionAndUpdate(teleportX, teleportY, teleportZ);

                    if (world.rand.nextInt(3) == 0)
                    {
                        world.playEvent(2003,
                                new BlockPos(entity.posX, entity.posY + 1, entity.posZ), 0);
                    }
                }
            }
        }
    }

    private boolean isHostileMob(Entity entity)
    {
        if (entity instanceof EntityMob)
        {
            return true;
        }

        if (entity instanceof EntitySlime)
        {
            EntitySlime slime = (EntitySlime) entity;
            return !(slime instanceof EntityMagmaCube);
        }

        if (entity instanceof EntityGhast)
        {
            return true;
        }

        if (entity instanceof EntityPigZombie)
        {
            return true;
        }

        if (entity instanceof EntityEnderman)
        {
            EntityEnderman enderman = (EntityEnderman) entity;
            return enderman.isScreaming();
        }

        if (entity instanceof EntityWolf)
        {
            EntityWolf wolf = (EntityWolf) entity;
            return wolf.isAngry();
        }

        String entityName = EntityList.getEntityString(entity);

        if ("shulker".equals(entityName))
        {
            return true;
        }

        if ("husk".equals(entityName) || "stray".equals(entityName))
        {
            return true;
        }

        return false;
    }
}