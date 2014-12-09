package li.moskito.scribble.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Extension for the {@link org.junit.rules.ExternalResource} that supports class-level before and after statements
 * 
 * @author Gerald Muecke, gerald@moskito.li
 */
public class ExternalResource extends org.junit.rules.ExternalResource {

    /**
     * Verifies if the caller is a Suite and triggers the beforeClass and afterClass behavior.
     */
    @Override
    public Statement apply(final Statement base, final Description description) {
        if (description.isSuite()) {
            return statement(base);
        }
        return super.apply(base, description);
    }

/**
     * Creates a statement that will execute {@code beforeClass) and {@code afterClass)
     * 
     * @param base
     *  the base statement to be executed
     * @return
     *  the statement for invoking beforeClass and afterClass
     */
    private Statement statement(final Statement base) {
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
     * @throws if
     *             setup fails (which will disable {@code afterClass}
     */
    protected void beforeClass() throws Throwable {
        // do nothing
    }

    /**
     * Override to tear down your specific external resource.
     */
    protected void afterClass() {
        // do nothing
    }
}
