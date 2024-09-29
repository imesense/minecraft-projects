package org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DCSSingleConfig
{
    /**
     *
     * @return
     */
    String fileName();
}
