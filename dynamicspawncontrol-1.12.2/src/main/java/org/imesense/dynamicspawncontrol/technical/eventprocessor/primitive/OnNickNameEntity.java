package org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.UniqueField;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import scala.util.Random;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnNickNameEntity
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    private static final Random RANDOM = new Random();

    /**
     *
     */
    private final List<String> RANDOM_NAMES = new ArrayList<>();

    /**
     *
     */
    public OnNickNameEntity()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        this.loadNamesFromFile();
    }

    /**
     *
     */
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

    /**
     *
     * @param specialSpawn
     */
    @SubscribeEvent
    public void onEntitySpawn_0(LivingSpawnEvent.SpecialSpawn specialSpawn)
    {
        if (specialSpawn.getEntity() instanceof EntityZombie)
        {
            EntityZombie entityZombie = (EntityZombie) specialSpawn.getEntity();

            if (RANDOM.nextFloat() < 0.3f)
            {
                String randomName =
                        this.RANDOM_NAMES.get(RANDOM.nextInt(this.RANDOM_NAMES.size()));

                entityZombie.setCustomNameTag(randomName);
                entityZombie.setAlwaysRenderNameTag(UniqueField.IDEA_RT);
            }
        }

        if (specialSpawn.getEntity() instanceof EntityVillager)
        {
            EntityVillager entityVillager = (EntityVillager) specialSpawn.getEntity();

            String randomName =
                    this.RANDOM_NAMES.get(RANDOM.nextInt(this.RANDOM_NAMES.size()));

            entityVillager.setCustomNameTag(randomName);
            entityVillager.setAlwaysRenderNameTag(UniqueField.IDEA_RT);
        }
    }

    /**
     *
     * @param livingDeathEvent
     */
    @SubscribeEvent
    public void onEntityDeath_1(LivingDeathEvent livingDeathEvent)
    {
        if (livingDeathEvent.getEntity() instanceof EntityZombie)
        {
            EntityZombie entityZombie = (EntityZombie) livingDeathEvent.getEntity();
            World world = entityZombie.getEntityWorld();
            DamageSource damageSource = livingDeathEvent.getSource();
            Entity killer = damageSource.getTrueSource();

            if (entityZombie.hasCustomName())
            {
                String zombieName = entityZombie.getCustomNameTag();

                String deathMessage = this.getDeathMessage(zombieName, damageSource, killer);

                if (!world.isRemote && world.getMinecraftServer() != null)
                {
                    if (RANDOM.nextFloat() < 0.75f)
                    {
                        world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentString(deathMessage));
                    }
                }
            }
        }

        if (livingDeathEvent.getEntity() instanceof EntityVillager)
        {
            EntityVillager villager = (EntityVillager) livingDeathEvent.getEntity();
            World world = villager.getEntityWorld();
            DamageSource damageSource = livingDeathEvent.getSource();
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

    /**
     *
     * @param entity
     * @param damageSource
     * @param killer
     * @return
     */
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
