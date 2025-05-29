package org.imesense.dynamicspawncontrol.core.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.ai.spider.event.OnEventAvoidLight;
import org.imesense.dynamicspawncontrol.ai.zombie.event.OnEventBreakTorch;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.memory.MemoryEvents;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventMobTaskManager;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventWorldCache;
import org.imesense.dynamicspawncontrol.eventdescriptions.PlayerNetwork;
import org.imesense.dynamicspawncontrol.eventdescriptions.UpdateFire;

@TODO(value = "Отключена до следующих версий, логика, когда зомби идет разбивать факел. Не правильно работает приоритет. 2 Задача, перенести новое событие для нового парсера",
        showOnce = false,
        priority = TODO.TodoPriority.HIGH)
@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntityJoinWorldEvent extends BaseOnEventInstance
{
    public OnEventEntityJoinWorldEvent()
    {

    }

    static boolean isTestLogic = false;

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnEntityJoinWorldEvent_LOW(EntityJoinWorldEvent event)
    {
        PlayerNetwork.getInstance().handlePlayerJoinWorld(event);

        UpdateFire.getInstance().handleEntityJoinWorld(event);

        if (isTestLogic)
        {
            OnEventBreakTorch.getInstance().handleSearchToBreakTorch(event);
        }

        OnEventAvoidLight.getInstance().handleSpiderSpawn(event);

        OnEventMobTaskManager.getInstance().handleUpdateEntityJoinWorld(event);

        OnEventWorldCache.getInstance().handleEntityJoinWorld(event);

        MemoryEvents.handleOnPlayerLogin(event);
    }

    @SubscribeEvent
    public void onTestParserSpawn(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote)
        {
            Entity entity = event.getEntity();

            if (entity instanceof EntityLiving)
            {
                ResourceLocation entityId = EntityRegistry.getEntry(entity.getClass()).getRegistryName();

                if (entityId != null)
                {
                    String mobId = entityId.toString();

                    if (mobId.equals("srparasites:warden") ||
                            mobId.equals("srparasites:marauder") ||
                            mobId.equals("srparasites:grunt") ||
                            mobId.equals("minecraft:zombie_pigman") ||
                            mobId.equals("specialmobs:hellfireblaze"))
                    {
                        String mobName = getMobDisplayName(mobId);

                        String message = String.format("%s%s появился на высоте %s%d блоков%s!",
                                TextFormatting.RED,
                                mobName,
                                TextFormatting.YELLOW,
                                (int)entity.posY,
                                TextFormatting.RED);

                        event.getWorld().getMinecraftServer().getPlayerList()
                                .sendMessage(new TextComponentString(message));
                    }
                }
            }
        }
    }

    private String getMobDisplayName(String mobId)
    {
        switch(mobId)
        {
            case "srparasites:warden":
                return "Хранитель";
            case "srparasites:marauder":
                return "Мародер";
            case "srparasites:grunt":
                return "Пехотинец";
            case "specialmobs:hellfireblaze":
                return "Адский огонь";
            default:
                return mobId;
        }
    }
}
