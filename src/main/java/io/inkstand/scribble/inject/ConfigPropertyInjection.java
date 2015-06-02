/*
 * Copyright 2015 Gerald Muecke, gerald.muecke@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.inkstand.scribble.inject;

import java.lang.reflect.Field;
import org.apache.deltaspike.core.api.config.ConfigProperty;

/**
 * Injection support for injecting DeltaSpike {@link ConfigProperty} annotated properties. The property can be of any
 * type and has a name. If the property annotation defines a default value, it is injected if the inject value is
 * <code>null</code>.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald Muecke</a>
 */
public class ConfigPropertyInjection extends CdiInjection {

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

    @Override
    protected boolean isFieldCandidate(final Field field, final Object injectedValue) {

        //@formatter:off
        return field.getAnnotation(ConfigProperty.class) != null
                && (injectedValue == null
                || super.isFieldCandidate(field, injectedValue));
        //@formatter:on
    }

    /**
     * Verifies the name of the {@link ConfigProperty}. If the field type matches and the {@link ConfigProperty}'s name
     * matches, the method returns <code>true</code>. If the {@link ConfigProperty} has a default value set, the value
     * will be used in case the injection value is <code>null</code>.
     */
    @Override
    protected boolean isMatching(final Field field) {

        if (!super.isMatching(field)) {
            return false;
        }
        final ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
        if (configProperty != null && configPropertyName.equals(configProperty.name())) {
            defaultValue = configProperty.defaultValue();
            return true;
        }
        return false;
    }

}
