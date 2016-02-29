package com.littlesorry.rpc.util.retry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by takara on 2/29/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Retry {

    int value() default -1;

    Class<? extends Throwable>[] stopOn() default {};

    Class<? extends Throwable>[] continueOn() default {};

}
