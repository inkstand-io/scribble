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
     * Target to wrap around an object. It provides various method for injecting certain values into the object.
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
        public void configProperty(final String configPropertyName, final Object value)
                throws IllegalArgumentException, IllegalAccessException {
            for (final Field field : target.getClass().getDeclaredFields()) {
                final ConfigProperty cp = field.getAnnotation(ConfigProperty.class);
                if (cp != null && configPropertyName.equals(cp.name())) {
                    field.setAccessible(true);
                    field.set(target, value);
                }
            }
        }

        /**
         * Injects the default a value into a deltaspike {@link ConfigProperty} with the given configuration name. The
         * method is intended for simple JUnit tests without a CDI container. The default value is defined in the
         * {@link ConfigProperty} itself.
         *
         * @param configPropertyName
         *            the name of the ConfigProperty
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         */
        public void defaultConfigProperty(final String configPropertyName) throws IllegalArgumentException,
        IllegalAccessException {
            for (final Field field : target.getClass().getDeclaredFields()) {
                final ConfigProperty cp = field.getAnnotation(ConfigProperty.class);
                if (cp != null && configPropertyName.equals(cp.name())) {
                    field.setAccessible(true);
                    field.set(target, cp.defaultValue());
                }
            }
        }
    }

    /**
     * Creates an {@link InjectionTarget} for the Object
     *
     * @param target
     * @return
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
