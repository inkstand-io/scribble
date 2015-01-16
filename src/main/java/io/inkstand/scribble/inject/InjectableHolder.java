/**
 *
 */
package io.inkstand.scribble.inject;

import org.junit.rules.TestRule;

/**
 * Interface to be used by Testing objects such as {@link TestRule}s to provide an object to be injected into a test
 * subject.
 *
 * @author Gerald Muecke, gerald@moskito.li
 * @param <T>
 *            the type of the injected value
 */
public interface InjectableHolder<T> {

    /**
     * The value that should be be injected into a injection target.
     *
     * @return the target object to be injected
     */
    public T getInjectionValue();
}
