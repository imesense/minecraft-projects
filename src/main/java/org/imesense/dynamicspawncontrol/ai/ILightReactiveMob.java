package org.imesense.dynamicspawncontrol.ai;

import org.imesense.dynamicspawncontrol.core.annotation.TODO;

@TODO(value = "Add on diagram project", showOnce = false, priority = TODO.TodoPriority.HIGH)
public interface ILightReactiveMob
{
    /** Может ли моб реагировать на источники света */
    boolean canReactToLight();

    /** Шанс реакции (0.0 – 1.0) */
    float getLightReactionChance();

    /** Максимальная дистанция поиска источника */
    int getLightSearchRadius();
}
