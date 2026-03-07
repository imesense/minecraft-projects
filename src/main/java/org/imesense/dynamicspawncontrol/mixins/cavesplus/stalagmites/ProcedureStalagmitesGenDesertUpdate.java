package org.imesense.dynamicspawncontrol.mixins.cavesplus.stalagmites;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.mcreator.caves.procedure.ProcedureStalagmitesGenDesert;

@Mixin(value = ProcedureStalagmitesGenDesert.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class ProcedureStalagmitesGenDesertUpdate
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
