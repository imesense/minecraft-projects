package org.imesense.dynamicspawncontrol.mechanic.bloodmoon;

import java.lang.reflect.InvocationTargetException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

public class BloodmoonEventHandler
{
    @SubscribeEvent
    public void loadWorld(WorldEvent.Load event) {
        if (!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0) {
            BloodmoonHandler.INSTANCE = (BloodmoonHandler) event.getWorld().getMapStorage().getOrLoadData(BloodmoonHandler.class, DynamicSpawnControlStructure.STRUCT_INFO_MOD.NAME);
            if (BloodmoonHandler.INSTANCE == null) {
                BloodmoonHandler.INSTANCE = new BloodmoonHandler();
                BloodmoonHandler.INSTANCE.markDirty();
            }
            event.getWorld().getMapStorage().setData(DynamicSpawnControlStructure.STRUCT_INFO_MOD.NAME, BloodmoonHandler.INSTANCE);
            BloodmoonHandler.INSTANCE.updateClients();
        }
    }

    @SubscribeEvent
    public void livingDrops(LivingDropsEvent event) {
        if (!event.getEntityLiving().world.isRemote && event.getSource() == DamageSource.OUT_OF_WORLD && event.getEntityLiving().getEntityData().getBoolean("bloodmoonSpawned")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void livingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (BloodMoonConfig.getInstance(BloodMoonConfig.class).getGeneral().isVanish() && BloodmoonHandler.INSTANCE != null && event.getEntityLiving().dimension == 0 && !event.getEntityLiving().world.isRemote && !BloodmoonHandler.INSTANCE.isBloodmoonActive() && event.getEntityLiving().world.getTotalWorldTime() % 20 == 0 && Math.random() <= 0.20000000298023224d && event.getEntityLiving().getEntityData().getBoolean("bloodmoonSpawned")) {
            event.getEntityLiving().onKillCommand();
        }
    }

    @SubscribeEvent
    public void sleepInBed(PlayerSleepInBedEvent event) {
        if (BloodmoonHandler.INSTANCE != null && BloodMoonConfig.getInstance(BloodMoonConfig.class).getGeneral().isNoSleep() && DynamicSpawnControl.isBloodmoon()) {
            event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
            event.getEntityPlayer().sendMessage(new TextComponentTranslation("text.bloodmoon.nosleep", new Object[0]).setStyle(new Style().setColor(TextFormatting.RED)));
        }
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        ConfigManager.sync(DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public void playerJoinedWorld(EntityJoinWorldEvent event) {
        if (BloodmoonHandler.INSTANCE != null && !event.getWorld().isRemote) {
            BloodmoonHandler.INSTANCE.playerJoinedWorld(event);
        }
    }

    @SubscribeEvent
    public void endWorldTick(TickEvent.WorldTickEvent event) throws IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (BloodmoonHandler.INSTANCE != null) {
            BloodmoonHandler.INSTANCE.endWorldTick(event);
        }
    }
}
