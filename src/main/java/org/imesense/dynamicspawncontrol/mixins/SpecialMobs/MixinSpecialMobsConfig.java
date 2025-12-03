package org.imesense.dynamicspawncontrol.mixins.SpecialMobs;

import org.imesense.dynamicspawncontrol.core.annotation.TODO;

@TODO(
        value = "В конфиге special mobs стоит подмена id через параметр _replace_vanilla - исправить. Сделать по умолчанию false. " +
                "Потому что ломается логика паразитов, когда они убивают сущность зомби из этого мода, она не учитывается в списке " +
                "и паразиты не могут изучить данную сущность",
        showOnce = false,
        priority = TODO.TodoPriority.HIGH)
public abstract class MixinSpecialMobsConfig
{

}
