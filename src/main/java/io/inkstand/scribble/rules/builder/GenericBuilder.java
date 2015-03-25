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

package io.inkstand.scribble.rules.builder;

import io.inkstand.scribble.rules.BaseRule;

/**
 * Generic builder implementation that is configurable and dynamically instantiates the rule and nested rules.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public abstract class GenericBuilder<T extends BaseRule<?>> extends Builder<T> {

    public <R extends BaseRule<?>, B extends GenericBuilder<R>> B around(final Class<R> ruleType) {
        throw new UnsupportedOperationException("around is not yet supported");
    }

    @Override
    public T build() {
        throw new UnsupportedOperationException("build is not yet supported");
    }

}
