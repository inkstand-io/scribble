package io.inkstand.scribble.matchers;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeUnit;

/**
 * Interface to be implemented by Matchers that require a timeout. By implementing this method the matcher
 * becomes configurable to produce a result within a certain timeframe.
 *
 * Created by <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a> on 3/13/2015
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public interface TimeoutSupport {

    /**
     * Specifies the timeout for the matcher. If the the matcher does not produce a result within this specified
     * timeframe it should fail. If milliseconds are required, the implementation of this method could simply be
     * <pre><code>
     *     long timeoutInMs = timeUnit.toMillis(duration);
     * </code></pre>
     * @param duration
     *  the duration to wait until timing out
     * @param timeUnit
     *  the time unit for the duration
     * @param <T>
     *     the matcher itself
     * @return
     *  the matcher itself
     */
    <T extends Matcher<?>> T within(long duration, TimeUnit timeUnit);
}
