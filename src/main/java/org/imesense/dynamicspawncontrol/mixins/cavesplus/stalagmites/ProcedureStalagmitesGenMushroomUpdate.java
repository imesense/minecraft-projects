package org.imesense.dynamicspawncontrol.mixins.cavesplus.stalagmites;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.mcreator.caves.procedure.ProcedureStalagmitesGenMushroom;

@Mixin(value = ProcedureStalagmitesGenMushroom.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class ProcedureStalagmitesGenMushroomUpdate
{
    @Overwrite
    public static void executeProcedure(HashMap<String, Object> dependencies)
    {
    }
}
