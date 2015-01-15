package li.moskito.scribble.rules;

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
        if (outerRule != null) {
            return outerRule.apply(base, description);
        }
        return base;
    }
}
