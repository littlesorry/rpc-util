# rpc-util

Annotation based java method retry util, especially for rpc call.

Sample:

```java
@Retry(
    value = 3 // method level max retry time config
    , stopOn = {NullPointException.class} // exception to exit invoking
    , continueOn = {InvalidClassException.class} // exception to continue retry
)
public void call() {

}
```

Enabled apect proxy by:

```xml
<aop:aspectj-autoproxy/>

<bean id="retryAspect" class="com.littlesorry.rpc.util.retry.RetryAspect">
  <property name="defaultRetryTimes" value="3" /> <!-- source level max retry time config -->
</bean>

```

