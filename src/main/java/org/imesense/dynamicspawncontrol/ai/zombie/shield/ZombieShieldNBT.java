package org.imesense.dynamicspawncontrol.ai.zombie.shield;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;

import static org.imesense.dynamicspawncontrol.ai.zombie.shield.ZombieShieldConfig.*;

public final class ZombieShieldNBT
{
    public static final String SHIELD_HEALTH = "ShieldHealth";
    public static final String SHIELD_COOLDOWN = "ShieldCooldown";
    public static final String LAST_HIT_TIME = "LastHitTime";

    public static void initShieldData(EntityZombie zombie)
    {
        NBTTagCompound data = zombie.getEntityData();

        if (!data.hasKey("DynamicSpawnControl"))
        {
            data.setTag("DynamicSpawnControl", new NBTTagCompound());
        }

        NBTTagCompound dscData = data.getCompoundTag("DynamicSpawnControl");

        if (!dscData.hasKey(SHIELD_HEALTH))
        {
            dscData.setFloat(SHIELD_HEALTH, MAX_SHIELD_HEALTH);
            dscData.setInteger(SHIELD_COOLDOWN, 0);
            dscData.setLong(LAST_HIT_TIME, 0);
        }
    }

    public static NBTTagCompound getDSCData(EntityZombie zombie)
    {
        NBTTagCompound data = zombie.getEntityData();

        if (!data.hasKey("DynamicSpawnControl"))
        {
            initShieldData(zombie);
        }

        return data.getCompoundTag("DynamicSpawnControl");
    }
}
