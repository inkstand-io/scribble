package io.inkstand.scribble.rules;

/**
 * Helper class to deal with the BaseRule when writing tests for Rules that subclass it
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public final class BaseRuleHelper {

    private BaseRuleHelper() {
    }

    /**
     * Invokes the setInitialize method using reflection. It is required to do it this way as the method is protected
     * and inside another package and without invoking it, the subject would remain uninitialized
     *
     * @throws Throwable
     */
    public static <T extends BaseRule<?>> void setInitialized(final T subject) {
        subject.setInitialized();
    }

}
