package org.imesense.dynamicspawncontrol.core.mixinconfig.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MixinConfigFile
{
    String value();
    String description() default "";
    boolean createIfAbsent() default true;
}