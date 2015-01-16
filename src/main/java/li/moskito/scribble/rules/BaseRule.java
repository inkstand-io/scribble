package li.moskito.scribble.rules;

import junit.framework.AssertionFailedError;

import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Base {@link TestRule} that allows to define an outer rule that is evaluated around this rule. This is an alternative
 * to {@link RuleChain} that is helpful if rules depend on each other.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public abstract class BaseRule implements TestRule {

    /**
     * Test Rule that should be evaluated around this {@link TestRule}
     */
    private final TestRule outerRule;
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
    public BaseRule(final TestRule outerRule) {
        this.outerRule = outerRule;
    }

    /**
     * Invokes the outer rule - if set - around the base {@link Statement}
     */
    @Override
    public Statement apply(final Statement base, final Description description) {
        initialized = true;
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
        if (initialized) {
            throw new AssertionFailedError("Rule is already initialized");
        }
    }

    /**
     * Invoke this method to verify, it is completely initialized. As soon as the rule is applied, it is set to
     * initialized and methods that are intended for being used by tests should be possible to be performed.
     */
    protected void assertInitialized() {
        if (!initialized) {
            throw new AssertionFailedError("Rule is not initialized");
        }
    }
}
