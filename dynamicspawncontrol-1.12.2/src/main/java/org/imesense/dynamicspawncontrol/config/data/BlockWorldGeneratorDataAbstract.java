package org.imesense.dynamicspawncontrol.config.data;

import lombok.*;
import lombok.experimental.Accessors;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

/**
 *
 */
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
public abstract class BlockWorldGeneratorDataAbstract
{
    /**
     *
     */
    private String category;
    private Integer chanceSpawn;
    private Integer minHeight;
    private Integer maxHeight;

    /**
     *
     */
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }
}
