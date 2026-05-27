package org.imesense.dynamicspawncontrol.bloodmoonmanager;

import java.lang.reflect.InvocationTargetException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

public class BloodmoonHandler extends WorldSavedData {
    public static BloodmoonHandler INSTANCE;
    private BloodmoonSpawner bloodMoonSpawner;
    boolean bloodMoon;
    boolean forceBloodMoon;
    int nightCounter;

    public BloodmoonHandler() {
        super(DynamicSpawnControlStructure.STRUCT_INFO_MOD.NAME);
        this.bloodMoonSpawner = new BloodmoonSpawner();
        this.bloodMoon = false;
        this.forceBloodMoon = false;
    }

    public BloodmoonHandler(String name) {
        super(DynamicSpawnControlStructure.STRUCT_INFO_MOD.NAME);
        this.bloodMoonSpawner = new BloodmoonSpawner();
        this.bloodMoon = false;
        this.forceBloodMoon = false;
    }

    public void playerJoinedWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && (event.getEntity() instanceof EntityPlayer) && this.bloodMoon) {
            PacketHandler.INSTANCE.sendTo(new MessageBloodmoonStatus(this.bloodMoon), (EntityPlayerMP) event.getEntity());
        }
    }

    public void endWorldTick(TickEvent.WorldTickEvent event) throws IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            WorldServer worldServer = (WorldServer) event.world;
            if (((World) worldServer).provider.getDimension() == 0) {
                int time = (int) (worldServer.getWorldTime() % 24000);
                if (isBloodmoonActive()) {
                    if (!BloodMoonConfig.getInstance(BloodMoonConfig.class).getGeneral().isRespectGamerule() || worldServer.getGameRules().getBoolean("doMobSpawning")) {
                        for (int i = 0; i < BloodMoonConfig.getInstance(BloodMoonConfig.class).getSpawning().getSpawnSpeed(); i++) {
                            this.bloodMoonSpawner.findChunksForSpawning(worldServer, worldServer.getDifficulty() != EnumDifficulty.PEACEFUL, false, false);
                        }
                    }
                    if (time >= 0 && time < 12000) {
                        setBloodmoon(false);
                        return;
                    }
                    return;
                }
                if (time == 12000) {
                    if (BloodMoonConfig.getInstance(BloodMoonConfig.class).getSchedule().getNthNight() != 0) {
                        this.nightCounter--;
                        if (this.nightCounter < 0) {
                            this.nightCounter = BloodMoonConfig.getInstance(BloodMoonConfig.class).getSchedule().getNthNight();
                        }
                        markDirty();
                    }
                    if (this.forceBloodMoon || Math.random() < BloodMoonConfig.getInstance(BloodMoonConfig.class).getSchedule().getChance() || ((BloodMoonConfig.getInstance(BloodMoonConfig.class).getSchedule().isFullmoon() && worldServer.getCurrentMoonPhaseFactor() == 1.0f) || (BloodMoonConfig.getInstance(BloodMoonConfig.class).getSchedule().getNthNight() != 0 && this.nightCounter == 0))) {
                        this.forceBloodMoon = false;
                        setBloodmoon(true);
                        if (BloodMoonConfig.getInstance(BloodMoonConfig.class).getGeneral().isSendMessage()) {
                            for (Object object : ((World) worldServer).playerEntities) {
                                EntityPlayer player = (EntityPlayer) object;
                                player.sendMessage(new TextComponentTranslation("text.bloodmoon.notify", new Object[0]).setStyle(new Style().setColor(TextFormatting.RED)));
                            }
                        }
                        if (this.nightCounter == 0 && BloodMoonConfig.getInstance(BloodMoonConfig.class).getSchedule().getNthNight() != 0) {
                            this.nightCounter = BloodMoonConfig.getInstance(BloodMoonConfig.class).getSchedule().getNthNight();
                            markDirty();
                        }
                    }
                }
            }
        }
    }

    private void setBloodmoon(boolean bloodMoon) {
        if (this.bloodMoon != bloodMoon) {
            PacketHandler.INSTANCE.sendToDimension(new MessageBloodmoonStatus(bloodMoon), 0);
            markDirty();
        }
        this.bloodMoon = bloodMoon;
    }

    public void updateClients() {
        PacketHandler.INSTANCE.sendToDimension(new MessageBloodmoonStatus(this.bloodMoon), 0);
    }

    public void force() {
        this.forceBloodMoon = true;
        markDirty();
    }

    public boolean isBloodmoonActive() {
        return this.bloodMoon;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        this.bloodMoon = nbt.getBoolean("bloodMoon");
        this.forceBloodMoon = nbt.getBoolean("forceBloodMoon");
        this.nightCounter = nbt.getInteger("nightCounter");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("bloodMoon", this.bloodMoon);
        nbt.setBoolean("forceBloodMoon", this.forceBloodMoon);
        nbt.setInteger("nightCounter", this.nightCounter);
        return nbt;
    }

    public boolean isBloodmoonScheduled() {
        return this.forceBloodMoon;
    }

    public void stop() {
        setBloodmoon(false);
    }
}

