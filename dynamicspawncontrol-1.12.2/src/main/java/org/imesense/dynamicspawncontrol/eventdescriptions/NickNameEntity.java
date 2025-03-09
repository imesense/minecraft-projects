package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class NickNameEntity
{
    private final List<String> RANDOM_NAMES = new ArrayList<>();

    private static volatile NickNameEntity _INSTANCE;

    public static NickNameEntity getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (NickNameEntity.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new NickNameEntity();
                }
            }
        }

        return _INSTANCE;
    }

    public NickNameEntity()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.loadNamesFromFile();
    }

    private void loadNamesFromFile()
    {
        InputStream inputStream = getClass().getResourceAsStream("/assets/dynamicspawncontrol/names.txt");

        assert inputStream != null;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)))
        {
            String line;

            while ((line = bufferedReader.readLine()) != null)
            {
                this.RANDOM_NAMES.add(line.trim());
            }
        }
        catch (IOException exception)
        {

        }
    }

    public void handleZombieSpawn(LivingSpawnEvent.SpecialSpawn event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntity();

            if (UniqueField.RANDOM.nextFloat() < 0.3f)
            {
                String randomName = this.RANDOM_NAMES.get(UniqueField.RANDOM.nextInt(this.RANDOM_NAMES.size()));
                zombie.setCustomNameTag(randomName);
                zombie.setAlwaysRenderNameTag(UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG);
            }
        }
    }

    public void handleVillagerSpawn(LivingSpawnEvent.SpecialSpawn event)
    {
        if (event.getEntity() instanceof EntityVillager)
        {
            EntityVillager villager = (EntityVillager) event.getEntity();

            String randomName = this.RANDOM_NAMES.get(UniqueField.RANDOM.nextInt(this.RANDOM_NAMES.size()));
            villager.setCustomNameTag(randomName);
            villager.setAlwaysRenderNameTag(UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG);
        }
    }

    public void handleZombieDeath(LivingDeathEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntity();
            World world = zombie.getEntityWorld();
            DamageSource damageSource = event.getSource();
            Entity killer = damageSource.getTrueSource();

            if (zombie.hasCustomName())
            {
                String zombieName = zombie.getCustomNameTag();
                String deathMessage = this.getDeathMessage(zombieName, damageSource, killer);

                if (!world.isRemote && world.getMinecraftServer() != null)
                {
                    if (UniqueField.RANDOM.nextFloat() < 0.75f)
                    {
                        world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentString(deathMessage));
                    }
                }
            }
        }
    }

    public void handleVillagerDeath(LivingDeathEvent event)
    {
        if (event.getEntity() instanceof EntityVillager)
        {
            EntityVillager villager = (EntityVillager) event.getEntity();
            World world = villager.getEntityWorld();
            DamageSource damageSource = event.getSource();
            Entity killer = damageSource.getTrueSource();

            if (villager.hasCustomName())
            {
                String villagerName = villager.getCustomNameTag();
                String deathMessage = this.getDeathMessage(villagerName, damageSource, killer);

                if (!world.isRemote && world.getMinecraftServer() != null)
                {
                    world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentString(deathMessage));
                }
            }
        }
    }

    private String getDeathMessage(String entity, DamageSource damageSource, Entity killer)
    {
        if (damageSource.isFireDamage())
        {
            return entity + " burned in flames";
        }
        else if (damageSource == DamageSource.LAVA)
        {
            return entity + " tried to swim in lava";
        }
        else if (damageSource == DamageSource.DROWN)
        {
            return entity + " drowned";
        }
        else if (damageSource == DamageSource.FALL)
        {
            return entity + " couldn't survive the fall";
        }
        else if (damageSource == DamageSource.CACTUS)
        {
            return entity + " got pricked by a cactus";
        }
        else if (damageSource == DamageSource.STARVE)
        {
            return entity + " starved to death";
        }
        else if (damageSource == DamageSource.WITHER)
        {
            return entity + " withered away";
        }
        else if (damageSource.getDamageType().equals("player") && killer != null)
        {
            return entity + " was slain by player " + killer.getName();
        }
        else if (damageSource.getDamageType().equals("mob") && killer != null)
        {
            return entity + " was slain by " + killer.getName();
        }
        else if (damageSource == DamageSource.MAGIC)
        {
            return entity + " was killed by magic";
        }
        else if (damageSource == DamageSource.LIGHTNING_BOLT)
        {
            return entity + " was struck by lightning";
        }
        else
        {
            return entity + " died under mysterious circumstances";
        }
    }
}
