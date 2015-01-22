package io.inkstand.scribble.rules.jcr;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.jcr.Repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

public class MockContentRepositoryTest {

    private MockContentRepository subject;

    @Before
    public void setUp() throws Exception {
        subject = new MockContentRepository();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateRepository() throws Exception {
        final Repository repository = subject.createRepository();
        assertNotNull(repository);
        assertTrue(new MockUtil().isMock(repository));
    }

    @Test
    public void testMockContentRepository_noWorkingDirectory() throws Throwable {
        // prepare
        subject.before();
        // act
        assertNull(subject.getWorkingDirectory());
    }

}
