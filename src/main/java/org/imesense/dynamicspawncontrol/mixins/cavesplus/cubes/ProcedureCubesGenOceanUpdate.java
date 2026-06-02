package org.imesense.dynamicspawncontrol.mixins.cavesplus.cubes;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.mcreator.caves.procedure.ProcedureCubesGenOcean;

@Mixin(value = ProcedureCubesGenOcean.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class ProcedureCubesGenOceanUpdate
{
    @Overwrite
    public static void executeProcedure(HashMap<String, Object> dependencies)
    {
    }
}
