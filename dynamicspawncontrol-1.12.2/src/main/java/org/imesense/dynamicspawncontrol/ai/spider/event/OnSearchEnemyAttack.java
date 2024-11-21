package org.imesense.dynamicspawncontrol.ai.spider.event;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.ai.spider.action.DelayedEntityAITask;
import org.imesense.dynamicspawncontrol.ai.spider.action.EntityAISpiderAvoidLight;
//import org.imesense.dynamicspawncontrol.ai.spider.action.EntityAISpiderFearLight;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnSearchEnemyAttack
{
    //-' Вариант 0 (Лагающий)
    /*@SubscribeEvent
    public static void onSpiderSpawn(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntitySpider)
        {
            EntitySpider spider = (EntitySpider) event.getEntity();
            if (spider.getPosition().getY() < 50)
            {
                //-' Принудительно запускаем проверку света при спавне
                spider.tasks.addTask(5, new EntityAISpiderAvoidLight(spider, 1.2, 7));
            }
        }
    }*/

    //-' Вариант 1 (Принудительный тест)
    /**
    @SubscribeEvent
    public static void onSpiderSpawn(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntitySpider)
    {
            EntitySpider spider = (EntitySpider) event.getEntity();
            World world = spider.world;
            BlockPos pos = spider.getPosition();

            //-' Принудительно обновляем освещение
            world.checkLight(pos);

            spider.tasks.addTask(5, new EntityAISpiderAvoidLight(spider, 1.2, 7));
        }
    }
     */

    //-' Вариант 2 (Почти оптимально)
    @SubscribeEvent
    public static void onSpiderSpawn(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntitySpider)
        {
            EntitySpider spider = (EntitySpider) event.getEntity();
            spider.tasks.addTask(5, new DelayedEntityAITask(spider, new EntityAISpiderAvoidLight(spider, 1.2, 7), 20));
        }
    }
}
