package org.imesense.dynamicspawncontrol.mixins.cavesplus;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.mcreator.caves.procedure.ProcedureCubesGenDesert;

@Mixin(value = ProcedureCubesGenDesert.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class ProcedureCubesGenDesertUpdate
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
