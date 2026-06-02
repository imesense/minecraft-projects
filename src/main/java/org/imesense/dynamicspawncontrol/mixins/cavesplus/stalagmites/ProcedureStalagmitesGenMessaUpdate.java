package org.imesense.dynamicspawncontrol.mixins.cavesplus.stalagmites;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.mcreator.caves.procedure.ProcedureStalagmitesGenMessa;

@Mixin(value = ProcedureStalagmitesGenMessa.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class ProcedureStalagmitesGenMessaUpdate
{
    @Overwrite
    public static void executeProcedure(HashMap<String, Object> dependencies)
    {
    }
}
