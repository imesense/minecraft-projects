package org.imesense.dynamicspawncontrol.technical.customlibrary;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationDCSJson
{
    String fileName();
}
