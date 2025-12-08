package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.entity.item.*;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;

import java.util.Arrays;
import java.util.List;

//@InitLog
@TODO(value = "Запрещаем спавн всех сущностей на этапе загрузки мира. Новый концепт для работы кеширования в 0.2 версии. TEST", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class NewConceptTestEvent
{
    //public NewConceptTestEvent()
    //{

    //}

    @SubscribeEvent
    public void NewConceptTestEvent1(EntityJoinWorldEvent event)
    {
        World world = event.getWorld();

        // Пропускаем только во время загрузки мира
        if (world.isRemote || !world.provider.isSurfaceWorld())
        {
            return;
        }

        // Отключаем спавн всех сущностей при загрузке мира
        if (!world.playerEntities.isEmpty())
        {
            // Игроки уже есть - значит мир загружен, пропускаем всё
            return;
        }

        // Список разрешенных сущностей (жители, пассивные мобы)
        List<Class<?>> allowedEntities = Arrays.asList(
                EntityVillager.class
                // Добавьте другие мирные мобы по необходимости
        );

        // Отменяем спавн всех остальных сущностей
        boolean isAllowed = false;

        for (Class<?> allowedClass : allowedEntities)
        {
            if (allowedClass.isInstance(event.getEntity()))
            {
                isAllowed = true;
                break;
            }
        }

        // Также разрешаем предметы, опыт и неживые сущности
        if (!isAllowed &&
                !(event.getEntity() instanceof EntityItem) &&
                !(event.getEntity() instanceof EntityXPOrb) &&
                !(event.getEntity() instanceof EntityArmorStand) &&
                !(event.getEntity() instanceof EntityBoat) &&
                !(event.getEntity() instanceof EntityMinecart) &&
                !(event.getEntity() instanceof EntityArrow) &&
                !(event.getEntity() instanceof EntityPlayer)) {

            event.setCanceled(true);
        }
    }
}