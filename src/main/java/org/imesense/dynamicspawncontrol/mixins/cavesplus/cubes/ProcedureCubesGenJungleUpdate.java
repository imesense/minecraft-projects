package org.imesense.dynamicspawncontrol.mixins.cavesplus.cubes;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.mcreator.caves.procedure.ProcedureCubesGenJungle;

@Mixin(value = ProcedureCubesGenJungle.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class ProcedureCubesGenJungleUpdate
{
    @Overwrite
    public static void executeProcedure(HashMap<String, Object> dependencies)
    {
    }
}
