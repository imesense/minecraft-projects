package org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.storage;

import org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.data.EntityDropExperience;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class GeneralDropExperience
{
    private static volatile GeneralDropExperience _INSTANCE;

    public static GeneralDropExperience getInstance()
    {
        return CodeGeneric.getInstance(GeneralDropExperience.class);
    }

    public GeneralDropExperience()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.dropExperienceList = new ArrayList<>();
    }

    public List<EntityDropExperience.Data> dropExperienceList;
}
