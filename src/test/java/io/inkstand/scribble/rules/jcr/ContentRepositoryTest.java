package io.inkstand.scribble.rules.jcr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.inkstand.scribble.rules.BaseRuleHelper;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContentRepositoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Mock
    private Repository repository;

    @Mock
    private Session session;

    private ContentRepository subject;

    @Before
    public void setUp() throws Exception {
        subject = new ContentRepository(folder) {

            @Override
            protected Repository createRepository() throws Exception {
                return repository;
            }

            @Override
            protected void destroyRepository() {

            }
        };
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBeforeClass() throws Throwable {
        // act
        subject.beforeClass();
        BaseRuleHelper.setInitialized(subject);
        // assert
        // now, the repositor is created
        assertNotNull(subject.getRepository());
    }

    @Test
    public void testAfterClass() throws Exception {
        // prepare
        final ContentRepository spy = spy(subject);
        // act
        spy.afterClass();
        // assert
        verify(spy).destroyRepository();
    }

    @Test
    public void testBefore() throws Throwable {
        // act
        subject.before();
        BaseRuleHelper.setInitialized(subject);

        // assert
        // now, the repositor is created
        assertNotNull(subject.getRepository());
    }

    @Test
    public void testAfter_beforeExecuted() throws Throwable {
        // prepare
        final ContentRepository spy = spy(subject);
        // before has to be executed before after can be executed
        spy.before();
        // act
        spy.after();
        // assert
        verify(spy).destroyRepository();
    }

    @Test
    public void testAfter_beforeNotExecuted() throws Throwable {
        // prepare
        final ContentRepository spy = spy(subject);
        // act
        spy.after();
        // assert
        verify(spy, times(0)).destroyRepository();
    }

    @Test(expected = AssertionError.class)
    public void testGetRepository_uninitialized_fail() throws Exception {
        subject.getRepository();
    }

    @Test
    public void testGetRepository_initialized_ok() throws Throwable {
        // prepare
        subject.before();
        BaseRuleHelper.setInitialized(subject);
        // act
        final Repository repo = subject.getRepository();
        // assert
        assertEquals(repository, repo);
    }

    @Test(expected = AssertionError.class)
    public void testGetInjectionValue_uninitialized_fail() throws Exception {
        subject.getInjectionValue();
    }

    @Test
    public void testGetInjectionValue_initialized_ok() throws Throwable {
        // prepare
        subject.before();
        BaseRuleHelper.setInitialized(subject);
        // act
        final Repository repo = subject.getInjectionValue();
        // assert
        assertEquals(repository, repo);
    }

    @Test
    public void testGetWorkingDirectory_initialized_ok() throws Throwable {
        // prepare
        BaseRuleHelper.setInitialized(subject);
        // act
        final TemporaryFolder workingDirectory = subject.getWorkingDirectory();
        // aasert
        assertEquals(folder, workingDirectory);
    }

    @Test(expected = AssertionError.class)
    public void testGetWorkingDirectory_notInitialized_fail() throws Throwable {
        subject.getWorkingDirectory();
    }

    @Test(expected = AssertionError.class)
    public void testLogin_notInitialized_fail() throws Throwable {
        subject.login("aUser", "aPassword");
    }

    @Test
    public void testLogin_initialized() throws Throwable {
        // prepare
        when(repository.login(any(SimpleCredentials.class))).thenReturn(session);
        subject.before();
        BaseRuleHelper.setInitialized(subject);
        // act
        final Session userSession = subject.login("aUser", "aPassword");
        // assert
        assertNotNull(userSession);
        assertEquals(session, userSession);

        final ArgumentCaptor<SimpleCredentials> captor = ArgumentCaptor.forClass(SimpleCredentials.class);
        verify(repository).login(captor.capture());
        final SimpleCredentials passedParam = captor.getValue();
        assertEquals("aUser", passedParam.getUserID());
        assertEquals("aPassword", String.valueOf(passedParam.getPassword()));
    }

}
