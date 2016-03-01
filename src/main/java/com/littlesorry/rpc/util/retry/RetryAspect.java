package com.littlesorry.rpc.util.retry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by takara on 2/29/16.
 */
@Aspect
public class RetryAspect {

    private Integer defaultRetryTimes = 3;

    private final Map<Class<? extends LoopPolicy>, LoopPolicy> policies = new HashMap<>();

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
                if (retry.loopPolicy() != LoopPolicy.NONE_POLICY.class) {
                    LoopPolicy policy = getPolicy(retry);
                    if (policy.shouldContinue(e, i, times)) {
                        continue;
                    }

                    if (policy.shouldBreak(e, i, times)) {
                        break;
                    }
                } else {
                    // normal flow
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
        }

        throw new RuntimeException("com.littlesorry.rpc.util.retry.RetryAspect:unreachable code");
    }

    private LoopPolicy getPolicy(Retry retry) {
        policies.computeIfAbsent(retry.loopPolicy(), aClass -> {
            LoopPolicy policy = null;
            try {
                policy = aClass.newInstance();
            } catch (Throwable e) {
            }
            return policy;
        });

        return policies.get(retry.loopPolicy());
    }

    public RetryAspect setDefaultRetryTimes(Integer defaultRetryTimes) {
        this.defaultRetryTimes = defaultRetryTimes;
        return this;
    }
}
