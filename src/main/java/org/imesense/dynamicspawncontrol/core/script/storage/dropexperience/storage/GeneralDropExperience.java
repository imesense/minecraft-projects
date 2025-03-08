package org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.storage;

import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class GeneralDropExperience
{
    private static volatile GeneralDropExperience _INSTANCE;

    public static GeneralDropExperience getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (GeneralDropExperience.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new GeneralDropExperience();
                }
            }
        }
        return _INSTANCE;
    }

    public GeneralDropExperience()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.dropExperienceList = new ArrayList<>();
    }

    public static final class Data
    {
        public ResourceLocation entity;
        public Integer xp;
        public Float multi_xp;
        public Float adding_xp;
    }

    public List<GeneralDropExperience.Data> dropExperienceList;
}
