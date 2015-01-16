package io.inkstand.scribble.rules.builder;

import org.junit.rules.TestRule;

public abstract class Builder<T extends TestRule> {

    public abstract T build();

}