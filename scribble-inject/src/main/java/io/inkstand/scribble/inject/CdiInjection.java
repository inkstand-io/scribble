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

import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Injection support for injecting {@link javax.inject.Inject} annotated fields. The field can be of any
 * type.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald Muecke</a> (18.05.2015)
 *
 */
public class CdiInjection extends Injection{

    private final List<Class<? extends Annotation>> qualifiers;

    /**
     * Creates a new Injection helper for the target object and the object to be injected.
     *
     * @param injectedValue
     *         the object to be injected
     * @param qualifiers
     */
    public CdiInjection(final Object injectedValue, final Class<? extends Annotation>... qualifiers) {

        super(injectedValue);
        this.qualifiers = Arrays.asList(qualifiers);
    }

    @Override
    protected boolean isMatching(final Field field) {

        final boolean matches = super.isMatching(field);
        final boolean isInject = field.getAnnotation(Inject.class) != null;
        if (!matches || !isInject) {
            return false;
        }
        return matchQualifiers(field);
    }

    private boolean matchQualifiers(final Field field) {
        if(qualifiers.isEmpty()){
            return true;
        }
        final Set<Class<? extends Annotation>> fieldQualifiers = collectQualifiers(field);
        for(Class<? extends Annotation> qualifierClass : this.qualifiers){
            if(!fieldQualifiers.contains(qualifierClass)){
                return false;
            }
        }
        return true;
    }

    private Set<Class<? extends Annotation>> collectQualifiers(final Field field) {

        final Set<Class<? extends Annotation>> qualifiers = new HashSet<>();
        for(Annotation an : field.getDeclaredAnnotations()){
            if(an.annotationType().getAnnotation(Qualifier.class) != null) {
                qualifiers.add(an.annotationType());
            }
        }
        return qualifiers;
    }
}
