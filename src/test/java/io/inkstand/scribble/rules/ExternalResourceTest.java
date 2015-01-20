package io.inkstand.scribble.rules;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExternalResourceTest {

    @Mock
    private TestRule outer;
    @Mock
    private Statement outerStatement;
    @Mock
    private Statement base;
    @Mock
    private Description description;

    private ExternalResource subject;

    @Before
    public void setUp() throws Exception {
        subject = new ExternalResource() {
        };
        when(outer.apply(base, description)).thenReturn(outerStatement);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testApply_instanceRule() throws Throwable {
        // prepare
        final ExternalResource spy = spy(subject);

        // act
        final Statement stmt = spy.apply(base, description);
        stmt.evaluate();

        // assert
        verify(spy).before();
        verify(spy).after();
        verify(base).evaluate();
        verify(spy, times(0)).beforeClass();
        verify(spy, times(0)).afterClass();
    }

    @Test
    public void testApply_classRule() throws Throwable {
        // prepare
        final ExternalResource spy = spy(subject);
        when(description.isSuite()).thenReturn(true);

        // act
        final Statement stmt = spy.apply(base, description);
        stmt.evaluate();

        // assert
        verify(spy, times(0)).before();
        verify(spy, times(0)).after();
        verify(base).evaluate();
        verify(spy).beforeClass();
        verify(spy).afterClass();
    }

    @Test
    public void testExternalResourceTestRule_classeRule() throws Throwable {
        // prepare
        final ExternalResource spy = spy(new ExternalResource(outer) {
        });

        when(description.isSuite()).thenReturn(true);

        // act
        final Statement stmt = spy.apply(base, description);
        stmt.evaluate();

        // assert
        verify(outer).apply(base, description);
        verify(outerStatement).evaluate();
    }

    @Test
    public void testExternalResourceTestRule_instanceRule() throws Throwable {
        // prepare
        final ExternalResource spy = spy(new ExternalResource(outer) {
        });

        // act
        final Statement stmt = spy.apply(base, description);
        stmt.evaluate();

        // assert
        verify(outer).apply(base, description);
        verify(outerStatement).evaluate();
    }

}
