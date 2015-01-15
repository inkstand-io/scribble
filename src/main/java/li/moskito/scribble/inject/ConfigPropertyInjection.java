package li.moskito.scribble.inject;

import java.lang.reflect.Field;

import org.apache.deltaspike.core.api.config.ConfigProperty;

/**
 * Injection support for injecting DeltaSpike {@link ConfigProperty} annotated properties. The property can be of any
 * type and has a name. If the property annotation defines a default value, it is injected if the inject value is
 * <code>null</code>.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class ConfigPropertyInjection extends Injection {

    /**
     * The name of the {@link ConfigProperty} into which the value should be injected
     */
    private final String configPropertyName;

    /**
     * The default value if it is set on the matching annotation
     */
    private Object defaultValue;

    public ConfigPropertyInjection(final String configPropertyName, final Object injectedValue) {
        super(injectedValue);
        this.configPropertyName = configPropertyName;
    }

    @Override
    protected Object getValue() {
        Object value = super.getValue();
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * Verfifies the name of the {@link ConfigProperty}. If the field type matches and the {@link ConfigProperty}'s name
     * matches, the method returns <code>true</code>. If the {@link ConfigProperty} has a default value set, the value
     * will be used in case the injection value is <code>null</code>.
     */
    @Override
    protected boolean isMatching(final Field field) {
        final boolean matches = super.isMatching(field);
        final ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
        if (matches && configProperty != null && configPropertyName.equals(configProperty.name())) {

            defaultValue = configProperty.defaultValue();

            return true;
        }

        return false;
    }

}
