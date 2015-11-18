package io.inkstand.scribble.rules;

import java.util.Properties;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A TestRule that restores the System Properties after test execution to the state as they have been before.
 * This is useful for tests, that modifiy the System's Properties either for setup or as a result of the test
 * execution.
 * Created by Gerald Muecke on 18.11.2015.
 */
public class SystemProperties implements TestRule {

    @Override
    public Statement apply(final Statement statement, final Description description) {

        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                final Properties sysprops = (Properties) System.getProperties().clone();
                try {
                    statement.evaluate();
                } finally {
                    System.setProperties(sysprops);
                }

            }
        };
    }
}
