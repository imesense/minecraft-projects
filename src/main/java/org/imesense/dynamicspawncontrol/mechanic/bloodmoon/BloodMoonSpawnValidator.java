package org.imesense.dynamicspawncontrol.mechanic.bloodmoon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import java.util.HashMap;
import java.util.Map;

public class BloodMoonSpawnValidator
{
    private static final Map<String, String> classToEntityNameMap = new HashMap<>();

    public static boolean canSpawn(Class<? extends Entity> entityClass) {
        String entityName;
        String entityName2;
        if (BloodMoonConfig.getInstance(BloodMoonConfig.class).getSpawning().getSpawnWhitelist().length == 0) {
            if (BloodMoonConfig.getInstance(BloodMoonConfig.class).getSpawning().getSpawnBlacklist().length == 0) {
                return true;
            }
            String className = entityClass.getName();
            if (classToEntityNameMap.containsKey(className)) {
                entityName2 = classToEntityNameMap.get(className);
            } else {
                entityName2 = getEntityName(entityClass);
                classToEntityNameMap.put(className, entityName2);
            }
            for (int i = 0; i < BloodMoonConfig.getInstance(BloodMoonConfig.class).getSpawning().getSpawnBlacklist().length; i++) {
                if (BloodMoonConfig.getInstance(BloodMoonConfig.class).getSpawning().getSpawnBlacklist()[i].equals(entityName2)) {
                    return false;
                }
            }
            return true;
        }
        String className2 = entityClass.getName();
        if (classToEntityNameMap.containsKey(className2)) {
            entityName = classToEntityNameMap.get(className2);
        } else {
            entityName = getEntityName(entityClass);
            classToEntityNameMap.put(className2, entityName);
        }
        for (int i2 = 0; i2 < BloodMoonConfig.getInstance(BloodMoonConfig.class).getSpawning().getSpawnWhitelist().length; i2++) {
            if (BloodMoonConfig.getInstance(BloodMoonConfig.class).getSpawning().getSpawnWhitelist()[i2].equals(entityName)) {
                return true;
            }
        }
        return false;
    }

    public static String getEntityName(Class<? extends Entity> entityClass) {
        EntityRegistry.EntityRegistration registration;
        String entityName = EntityList.getTranslationName(EntityList.getKey(entityClass));
        if (entityName == null && (registration = EntityRegistry.instance().lookupModSpawn(entityClass, false)) != null) {
            entityName = registration.getEntityName();
        }
        return entityName;
    }
}
