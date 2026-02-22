package org.imesense.dynamicspawncontrol.mixins.cavesplus;

import net.mcreator.caves.procedure.ProcedureCubesGenDesert;
import net.mcreator.caves.procedure.ProcedureCubesGenGranite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.HashMap;

@Mixin(value = ProcedureCubesGenGranite.class, remap = false)
public abstract class ProcedureCubesGenGraniteUpdate
{
    @Overwrite
    public static void executeProcedure(HashMap<String, Object> dependencies)
    {

    }
}
