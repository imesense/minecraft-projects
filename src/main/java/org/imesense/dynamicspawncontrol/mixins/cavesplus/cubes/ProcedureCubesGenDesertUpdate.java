package org.imesense.dynamicspawncontrol.mixins.cavesplus.cubes;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.mcreator.caves.procedure.ProcedureCubesGenDesert;

@Mixin(value = ProcedureCubesGenDesert.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class ProcedureCubesGenDesertUpdate
{
    @Overwrite
    public static void executeProcedure(HashMap<String, Object> dependencies)
    {
    }
}
