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

package io.inkstand.scribble.rules;

/**
 * Helper class to deal with the BaseRule when writing tests for Rules that subclass it
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public final class BaseRuleHelper {

    private BaseRuleHelper() {
    }

    /**
     * Invokes the setInitialize method using reflection. It is required to do it this way as the method is protected
     * and inside another package and without invoking it, the subject would remain uninitialized
     *
     * @throws Throwable
     */
    public static <T extends BaseRule<?>> void setInitialized(final T subject) {
        subject.setInitialized();
    }

}
