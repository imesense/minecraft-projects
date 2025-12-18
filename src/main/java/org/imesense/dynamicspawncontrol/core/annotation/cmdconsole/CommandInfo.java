package org.imesense.dynamicspawncontrol.core.annotation.cmdconsole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo
{
    String description() default "";
    String usage() default "";
    String[] aliases() default {};
}
