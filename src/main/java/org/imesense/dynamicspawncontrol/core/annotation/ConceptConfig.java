package org.imesense.dynamicspawncontrol.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ConceptConfig
{
    /**
     *
     * @return
     */
    String fileName();
}
