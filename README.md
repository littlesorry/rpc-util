# rpc-util

Some method invoking util method, especially for rpc call.

## Retry

Annotation based java method retry util, especially for rpc call.

Sample:

```java
// fully config example
@Retry(
    value = 3 // method level max retry time config
    , breakOn = {NullPointException.class} // exception to exit invoking
    , continueOn = {InvalidClassException.class} // exception to continue retry
)
public void call() {

}

// simple one (which will use source level max retry time with no @code{breakOn} and no @code{continueOn}):
@Retry
public void call2() {

}
```

Enabled apect proxy by:

```xml
<aop:aspectj-autoproxy/>

<bean id="retryAspect" class="com.littlesorry.rpc.util.retry.RetryAspect">
  <property name="defaultRetryTimes" value="3" /> <!-- source level max retry time config -->
</bean>
```

