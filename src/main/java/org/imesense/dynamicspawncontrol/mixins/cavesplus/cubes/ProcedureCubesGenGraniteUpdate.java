package org.imesense.dynamicspawncontrol.mixins.cavesplus.cubes;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.mcreator.caves.procedure.ProcedureCubesGenGranite;

@Mixin(value = ProcedureCubesGenGranite.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class ProcedureCubesGenGraniteUpdate
{
    @Overwrite
    public static void executeProcedure(HashMap<String, Object> dependencies)
    {
    }
}
