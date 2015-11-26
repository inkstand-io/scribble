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
     * <br>
     * If a thread context classloader is defined, it will be used for loading the class, otherwise the
     * default class loader is used.
     * @return
     *  the class of the calling method's class
     */
    public static Class<?> getCallerClass(){
        final StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        try {
            StackTraceElement caller = stElements[2];
            return loadClass(caller.getClassName());
        } catch (ClassNotFoundException e) {
            LOG.debug("Could not determine caller class", e);
            return null;

        }
    }

    /**
     * Loads the class specified by name using the Thread's context class loader - if defined - otherwise the
     * default classloader.
     * @param className
     *  the name of the class to load
     * @return
     *  the loaded class
     * @throws ClassNotFoundException
     *  if the the class could not be loaded
     */
    private static Class<?> loadClass(final String className) throws ClassNotFoundException {
        ClassLoader ctxCL = Thread.currentThread().getContextClassLoader();
        if(ctxCL == null) {
            return Class.forName(className);
        }
        return ctxCL.loadClass(className);
    }

}
