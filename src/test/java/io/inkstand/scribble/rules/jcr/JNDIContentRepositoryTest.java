package io.inkstand.scribble.rules.jcr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import javax.jcr.Repository;
import javax.naming.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JNDIContentRepositoryTest {

    @Mock
    private Context context;
    @Mock
    private Repository repository;

    private JNDIContentRepository subject;

    @Before
    public void setUp() throws Exception {
        subject = new JNDIContentRepository();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = AssertionError.class)
    public void testCreateRepository_noContextNoLookup() throws Exception {
        subject.createRepository();
    }

    @Test
    public void testCreateRepository_withContextNoLookup_defaultLookup() throws Exception {
        // prepare
        subject.setContext(context);
        when(context.lookup("java:/jcr/local")).thenReturn(repository);
        // act
        final Repository jndiRepo = subject.createRepository();

        // assert
        assertNotNull(jndiRepo);
        assertEquals(repository, jndiRepo);
    }

    @Test(expected = AssertionError.class)
    public void testCreateRepository_noContextWithLookup() throws Exception {
        subject.setLookupName("java:/jcr/local");
        subject.createRepository();
    }

    @Test
    public void testCreateRepository_withContextAndWithLookup() throws Exception {
        // prepare
        subject.setContext(context);
        subject.setLookupName("java:/custom/lookup/name");
        when(context.lookup("java:/custom/lookup/name")).thenReturn(repository);
        // act
        final Repository jndiRepo = subject.createRepository();

        // assert
        assertNotNull(jndiRepo);
        assertEquals(repository, jndiRepo);
    }

}
