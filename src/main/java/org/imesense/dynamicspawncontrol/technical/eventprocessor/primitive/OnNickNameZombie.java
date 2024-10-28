package org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CheckDebugger;
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
public final class OnNickNameZombie
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
    public OnNickNameZombie()
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

        if (inputStream == null)
        {
            System.err.println("Файл names.txt не найден!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)))
        {
            String line;

            while ((line = reader.readLine()) != null)
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
     * @param event
     */
    @SubscribeEvent
    public void onZombieSpawn_0(LivingSpawnEvent.SpecialSpawn event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntity();

            if (RANDOM.nextFloat() < 0.3f)
            {
                String randomName = this.RANDOM_NAMES.get(RANDOM.nextInt(this.RANDOM_NAMES.size()));

                zombie.setCustomNameTag(randomName);
                zombie.setAlwaysRenderNameTag(CheckDebugger.instance.IsRunDebugger);
            }
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public void onZombieDeath_1(LivingDeathEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntity();
            World world = zombie.getEntityWorld();
            DamageSource source = event.getSource();
            Entity killer = source.getTrueSource();

            if (zombie.hasCustomName())
            {
                String zombieName = zombie.getCustomNameTag();

                String deathMessage = this.getDeathMessage(zombieName, source, killer);

                if (!world.isRemote && world.getMinecraftServer() != null)
                {
                    if (RANDOM.nextFloat() < 0.75f)
                    {
                        world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentString(deathMessage));
                    }
                }
            }
        }
    }

    /**
     *
     * @param zombieName
     * @param source
     * @param killer
     * @return
     */
    private String getDeathMessage(String zombieName, DamageSource source, Entity killer)
    {
        if (source.isFireDamage())
        {
            return zombieName + " burned in flames";
        }
        else if (source == DamageSource.LAVA)
        {
            return zombieName + " tried to swim in lava";
        }
        else if (source == DamageSource.DROWN)
        {
            return zombieName + " drowned";
        }
        else if (source == DamageSource.FALL)
        {
            return zombieName + " couldn't survive the fall";
        }
        else if (source == DamageSource.CACTUS)
        {
            return zombieName + " got pricked by a cactus";
        }
        else if (source == DamageSource.STARVE)
        {
            return zombieName + " starved to death";
        }
        else if (source == DamageSource.WITHER)
        {
            return zombieName + " withered away";
        }
        else if (source.getDamageType().equals("player") && killer != null)
        {
            return zombieName + " was slain by player " + killer.getName();
        }
        else if (source.getDamageType().equals("mob") && killer != null)
        {
            return zombieName + " was slain by " + killer.getName();
        }
        else if (source == DamageSource.MAGIC)
        {
            return zombieName + " was killed by magic";
        }
        else if (source == DamageSource.LIGHTNING_BOLT)
        {
            return zombieName + " was struck by lightning";
        }
        else
        {
            return zombieName + " died under mysterious circumstances";
        }
    }
}
