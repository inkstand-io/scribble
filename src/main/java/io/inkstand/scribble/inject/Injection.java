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
 * limitations under the License
 */

/**
 *
 */
package io.inkstand.scribble.inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides means to inject a value into an arbitrary object's fields. The default
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class Injection {

    private final Object value;

    /**
     * Creates a new Injection helper for the target object and the object to be injected.
     *
     * @param injectedValue
     *            the object to be injected
     */
    public Injection(final Object injectedValue) {

        value = injectedValue;
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
     * Prepares an injection of a {@link ConfigProperty} annotated field
     *
     * @return a {@link ConfigPropertyInjection} handle
     */
    public ConfigPropertyInjection asConfigProperty(final String propertyName) {

        return new ConfigPropertyInjection(propertyName, this.getValue());
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
     * Injects the value of the injection operation into the first matching field of the target object.
     *
     * @param target
     *            the object into which the injection value is injected
     */
    public void into(final Object target) {

        injectInto(target, true);
    }

    /**
     * @return the injection value
     */
    protected Object getValue() {

        return value;
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

        return field.getType().isAssignableFrom(value.getClass());
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
                    field.set(target, value);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new AssertionError("Injection of " + value + " into " + target + " failed", e);
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

        final List<Field> fieldCandidates;
        if (value != null) {
            final Class<?> injectedClass = value.getClass();
            final Class<?> targetClass = target.getClass();
            fieldCandidates = collectFieldCandidates(injectedClass, targetClass);
        } else {
            fieldCandidates = Collections.emptyList();
        }

        return fieldCandidates;
    }

    /**
     * Collects all matching declared fields of the target class and returns the result as a list.
     *
     * @param injectedClass
     *            the class of the value that should be injected
     * @param targetClass
     *            the class of the target of the injection whose declared fields should be collected
     * @return a list of fields that are type-compatible with the injected class.
     */
    private List<Field> collectFieldCandidates(final Class<?> injectedClass, final Class<?> targetClass) {
        final List<Field> fieldCandidates = new ArrayList<>();
        Class<?> currentTargetClass = targetClass;
        while (currentTargetClass != Object.class) {

            fieldCandidates.addAll(collectionDeclaredFieldCandidates(injectedClass, currentTargetClass));
            currentTargetClass = currentTargetClass.getSuperclass();
        }
        return fieldCandidates;
    }

    /**
     * Collects all declared fields from the targetClass that are type-compatible with the injectedClass into the
     * fieldCandidates list.
     *
     * @param injectedClass
     *            the class of the injection value
     * @param targetClass
     *            the class or any of its superclasses of the injection target
     * @return list of declared {@link Field}s that are type-compatible with the injected class
     */
    private List<Field> collectionDeclaredFieldCandidates(final Class<?> injectedClass, final Class<?> targetClass) {
        final List<Field> fieldCandidates = new ArrayList<>();
        for (final Field field : targetClass.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(injectedClass)) {
                fieldCandidates.add(field);
            }
        }
        return fieldCandidates;
    }

}
