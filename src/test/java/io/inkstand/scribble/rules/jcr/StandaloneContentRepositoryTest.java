package io.inkstand.scribble.rules.jcr;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import io.inkstand.scribble.rules.BaseRuleHelper;

import java.net.URL;

import javax.jcr.Repository;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class StandaloneContentRepositoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final URL configUrl = getClass().getResource("StandaloneContentRepositoryTest_repository.xml");
    private StandaloneContentRepository subject;

    private RepositoryImpl repositorySpy;

    @Before
    public void setUp() throws Exception {
        subject = new StandaloneContentRepository(folder) {
            @Override
            public Repository getRepository() {
                repositorySpy = spy((RepositoryImpl) super.getRepository());
                return repositorySpy;
            }
        };
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
        BaseRuleHelper.setInitialized(subject);
        // act
        subject.destroyRepository();
        // assert
        assertNotNull(repositorySpy);
        verify(repositorySpy).shutdown();

    }

}
