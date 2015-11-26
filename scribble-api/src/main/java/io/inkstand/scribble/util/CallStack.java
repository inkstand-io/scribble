package io.inkstand.scribble.util;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

/**
 * Helper class for operations on the call stack
 * Created by Gerald Muecke on 26.11.2015.
 */
public final class CallStack {

    private static final Logger LOG = getLogger(CallStack.class);

    private CallStack(){}

    /**
     * Returns the caller class of the calling method.<br>
     * For example: <br>
     * A.calling() -> B.called()
     * B.called() -> getCallerClass(): A
     *
     * @return
     *  the class of the calling method's class
     */
    public static Class<?> getCallerClass(){
        final StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        try {
            return Class.forName(stElements[2].getClassName());
        } catch (ClassNotFoundException e) {
            LOG.debug("Could not determine caller class", e);
            return null;

        }
    }

}
