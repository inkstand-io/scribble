package io.inkstand.scribble.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Gerald Muecke on 18.11.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class SystemPropertiesTest  {

    public static final String A_TEST_PROPERTY = "a_test_property";
    /**
     * The class under test
     */
    @InjectMocks
    private SystemProperties subject;

    @Mock
    private Description description;

    @Test
    public void testApply() throws Throwable {

        //prepare
        assumeTrue(System.getProperty(A_TEST_PROPERTY) == null);
        Statement stmt = new Statement() {

            @Override
            public void evaluate() throws Throwable {
                System.setProperty(A_TEST_PROPERTY, "someValue");
                assertEquals("someValue", System.getProperty(A_TEST_PROPERTY) );
            }
        };

        //act
        subject.apply(stmt, description).evaluate();

        //assert
        assertNull(System.getProperty(A_TEST_PROPERTY));
    }
}
