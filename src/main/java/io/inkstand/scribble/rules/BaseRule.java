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

import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Base {@link TestRule} that allows to define an outer rule that is evaluated around this rule. This is an alternative
 * to {@link RuleChain} that is helpful if rules depend on each other.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public abstract class BaseRule<T extends TestRule> implements TestRule {

    /**
     * Test Rule that should be evaluated around this {@link TestRule}
     */
    private final T outerRule;
    private boolean initialized;

    /**
     * Creates a rule without an outer rule
     */
    public BaseRule() {
        outerRule = null;
    }

    /**
     * Creates a rule with a that has a rule around it.
     *
     * @param outerRule
     */
    public BaseRule(final T outerRule) {
        this.outerRule = outerRule;
    }

    /**
     * Invokes the outer rule - if set - around the base {@link Statement}
     */
    @Override
    public Statement apply(final Statement base, final Description description) {
        if (outerRule != null) {
            return outerRule.apply(base, description);
        }
        return base;
    }

    /**
     * Invoke this method to verify, it is not completely initialized. As soon as the rule is applied, it is set to
     * initialized and no further configurations should be done any more. This method may be invoked by method that are
     * also annotated with {@link RuleSetup}.
     */
    protected void assertNotInitialized() {
        if (isInitialized()) {
            throw new AssertionError("Rule is already initialized");
        }
    }

    /**
     * Returns whether the rule is initialized. The base rule itself is initialized as soon as it is applied. If an
     * implementing class determines the initialization state on other condition, this method should be overriden so
     * that the both initialization assertion methods can be used properly
     *
     * @return <code>true</code> when the rule is initialized, <code>false</code> if not
     */
    protected boolean isInitialized() {
        return initialized;
    }

    /**
     * Invoke this method to indicate, the initialization is complete. The method has to be invoked by the implementing
     * class as the {@link BaseRule} itself won't invoked it.
     */
    protected void setInitialized() {
        initialized = true;
    }

    /**
     * Invoke this method to verify, it is completely initialized. As soon as the rule is applied, it is set to
     * initialized and methods that are intended for being used by tests should be possible to be performed.
     */
    protected void assertInitialized() {
        if (!isInitialized()) {
            throw new AssertionError("Rule is not initialized");
        }
    }

    /**
     * Returns the outer rule for this base rule. The outer rule is applied around the implementing rule
     * 
     * @return the outer rule or <code>null</code> if the rule has no outer rule
     */
    protected T getOuterRule() {
        return this.outerRule;
    }
}
