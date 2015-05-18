package io.inkstand.scribble.inject;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * Injection support for injecting {@link javax.inject.Inject} annotated fields. The field can be of any
 * type.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald Muecke</a> (18.05.2015)
 *
 */
public class CdiInjection extends Injection{

    /**
     * Creates a new Injection helper for the target object and the object to be injected.
     *
     * @param injectedValue
     *         the object to be injected
     */
    public CdiInjection(final Object injectedValue) {

        super(injectedValue);
    }

    @Override
    protected boolean isMatching(final Field field) {

        final boolean matches = super.isMatching(field);
        final boolean isInject = field.getAnnotation(Inject.class) != null;
        if (!matches || !isInject) {
            return false;
        }
        return true;
    }
}
