package org.imesense.dynamicspawncontrol.bloodmoonmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IVec3dAccessor;
import org.lwjgl.opengl.GL11;

public class ClientBloodmoonHandler
{
    public static ClientBloodmoonHandler INSTANCE = new ClientBloodmoonHandler();
    float lightSub;
    public float fogRemove;
    float skyColorAdd;
    float moonColorRed;
    public static double sin;
    final float sinMax = 2.6179937E-4f;
    float d = 6.666667E-5f;
    int difTime = 0;
    boolean bloodmoonActive = false;
    public float fogStrength = 0.0f;

    public boolean isBloodmoonActive() {
        return this.bloodmoonActive;
    }

    public void setBloodmoon(boolean active) {
        this.bloodmoonActive = active;
    }

    public void moonColorHook() {
        if (isBloodmoonActive() && BloodMoonConfig.getInstance(BloodMoonConfig.class).getAppearance().isRedMoon()) {
            GL11.glColor3f(0.8f, 0.0f, 0.0f);
        }
    }

    public Vec3d skyColorHook(Vec3d color) {
        if (isBloodmoonActive() && BloodMoonConfig.getInstance(BloodMoonConfig.class).getAppearance().isRedSky()) {
            // Кастуем к нашему интерфейсу и меняем значение
            ((IVec3dAccessor) color).setX(color.x + skyColorAdd);
            return color; // Возвращаем тот же объект, но с измененным X
        }
        return color;
    }

    public int manipulateRed(int position, int originalValue) {
        return originalValue;
    }

    public int manipulateGreen(int position, int originalValue) {
        int height;
        if (isBloodmoonActive() && BloodMoonConfig.getInstance(BloodMoonConfig.class).getAppearance().isRedLight() && (height = position / 16) < 16) {
            float mod = 0.0625f * height;
            return Math.max((int) (originalValue - ((mod * this.lightSub) * ((this.sin / 2.0d) + 1.0d))), 0);
        }
        return originalValue;
    }

    public int manipulateBlue(int position, int originalValue) {
        int height;
        if (isBloodmoonActive() && BloodMoonConfig.getInstance(BloodMoonConfig.class).getAppearance().isRedLight() && (height = position / 16) < 16) {
            float mod = 0.0625f * height;
            return Math.max((int) (originalValue - ((mod * this.lightSub) * 2.3f)), 0);
        }
        return originalValue;
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (isBloodmoonActive()) {
            WorldClient world = Minecraft.getMinecraft().world;
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (world != null && player != null) {
                float difTime = ((int) (world.getWorldTime() % 24000)) - 12000;
                this.sin = Math.sin(difTime * 2.6179937E-4f);
                this.fogStrength = MathHelper.clamp((float)((this.sin + 1.0) / 2.0), 0.0f, 1.0f);
                this.lightSub = (float) (this.sin * 150.0d);
                this.skyColorAdd = (float) (this.sin * 0.10000000149011612d);
                this.moonColorRed = (float) (this.sin * 0.699999988079071d);
                this.fogRemove = (float) (this.sin * this.d * 6000.0d);
                if (world.provider.getDimension() != 0) {
                    this.bloodmoonActive = false;
                    return;
                }
                return;
            }
            if (this.bloodmoonActive) {
                this.bloodmoonActive = false;
            }
        }
    }
}
