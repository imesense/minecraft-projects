package org.imesense.dynamicspawncontrol.core.config.blockworldgenerator;

import lombok.*;
import lombok.experimental.Accessors;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

/**
 *
 */
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@InitLog
@AllArgsConstructor
@Accessors(chain = true)
public abstract class BlockWorldGeneratorDataAbstract
{
    private String category;
    private Integer chanceSpawn;
    private Integer minHeight;
    private Integer maxHeight;
}
