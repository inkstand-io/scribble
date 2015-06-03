package io.inkstand.scribble.rules.jcr;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.slf4j.LoggerFactory.getLogger;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import java.net.URL;

import io.inkstand.scribble.rules.BaseRule;
import io.inkstand.scribble.rules.BaseRuleHelper;
import junit.framework.TestCase;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.core.RepositoryImpl;
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
import org.slf4j.Logger;

/**
 * Created by gerald on 03.06.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class JackrabbitContentRepositoryTest extends TestCase {

    private static final Logger LOG = getLogger(JackrabbitContentRepositoryTest.class);

    private final URL configUrl = getClass().getResource("JackrabbitContentRepositoryTest_repository.xml");

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private RepositoryImpl repositorySpy;

    private JackrabbitContentRepository subject;

    @Mock
    private Description description;

    @Before
    public void setUp() throws Exception {
        subject = new JackrabbitContentRepository(folder) {
            @Override
            public Repository getRepository() {
                repositorySpy = spy((RepositoryImpl) super.getRepository());
                return repositorySpy;
            }
        };
        subject.setConfigUrl(configUrl);
    }

    @After
    public void tearDown() throws Exception {

        subject.after();
    }


    @Test
    public void testAddUser() throws Throwable {
        //prepare
        final String username = "testuser";
        final String password = "password";

        //act
        subject.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                User user = subject.addUser(username, password);
                LOG.info("User {} created", user);
                Session session = subject.getRepository().login(
                        new SimpleCredentials(username, password.toCharArray()));
                assertEquals(username, session.getUserID());

            }
        },description).evaluate();

        //assert

    }

    @Test
    public void testDeleteUser() throws Throwable {
        //prepare
        final String username = "testuser";
        final String password = "password";


        //act
        //act
        subject.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                User user = subject.addUser(username, password);
                assertNotNull(user);
                boolean result = subject.deleteUser(username);

                //assert
                assertTrue(result);

            }
        },description).evaluate();


    }

    @Test
    public void testCreateRepository_noConfigUrl_useDefaultConfig() throws Exception {
        // act
        final Repository repository = subject.createRepository();

        // assert
        assertNotNull(repository);
    }

    @Test
    public void testCreateRepository_withConfigUrl() throws Exception {
        // prepare
        subject.setConfigUrl(configUrl);

        // act
        final Repository repository = subject.createRepository();

        // assert
        assertNotNull(repository);

    }

    @Test
    public void testDestroyRepository() throws Throwable {
        // prepare
        subject.before();
        BaseRuleHelper.setState(subject, BaseRule.State.INITIALIZED);
        // act
        subject.destroyRepository();
        // assert
        assertNotNull(repositorySpy);
        verify(repositorySpy).shutdown();
    }
}