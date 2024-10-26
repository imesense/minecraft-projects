package org.imesense.dynamicspawncontrol.debug.event;

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
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.debug.IDebug;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import scala.util.Random;

import java.util.Objects;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventDummy implements IDebug
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventDummy()
    {
		CodeGenericUtil.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    private static final Random random = new Random();

    //-' Присваиваем случайные имена зомби при спавне
    @SubscribeEvent
    public void onZombieSpawn(LivingSpawnEvent.SpecialSpawn event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntity();

            //-' Присваиваем случайное имя с некоторым шансом
            if (random.nextFloat() < 0.3f)
            {
                String[] randomNames = {"Adik", "Boris", "Igor", "Alex", "Nikolay"};
                String randomName = randomNames[random.nextInt(randomNames.length)];
                zombie.setCustomNameTag(randomName);
                zombie.setAlwaysRenderNameTag(true); // debug
            }
        }
    }

    @SubscribeEvent
    public void onZombieDeath(LivingDeathEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntity();
            World world = zombie.getEntityWorld();
            DamageSource source = event.getSource();
            Entity killer = source.getTrueSource();

            //-' Проверяем, есть ли у зомби имя
            if (zombie.hasCustomName())
            {
                String zombieName = zombie.getCustomNameTag();

                //-' Определяем причину смерти
                String deathMessage = getDeathMessage(zombieName, source, killer);

                //-' Проверка на серверную сторону и наличие сервера
                if (!world.isRemote && world.getMinecraftServer() != null)
                {
                    if (random.nextFloat() < 0.75f)
                    {
                        // Отправляем сообщение в чат для всех игроков
                        world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentString(deathMessage));
                    }
                }
            }
        }
    }

    //-' Метод для получения сообщения о смерти
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
