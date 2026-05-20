package org.imesense.dynamicspawncontrol.bloodmoonmanager;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.mixins.minecraft.IVec3dAccessor;
import org.lwjgl.opengl.GL11;

public class ClientBloodmoonHandler
{
    public static ClientBloodmoonHandler INSTANCE = new ClientBloodmoonHandler();
    float lightSub;
    public double sin;
    private float lastSmoothFactor = 0.0f;
    public static float BLOODMOON_FOG_FACTOR = 0.0f;

    @Getter
    boolean bloodmoonActive = false;

    public void setBloodmoon(boolean active)
    {
        this.bloodmoonActive = active;
    }

    public void moonColorHook()
    {
        if (isBloodmoonActive() && BloodMoonConfig.getInstance(BloodMoonConfig.class).getAppearance().isRedMoon()) {
            GL11.glColor3f(0.8f, 0.0f, 0.0f);
        }
    }

    public Vec3d skyColorHook(Vec3d color)
    {
        if (!isBloodmoonActive() || !BloodMoonConfig.getInstance(BloodMoonConfig.class).getAppearance().isRedSky())
            return color;

        float factor = MathHelper.clamp(BLOODMOON_FOG_FACTOR, 0.0f, 1.0f);

        factor = factor * factor * (3.0f - 2.0f * factor);

        float redBoost   = 0.35f * factor;
        float greenDown  = 0.10f * factor;
        float blueDown   = 0.20f * factor;

        double r = MathHelper.clamp(color.x + redBoost, 0.0, 1.0);
        double g = MathHelper.clamp(color.y - greenDown, 0.0, 1.0);
        double b = MathHelper.clamp(color.z - blueDown, 0.0, 1.0);

        return new Vec3d(r, g, b);
    }

    public int manipulateRed(int position, int originalValue) {
        return originalValue;
    }

    public int manipulateGreen(int position, int originalValue)
    {
        int height;

        if (isBloodmoonActive() && BloodMoonConfig.getInstance(BloodMoonConfig.class).getAppearance().isRedLight() &&
                (height = position / 16) < 16)
        {
            float mod = 0.0625f * height;
            return Math.max((int) (originalValue - ((mod * this.lightSub) * ((this.sin / 2.0d) + 1.0d))), 0);
        }

        return originalValue;
    }

    public int manipulateBlue(int position, int originalValue)
    {
        int height;

        if (isBloodmoonActive() && BloodMoonConfig.getInstance(BloodMoonConfig.class).getAppearance().isRedLight() &&
                (height = position / 16) < 16)
        {
            float mod = 0.0625f * height;
            return Math.max((int) (originalValue - ((mod * this.lightSub) * 2.3f)), 0);
        }

        return originalValue;
    }

    public float getSmoothBlendFactor()
    {
        if (!isBloodmoonActive())
        {
            lastSmoothFactor = lastSmoothFactor * 0.9f;
            if (lastSmoothFactor < 0.01f) lastSmoothFactor = 0.0f;
            return lastSmoothFactor;
        }

        float targetFactor = MathHelper.clamp(BLOODMOON_FOG_FACTOR, 0.0f, 1.0f);

        float smoothingSpeed = 0.15f;

        lastSmoothFactor = lastSmoothFactor + (targetFactor - lastSmoothFactor) * smoothingSpeed;
        lastSmoothFactor = MathHelper.clamp(lastSmoothFactor, 0.0f, 1.0f);

        return lastSmoothFactor;
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event)
    {
        if (!isBloodmoonActive())
        {
            BLOODMOON_FOG_FACTOR = 0.0f;
            return;
        }

        WorldClient world = Minecraft.getMinecraft().world;
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (world == null || player == null)
        {
            BLOODMOON_FOG_FACTOR = 0.0f;
            return;
        }

        if (world.provider.getDimension() != 0)
        {
            this.bloodmoonActive = false;
            BLOODMOON_FOG_FACTOR = 0.0f;
            return;
        }

        float time = world.getWorldTime() % 24000.0f;

        float difTime = time - 12000.0f;
        this.sin = Math.sin(difTime * 2.6179937E-4f);

        this.lightSub = (float)(this.sin * 150.0d);

        if (time >= 11000.0f && time < 12000.0f)
        {
            BLOODMOON_FOG_FACTOR = ((time - 11000.0f) / 1000.0f) * 0.2f;
        }
        else if (time >= 12000.0f && time < 14000.0f)
        {
            BLOODMOON_FOG_FACTOR = (time - 12000.0f) / 2000.0f;
        }
        else if (time >= 14000.0f && time < 22000.0f)
        {
            BLOODMOON_FOG_FACTOR = 1.0f;
        }
        else if (time >= 22000.0f && time < 24000.0f)
        {
            BLOODMOON_FOG_FACTOR = 1.0f - ((time - 22000.0f) / 2000.0f);
        }
        else
        {
            BLOODMOON_FOG_FACTOR = 0.0f;
        }

        BLOODMOON_FOG_FACTOR = MathHelper.clamp(BLOODMOON_FOG_FACTOR, 0.0f, 1.0f);

        LogManager.debug("Bloodmoon tick: active=" + isBloodmoonActive()
                + ", time=" + time
                + ", BLOODMOON_FOG_FACTOR=" + BLOODMOON_FOG_FACTOR);
    }
}
