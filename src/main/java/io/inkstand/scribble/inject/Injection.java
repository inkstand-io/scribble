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

/**
 *
 */
package io.inkstand.scribble.inject;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.deltaspike.core.api.config.ConfigProperty;

/**
 *
 * Provides means to inject a value into an arbitrary object's fields.
 * <br/>
 * Example:
 * <pre><code>
 *     class Target {
 *         {@literal @}Resource(name=&quot;resourceName&quot;)
 *         private Object myResource
 *         //...
 *     };
 *     //...
 *     Target target = ...;
 *     Object anyValue = ...;
 *     new Injection(anyValue).asResource().byName("resourceName").into(target);
 * </code></pre>
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public class Injection {

    /**
     * Map of primitive wrapper types to the corresponding primitive type
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAP;

    static {
        final Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(Boolean.class, boolean.class);
        map.put(Byte.class, byte.class);
        map.put(Short.class, short.class);
        map.put(Integer.class, int.class);
        map.put(Long.class, long.class);
        map.put(Float.class, float.class);
        map.put(Double.class, double.class);
        map.put(Character.class, char.class);
        PRIMITIVE_TYPE_MAP = Collections.unmodifiableMap(map);
    }

    private final Object value;

    /**
     * Creates a new Injection helper for the target object and the object to be injected.
     *
     * @param injectedValue
     *            the object to be injected
     */
    public Injection(final Object injectedValue) {

        if(injectedValue instanceof InjectableHolder){
            value  = ((InjectableHolder) injectedValue).getInjectionValue();
        } else {
            value = injectedValue;
        }
    }

    /**
     * Prepares an injection of a {@link Resource} annotated field
     *
     * @return a {@link ResourceInjection} handle
     */
    public ResourceInjection asResource() {

        return new ResourceInjection(this.getValue());
    }

    /**
     * @return the injection value
     */
    protected Object getValue() {

        return value;
    }

    /**
     * Prepares an injection of a {@link ConfigProperty} qualified injection point field.
     *
     * @return a {@link ConfigPropertyInjection} handle
     */
    public ConfigPropertyInjection asConfigProperty(final String propertyName) {

        return new ConfigPropertyInjection(propertyName, this.getValue());
    }

    /**
     * Prepares an injection of {@link javax.inject.Inject} annotated field. The default injection only checks
     * for type compatibility, using this method mandates the presence of the {@code @Inject} annotation.
     @return a {@link CdiInjection} handle
     */
    public CdiInjection asQualifyingInstance(){

        return new CdiInjection(this.getValue());
    }

    /**
     * Injects the value of the injection operation into all matching fields of the target object.
     *
     * @param target
     *            the object into which the injection value is injected
     */
    public void intoAll(final Object target) {

        injectInto(target, false);
    }

    /**
     * Injects the value of the Injection into the target.
     *
     * @param target
     *            the target for the injection operation
     * @param returnOnFirstMatch
     *            if set to true, the method returns after one injection operation has been performed. If set to
     *            <code>false</code> the injection value is injected into all matching fields.
     * @throws AssertionError
     */
    private void injectInto(final Object target, final boolean returnOnFirstMatch) throws AssertionError {

        for (final Field field : collectFieldCandidates(target)) {
            if (isMatching(field)) {
                field.setAccessible(true);
                try {
                    field.set(target, getValue());
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new AssertionError("Injection of " + getValue() + " into " + target + " failed", e);
                }
                if (returnOnFirstMatch) {
                    return;
                }
            }
        }
    }

    /**
     * Determines all fields that are candidates for the injection as their type is compatible with the injected object.
     * The method checks the target objects type and supertypes for declared fields.
     *
     * @param target
     *            the target object for which the candidate fields should be determined
     * @return an array of all fields into which the injection object can be injected.
     */
    private List<Field> collectFieldCandidates(final Object target) {

        final Class<?> targetClass = target.getClass();
        return collectFieldCandidates(value, targetClass);
    }

    /**
     * Determines if the field matches the value considering all specified match criteria. The default implementation
     * only checks for the type compatibility, override for more detailed match verification.
     *
     * @param field
     *            the field to check
     * @return <code>true</code> if the field matches
     */
    protected boolean isMatching(final Field field) {

        if (value == null) {
            // null is always type-compatible
            return true;
        }

        final Class<?> fieldType = field.getType();
        final Class<?> valueType = value.getClass();

        if (fieldType.isPrimitive()) {
            return fieldType == primitiveTypeFor(valueType);
        }
        return fieldType.isAssignableFrom(valueType);
    }

    /**
     * Collects all matching declared fields of the target class and returns the result as a list.
     *
     * @param injectedValue
     *            the value that should be injected
     * @param targetClass
     *            the class of the target of the injection whose declared fields should be collected
     * @return a list of fields that are type-compatible with the injected class.
     */
    private List<Field> collectFieldCandidates(final Object injectedValue, final Class<?> targetClass) {

        final List<Field> fieldCandidates = new ArrayList<>();
        Class<?> currentTargetClass = targetClass;
        while (currentTargetClass != Object.class) {

            fieldCandidates.addAll(collectionDeclaredFieldCandidates(injectedValue, currentTargetClass));
            currentTargetClass = currentTargetClass.getSuperclass();
        }
        return fieldCandidates;
    }

    /**
     * Returns the primitive type class for the given value type
     *
     * @param valueType
     *         a primitive wrapper type
     *
     * @return the corresponding primitive type
     */
    private Class primitiveTypeFor(final Class<?> valueType) {

        return PRIMITIVE_TYPE_MAP.get(valueType);

    }

    /**
     * Collects all declared fields from the targetClass that are type-compatible with the class of the
     * injected value into the fieldCandidates list.
     *
     * @param injectedValue
     *            the value that should be injected
     * @param targetClass
     *            the class or any of its superclasses of the injection target
     * @return list of declared {@link Field}s that are type-compatible with the injected class
     */
    private List<Field> collectionDeclaredFieldCandidates(final Object injectedValue, final Class<?> targetClass) {
        final List<Field> fieldCandidates = new ArrayList<>();
        for (final Field field : targetClass.getDeclaredFields()) {
            if (isFieldCandidate(field, injectedValue)) {
                fieldCandidates.add(field);
            }
        }
        return fieldCandidates;
    }

    /**
     * Checks if a field is an injection candidate for the given injected value
     * @param field
     *  the field that is a potential candidate
     * @param injectedValue
     *  the value that should be injected. May be null.
     * @return
     *  <code>true</code> if the field is a candidate. This does not mean, that the injection will actually
     *  be performed into that field.
     */
    protected boolean isFieldCandidate(final Field field, final Object injectedValue) {

        return injectedValue != null && field.getType().isAssignableFrom(injectedValue.getClass());
    }

    /**
     * Injects the value of the injection operation into the first matching field of the target object.
     *
     * @param target
     *            the object into which the injection value is injected
     */
    public void into(final Object target) {

        assertNotNull("Injection target must not be null", target);
        injectInto(target, true);
    }

}
