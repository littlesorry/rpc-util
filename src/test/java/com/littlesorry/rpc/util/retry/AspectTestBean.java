package com.littlesorry.rpc.util.retry;

/**
 * Created by takara on 2/29/16.
 */
public class AspectTestBean {

    private IntGenerator intGenerator;

    @Retry
    public Integer test() throws Exception {
        return intGenerator.next();
    }

    @Retry(
            value = 2
            , breakOn = {IllegalArgumentException.class, ClassNotFoundException.class}
    )
    public void test2() throws Exception {
        intGenerator.next();
    }

    public AspectTestBean setIntGenerator(IntGenerator intGenerator) {
        this.intGenerator = intGenerator;
        return this;
    }
}
