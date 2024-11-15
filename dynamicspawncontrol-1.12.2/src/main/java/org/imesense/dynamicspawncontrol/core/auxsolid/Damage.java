package org.imesense.dynamicspawncontrol.core.auxsolid;

import net.minecraft.util.DamageSource;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class Damage
{
    /**
     *
     */
    public static final Map<String, DamageSource> DAMAGE_MAP = new HashMap<>();

    /**
     *
     */
    static
    {
        for
        (
                DamageSource source : new DamageSource[]
                {
                        DamageSource.IN_FIRE, DamageSource.LIGHTNING_BOLT, DamageSource.ON_FIRE,
                        DamageSource.LAVA, DamageSource.HOT_FLOOR, DamageSource.IN_WALL,
                        DamageSource.CRAMMING, DamageSource.DROWN, DamageSource.STARVE,
                        DamageSource.CACTUS, DamageSource.FALL, DamageSource.FLY_INTO_WALL,
                        DamageSource.OUT_OF_WORLD, DamageSource.GENERIC, DamageSource.MAGIC,
                        DamageSource.WITHER, DamageSource.ANVIL, DamageSource.FALLING_BLOCK,
                        DamageSource.DRAGON_BREATH, DamageSource.FIREWORKS
                }
        )
        {
            DAMAGE_MAP.put(source.getDamageType(), source);
        }
    }
}
