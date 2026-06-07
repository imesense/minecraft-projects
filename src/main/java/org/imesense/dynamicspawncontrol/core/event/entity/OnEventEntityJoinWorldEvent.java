package org.imesense.dynamicspawncontrol.core.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.entity.spider.SpiderAvoidLightEventHandler;
import org.imesense.dynamicspawncontrol.entity.zombie.ZombieBreakTorchEventHandler;
import org.imesense.dynamicspawncontrol.entity.zombie.ZombieHasShieldEventHandler;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.base.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.memory.MemoryEvents;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventMobTaskManager;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventWorldCache;
import org.imesense.dynamicspawncontrol.eventdescriptions.PlayerNetwork;
import org.imesense.dynamicspawncontrol.eventdescriptions.UpdateFire;

@TODO(
        // = "Отключена до следующих версий, логика, когда зомби идет разбивать факел. Не правильно работает приоритет. 2 Задача, перенести новое событие для нового парсера",
        value = "TODO 1",
        showOnce = false,
        priority = TODO.TodoPriority.HIGH)
@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntityJoinWorldEvent extends BaseOnEventInstance
{
    public OnEventEntityJoinWorldEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnEntityJoinWorldEvent_LOW(EntityJoinWorldEvent event)
    {
        PlayerNetwork.getInstance().handlePlayerJoinWorld(event);

        UpdateFire.getInstance().handleEntityJoinWorld(event);

        if (!event.getEntity().world.isRemote)
        {
            OnEventMobTaskManager.getInstance().handleUpdateEntityJoinWorld(event);

            OnEventWorldCache.getInstance().handleEntityJoinWorld(event);

            ZombieHasShieldEventHandler.getInstance().handleEntityJoinWorld(event);

            ZombieBreakTorchEventHandler.getInstance().handleSearchToBreakTorch(event);

            SpiderAvoidLightEventHandler.getInstance().handleSpiderSpawn(event);
        }

        MemoryEvents.handleOnPlayerLogin(event);
    }

    //@SubscribeEvent // disabled on ver. 0.1
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
                            mobId.equals("specialmobs:hellfireblaze") ||
                            mobId.equals("minecraft:wither_skeleton") ||
                            mobId.equals("srparasites:hi_golem") ||
                            mobId.equals("minecraft:cave_spider") ||
                            mobId.equals("minecraft:slime") ||
                            mobId.equals("minecraft:silverfish") ||
                            mobId.equals("minecraft:vex") ||
                            mobId.equals("srparasites:heed") ||
                            mobId.equals("srparasites:crux") ||
                            mobId.equals("srparasites:monarch") ||
                            mobId.equals("minecraft:blaze")
                    )
                    {
                        String mobName = getMobDisplayName(mobId);

                        String biomeName = event.getWorld().getBiome(new BlockPos(entity)).getBiomeName();

                        String message = String.format("%s%s %sappeared on coordinates %sX: %s%d Y: %s%d Z: %s%d %s(biome: %s%s%s)%s",
                                TextFormatting.GREEN,
                                mobName,
                                TextFormatting.YELLOW,
                                TextFormatting.GOLD,
                                TextFormatting.GOLD,
                                (int)entity.posX,
                                TextFormatting.GOLD,
                                (int)entity.posY,
                                TextFormatting.GOLD,
                                (int)entity.posZ,
                                TextFormatting.WHITE,
                                TextFormatting.GREEN,
                                biomeName,
                                TextFormatting.WHITE,
                                TextFormatting.RESET
                        );

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
                return "(SRP) Warden";
            case "srparasites:marauder":
                return "(SRP) Marauder";
            case "srparasites:grunt":
                return "(SRP) Grunt";
            case "specialmobs:hellfireblaze":
                return "(Special mobs) Hell Fire Blaze";
            case "minecraft:wither_skeleton":
                return "(Minecraft) Wither Skeleton";
            case "minecraft:zombie_pigman":
                return "(Minecraft) Zombie Pigman";
            case "srparasites:hi_golem":
                return "(SRP) Captured Golem";
            case "minecraft:cave_spider":
                return "(Minecraft) Cave spider";
            case "minecraft:slime":
                return "(Minecraft) Slime";
            case "minecraft:silverfish":
                return "(Minecraft) Silverfish";
            case "minecraft:vex":
                return "(Minecraft) Vex";
            case "srparasites:heed":
                return "(SRP) Heed";
            case "srparasites:crux":
                return "(SRP) Crux";
            case "srparasites:monarch":
                return "(SRP) Monarch";
            case "minecraft:blaze":
                return "(Minecraft) Blaze";
            default:
                return mobId;
        }
    }
}
