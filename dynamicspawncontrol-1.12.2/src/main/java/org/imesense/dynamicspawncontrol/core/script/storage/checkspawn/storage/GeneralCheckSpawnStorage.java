package org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storage;

import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data.*;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class GeneralCheckSpawnStorage
{
    private static volatile GeneralCheckSpawnStorage _INSTANCE;

    public static GeneralCheckSpawnStorage getInstance()
    {
        return CodeGeneric.getInstance(GeneralCheckSpawnStorage.class);
    }

    public GeneralCheckSpawnStorage()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.entityEquipmentList = new ArrayList<>();;
        this.entityDescriptionsList = new ArrayList<>();
        this.potionEffectList = new ArrayList<>();;
        this.profilePriorityList = new ArrayList<>();
        this.gameWorldList = new ArrayList<>();
        this.entityAttributesList = new ArrayList<>();
    }

    public List<EntityEquipment.Data> entityEquipmentList;
    public List<ProfilePriority.Data> profilePriorityList;
    public List<GameWorld.Data> gameWorldList;
    public List<EntityDescription.Data> entityDescriptionsList;
    public List<PotionEffect.Data> potionEffectList;
    public List<EntityAttributes.Data> entityAttributesList;
}
