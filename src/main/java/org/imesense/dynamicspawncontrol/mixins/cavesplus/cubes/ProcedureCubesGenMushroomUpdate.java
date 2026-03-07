package org.imesense.dynamicspawncontrol.mixins.cavesplus.cubes;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.mcreator.caves.procedure.ProcedureCubesGenMushroom;

@Mixin(value = ProcedureCubesGenMushroom.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class ProcedureCubesGenMushroomUpdate
{
    /**
     * @author OldSerpskiStalker
     * @reason Corrupted block was generated
     */
    @Overwrite
    public static void executeProcedure(HashMap<String, Object> dependencies)
    {
    }
}
