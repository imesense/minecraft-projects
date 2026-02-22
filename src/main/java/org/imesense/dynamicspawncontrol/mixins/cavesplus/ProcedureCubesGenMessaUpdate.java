package org.imesense.dynamicspawncontrol.mixins.cavesplus;

import net.mcreator.caves.procedure.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.HashMap;

@Mixin(value = ProcedureCubesGenMessa.class, remap = false)
public abstract class ProcedureCubesGenMessaUpdate
{
    @Overwrite
    public static void executeProcedure(HashMap<String, Object> dependencies)
    {

    }
}