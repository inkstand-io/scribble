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

import org.junit.rules.TestRule;

/**
 * Interface to be used by Testing objects such as {@link TestRule}s to provide an object to be injected into a test
 * subject.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
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
