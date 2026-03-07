package org.imesense.dynamicspawncontrol.mixins.cavesplus;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.mcreator.caves.procedure.ProcedureCubesGenJungle;

@Mixin(value = ProcedureCubesGenJungle.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class ProcedureCubesGenJungleUpdate
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
