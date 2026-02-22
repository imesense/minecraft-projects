package org.imesense.dynamicspawncontrol.mixins.cavesplus;

import net.mcreator.caves.procedure.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.HashMap;

@Mixin(value = ProcedureCubesGenMushroom.class, remap = false)
public abstract class ProcedureCubesGenMushroomUpdate
{
    @Overwrite
    public static void executeProcedure(HashMap<String, Object> dependencies)
    {

    }
}