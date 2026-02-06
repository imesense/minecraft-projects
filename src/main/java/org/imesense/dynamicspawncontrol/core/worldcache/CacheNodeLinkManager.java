package org.imesense.dynamicspawncontrol.core.worldcache;

import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CacheNodeLinkManager
{
    private static CacheNodeLinkManager INSTANCE;

    private final Map<Long, CacheEntityStorage.EntityData> cacheNodes = new HashMap<>();
    private final Map<Long, List<CacheGameEventStorage.GameEventData>> eventNodes = new HashMap<>();
    private final Set<String> duplicateEntities = new HashSet<>();
    private final Set<Long> activeEventNodes = ConcurrentHashMap.newKeySet();

    public static CacheNodeLinkManager getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new CacheNodeLinkManager();
        }

        return INSTANCE;
    }

    public void registerCacheNode(Long idNode, CacheEntityStorage.EntityData entityData)
    {
        if (idNode != null)
        {
            cacheNodes.put(idNode, entityData);
        }
    }

    public void registerEventNode(Long idNode, CacheGameEventStorage.GameEventData eventData)
    {
        if (idNode != null)
        {
            eventNodes.computeIfAbsent(idNode, k -> new ArrayList<>()).add(eventData);
        }
    }

    public void clearAll()
    {
        cacheNodes.clear();
        eventNodes.clear();
        duplicateEntities.clear();
        activeEventNodes.clear();
    }

    public void checkForDuplicates(String entityName, Long idNode)
    {
        if (idNode == null)
        {
            if (duplicateEntities.contains(entityName))
            {
                throw new RuntimeException("Duplicate entity configuration found without id_node: " + entityName +
                        ". Entity must have unique id_node when defined in both files.");
            }

            duplicateEntities.add(entityName);
        }
    }

    public void activateNode(Long idNode)
    {
        if (idNode != null)
        {
            activeEventNodes.add(idNode);
            Log.write(0, "Activated node: " + idNode);
        }
    }

    public void deactivateNode(Long idNode)
    {
        if (idNode != null)
        {
            activeEventNodes.remove(idNode);
            Log.write(0, "Deactivated node: " + idNode);
        }
    }

    public boolean isNodeActive(Long idNode)
    {
        return idNode != null && activeEventNodes.contains(idNode);
    }

    public CacheEntityStorage.EntityData getCacheDataForNode(Long idNode)
    {
        return cacheNodes.get(idNode);
    }

    public List<CacheGameEventStorage.GameEventData> getEventDataForNode(Long idNode)
    {
        return eventNodes.getOrDefault(idNode, new ArrayList<>());
    }

    public void checkAllNodesActiveForDay(int day, int dimension)
    {
        activeEventNodes.clear();

        for (Map.Entry<Long, List<CacheGameEventStorage.GameEventData>> entry : eventNodes.entrySet())
        {
            for (CacheGameEventStorage.GameEventData eventData : entry.getValue())
            {
                if (eventData.idDimension != null && eventData.idDimension != dimension)
                {
                    continue;
                }

                boolean dayCondition = false;

                if (eventData.repeat)
                {
                    if (eventData.day > 0 &&
                            day > 0 &&
                            day % eventData.day == 0)
                    {
                        dayCondition = true;
                    }
                }
                else
                {
                    dayCondition = (day == eventData.day);
                }

                if (dayCondition)
                {
                    activateNode(entry.getKey());
                    break;
                }
            }
        }
    }
}