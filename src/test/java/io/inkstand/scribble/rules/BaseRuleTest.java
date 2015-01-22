package io.inkstand.scribble.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuleTest {

    @Mock
    private TestRule outer;
    @Mock
    private Statement base;
    @Mock
    private Statement statement;
    @Mock
    private Description description;

    private BaseRule subject;

    @Before
    public void setUp() throws Exception {
        subject = new BaseRule() {
        };
    }

    @Test
    public void testBaseRule_withOuterTestRule() throws Exception {

        // prepare
        when(outer.apply(base, description)).thenReturn(statement);
        // act
        final BaseRule subject = new BaseRule(outer) {
        };
        final Statement stmt = subject.apply(base, description);

        // assert
        verify(outer).apply(base, description);
        assertNotNull(stmt);
        assertEquals(statement, stmt);
    }

    @Test
    public void testApply() throws Exception {
        final Statement stmt = subject.apply(base, description);
        assertNotNull(stmt);
        assertEquals(base, stmt);
    }

    @Test
    public void testAssertNotInitialized_notInitialized_ok() throws Exception {
        subject.assertNotInitialized();
    }

    @Test(expected = AssertionError.class)
    public void testAssertNotInitialized_initialized_fail() throws Exception {
        subject.apply(base, description);
        subject.assertNotInitialized();
    }

    @Test(expected = AssertionError.class)
    public void testAssertInitialized_notInitialized_fail() throws Exception {
        subject.assertInitialized();
    }

    @Test
    public void testAssertInitialized_initialized_ok() throws Exception {
        subject.apply(base, description);
        subject.assertInitialized();
    }

}
