package io.inkstand.scribble.rules.builder;

import io.inkstand.scribble.rules.BaseRule;

/**
 * Generic builder implementation that is configurable and dynamically instantiates the rule and nested rules.
 *
 * @author Gerald Muecke, gerald@moskito.li
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
