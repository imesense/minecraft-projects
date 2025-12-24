package org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.datasupport;

import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data.PotionEffect;

import java.util.List;

public final class AdditionalChecks
{
    public AdditionalChecks()
    {

    }

    public static final class Data
    {
        public Boolean seeSky;
        public Integer idDimension;
        public ResourceLocation entityType;
        public List<PotionEffect.Data> potion;
    }
}
