package org.imesense.dynamicspawncontrol.core.annotation.cmdconsole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiredPermission
{
    int value() default 0;
    boolean playerOnly() default true;
    String customPermission() default "";
}
