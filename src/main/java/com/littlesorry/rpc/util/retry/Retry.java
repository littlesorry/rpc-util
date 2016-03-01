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

    /**
     * method level max retry time config
     */
    int value() default -1;

    /**
     * exception to exit invoking
     */
    Class<? extends Throwable>[] breakOn() default {};

    /**
     * exception to continue retry
     * , if this is configured
     * , only matching exception will continue retry process
     * , otherwise retry process will exit     * @return
     */
    Class<? extends Throwable>[] continueOn() default {};


    /**
     * interface to provide more precise logic flow control
     */
    Class<? extends LoopPolicy> loopPolicy() default LoopPolicy.NONE_POLICY.class;
}
