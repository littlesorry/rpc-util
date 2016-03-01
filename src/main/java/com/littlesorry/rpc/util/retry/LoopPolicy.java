package com.littlesorry.rpc.util.retry;

/**
 * Created by takara on 3/1/16.
 *
 * @littlesorry@github
 */
public interface LoopPolicy {

    boolean shouldContinue(Throwable t, int current, int maxRetry);

    boolean shouldBreak(Throwable t, int current, int maxRetry);

    class NONE_POLICY implements LoopPolicy {

        @Override
        public boolean shouldContinue(Throwable t, int current, int maxRetry) {
            return false;
        }

        @Override
        public boolean shouldBreak(Throwable t, int current, int maxRetry) {
            return false;
        }
    }

}
