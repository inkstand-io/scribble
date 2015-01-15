package li.moskito.scribble.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Alternative for {@link org.junit.rules.ExternalResource} that supports class-level before and after statements and
 * {@link TestRule} chaining.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class ExternalResource extends BaseRule {

    public ExternalResource() {
        super();
    }

    public ExternalResource(final TestRule outerRule) {
        super(outerRule);
    }

    /**
     * Verifies if the caller is a Suite and triggers the beforeClass and afterClass behavior.
     */
    @Override
    public Statement apply(final Statement base, final Description description) {
        if (description.isSuite()) {
            return classStatement(super.apply(base, description));
        }
        return statement(super.apply(base, description));
    }

/**
     * Creates a statement that will execute {@code before) and {@code after)
     *
     * @param base
     *  the base statement to be executed
     * @return
     *  the statement for invoking before and after
     */
    private Statement statement(final Statement base) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    base.evaluate();
                } finally {
                    after();
                }
            }
        };
    }

/**
     * Creates a statement that will execute {@code beforeClass) and {@code afterClass)
     *
     * @param base
     *  the base statement to be executed
     * @return
     *  the statement for invoking beforeClass and afterClass
     */
    private Statement classStatement(final Statement base) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                beforeClass();
                try {
                    base.evaluate();
                } finally {
                    afterClass();
                }
            }
        };
    }

    /**
     * Override to set up your specific external resource for an entire suite.
     *
     * @throws Throwable
     *             if setup fails (which will disable {@code afterClass}
     */
    protected void beforeClass() throws Throwable {
        // do nothing
    }

    /**
     * Override to tear down your specific external resource after all tests of a suite have been executed.
     */
    protected void afterClass() {
        // do nothing
    }

    /**
     * Override to set up your specific external resource.
     *
     * @throws Throwable
     *             if setup fails (which will disable {@code after}
     */
    protected void before() throws Throwable {
        // do nothing
    }

    /**
     * Override to tear down your specific external resource.
     */
    protected void after() {
        // do nothing
    }
}
