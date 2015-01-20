package io.inkstand.scribble.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.net.URL;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TemporaryFileTest {

    private static final String TEST_FILE_NAME = "testfile.txt";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private TemporaryFile subject;
    @Mock
    private Description description;

    private URL getTestContentUrl() {
        return getClass().getResource("TemporaryFileTest_testContent.txt");
    }

    private URL getEmptyTestContentUrl() {
        return getClass().getResource("TemporaryFileTest_emptyContent.txt");
    }

    @Before
    public void setUp() throws Exception {
        subject = new TemporaryFile(folder, TEST_FILE_NAME);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBefore_noContent() throws Throwable {
        // act
        subject.before();

        // assert
        assertNotNull(subject.getFile());
        assertTrue(subject.getFile().exists());
        assertEquals(0, subject.getFile().length());
    }

    @Test(expected = AssertionFailedError.class)
    public void testBefore_noContentForceContent() throws Throwable {
        // prepare
        subject.setForceContent(true);

        // act
        subject.before();
    }

    @Test
    public void testBefore_withContentNoForce() throws Throwable {
        // prepare
        subject.setContentUrl(getTestContentUrl());
        // act
        subject.before();
        // assert
        assertNotNull(subject.getFile());
        assertTrue(subject.getFile().exists());
        assertEquals(12, subject.getFile().length());
    }

    @Test
    public void testBefore_withContentWithForce() throws Throwable {
        // prepare
        subject.setForceContent(true);
        subject.setContentUrl(getTestContentUrl());
        // act
        subject.before();
        // assert
        assertNotNull(subject.getFile());
        assertTrue(subject.getFile().exists());
        assertEquals(12, subject.getFile().length());
    }

    @Test
    public void testBefore_withEmptyContentWithForce() throws Throwable {
        // prepare
        subject.setForceContent(true);
        subject.setContentUrl(getEmptyTestContentUrl());
        // act
        subject.before();
        // assert
        assertNotNull(subject.getFile());
        assertTrue(subject.getFile().exists());
        assertEquals(0, subject.getFile().length());
    }

    @Test
    public void testAfter() throws Throwable {
        // prepare
        subject.before();

        // act
        subject.after();

        // assert
        assertFalse(subject.getFile().exists());
    }

    public void testApply() throws Throwable {

        // prepare
        final Statement base = spy(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                if (!subject.getFile().exists()) {
                    throw new AssertionFailedError("Test File was not created");
                }

            }

        });

        // act
        subject.apply(base, description);

        // assert
        verify(base).evaluate();

    }
}
