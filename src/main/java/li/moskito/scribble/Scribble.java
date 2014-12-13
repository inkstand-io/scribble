package li.moskito.scribble;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.deltaspike.core.api.config.ConfigProperty;

/**
 * Test utility for inkstand.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public final class Scribble {

    private Scribble() {
    }

    /**
     * Defines which value should be injected into the injectionTarget.
     *
     * @author Gerald Muecke, gerald@moskito.li
     */
    public static class InjectionValueDefinition {

        private final Field field;
        private final Object target;
        private final Object defaultValue;

        private InjectionValueDefinition(final Field field, final Object target, final Object defaultValue) {
            this.field = field;
            this.target = target;
            this.defaultValue = defaultValue;
        }

        /**
         * Injects the specified value into the injection target
         *
         * @param value
         *            the value to be injected
         * @throws AssertionError
         *             if the value could not be injected.
         */
        public void value(final Object value) {
            field.setAccessible(true);
            try {
                field.set(target, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new AssertionError("Could not inject value '" + value + "' into " + field.getName(), e);
            }
        }

        /**
         * Injects the default value into the injection target.
         */
        public void defaultValue() {
            value(defaultValue);
        }

    }

    /**
     * Defines a target for an injection operation to prepare a test.
     *
     * @author Gerald Muecke, gerald@moskito.li
     */
    public static class InjectionTarget {

        private final Object target;

        private InjectionTarget(final Object target) {
            this.target = target;
        }

        /**
         * Injects a value into a deltaspike {@link ConfigProperty} with the given configuration name. The method is
         * intended for simple JUnit tests without a CDI container.
         *
         * @param configPropertyName
         *            the name of the ConfigProperty
         * @param value
         *            the value to be injected
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         */
        public InjectionValueDefinition configProperty(final String configPropertyName) {

            for (final Field field : target.getClass().getDeclaredFields()) {
                final ConfigProperty cp = field.getAnnotation(ConfigProperty.class);
                if (cp != null && configPropertyName.equals(cp.name())) {
                    return new InjectionValueDefinition(field, target, cp.defaultValue());
                }
            }
            throw new AssertionError("No ConfigProperty with name " + configPropertyName + " found on " + target);
        }

    }

    /**
     * Creates an {@link InjectionTarget} for the Object
     *
     * @param target
     *            the target object into which an object should be injected
     * @return an {@link InjectionTarget} that allows the definition of the what should be injected.
     */
    public static InjectionTarget injectInto(final Object target) {
        return new InjectionTarget(target);
    }

    /**
     * Generates a random run id.
     *
     * @return
     */
    public static String generateRunId() {
        final String ts = new SimpleDateFormat("YYYYMMdd-mm-ss").format(new Date());
        final String runID = ts + "_" + Math.abs(new Random(System.currentTimeMillis()).nextInt()) + "";
        return runID;
    }
}
