package org.imesense.dynamicspawncontrol.core.memory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryEvents
{
    public static long lastCleanTime = 0L;
    public static List<UUID> recognizedPlayers = new ArrayList();
    public static int idleTime = 0;

    @SideOnly(Side.CLIENT)
    //@SubscribeEvent
    public static void handleOnClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (!Minecraft.getMinecraft().isGamePaused() && event.phase == TickEvent.Phase.END && player != null && player.world.isRemote) {
            boolean doClean = false;
            if (System.currentTimeMillis() - lastCleanTime > (long)Configuration.AutomaticCleanup.minInterval * 1000L) {
                if ((double)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (double)Runtime.getRuntime().totalMemory() > (double)Configuration.forceCleanPercentage / 100.0D) {
                    doClean = true;
                } else if (Configuration.AutomaticCleanup.autoCleanup) {
                    if (idleTime > Configuration.AutomaticCleanup.minIdleTime * 20) {
                        doClean = true;
                    }

                    if (System.currentTimeMillis() - lastCleanTime > (long)Configuration.AutomaticCleanup.maxInterval * 1000L) {
                        doClean = true;
                    }
                }

                if (doClean) {
                    MemoryManager.cleanMemory(player);
                    lastCleanTime = System.currentTimeMillis();
                    idleTime = 0;
                }

                if (Configuration.AutomaticCleanup.autoCleanup) {
                    if (player.motionX < 0.001D && player.motionY < 0.001D && player.motionZ < 0.001D) {
                        ++idleTime;
                    } else {
                        idleTime = 0;
                    }
                }
            }
        }

    }

    @SideOnly(Side.CLIENT)
    //@SubscribeEvent
    public static void handleOnPlayerLogin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer && event.getWorld().isRemote) {
            EntityPlayer player = (EntityPlayer)event.getEntity();
            if (player.getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID()) && !recognizedPlayers.contains(player.getUniqueID())) {
                if (Configuration.cleanOnJoin) {
                    MemoryManager.cleanMemory(player);
                }

                lastCleanTime = System.currentTimeMillis();
                idleTime = 0;
                recognizedPlayers.add(player.getUniqueID());
            }
        }

    }

    //@SubscribeEvent
    //public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
    //    if (eventArgs.getModID().equals("memorycleaner")) {
    //        MemoryCleaner.logger.info("MemoryCleaner Config Changed!");
    //        ConfigManager.sync("memorycleaner", Type.INSTANCE);
    //    }

    //}
}
