package org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storage;

import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data.PotionEffect;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class SupportCheckSpawnStorage
{
    private static volatile SupportCheckSpawnStorage _INSTANCE;

    public static SupportCheckSpawnStorage getInstance()
    {
        return CodeGeneric.getInstance(SupportCheckSpawnStorage.class);
    }

    public SupportCheckSpawnStorage()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.dataSupportList = new ArrayList<>();;
    }

    public static final class DataSupport
    {
        public Boolean seeSky;
        public ResourceLocation entityType;
        public List<PotionEffect.Data> potion;
    }

    public List<DataSupport> dataSupportList;
}
