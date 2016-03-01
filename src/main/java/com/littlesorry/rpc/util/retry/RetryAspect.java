package com.littlesorry.rpc.util.retry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import static java.util.Arrays.asList;

/**
 * Created by takara on 2/29/16.
 */
@Aspect
public class RetryAspect {

    private Integer defaultRetryTimes = 3;

    @Around("@annotation(retry)")
    public Object aroundRetry(ProceedingJoinPoint pjp, Retry retry) throws Throwable {
        int times = retry.value();
        if (times <= 0) {
            times = defaultRetryTimes;
        }

        for (int i = 0; i < times; i++) {
            try {
                return pjp.proceed();
            } catch (Throwable e) {
                if (asList(retry.continueOn()).contains(e.getClass())) {
                    continue;
                }

                if (asList(retry.breakOn()).contains(e.getClass())
                        || (retry.continueOn().length > 0)
                        || i >= times - 1) {
                    throw e;
                }
            }
        }

        throw new RuntimeException("com.littlesorry.rpc.util.retry.RetryAspect:unreachable code");
    }

    public RetryAspect setDefaultRetryTimes(Integer defaultRetryTimes) {
        this.defaultRetryTimes = defaultRetryTimes;
        return this;
    }
}
