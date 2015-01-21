package io.inkstand.scribble.rules.jcr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import javax.jcr.Repository;

import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurableContentRepositoryTest {

    @Mock
    private Repository repository;
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private ConfigurableContentRepository subject;

    private final URL configUrl = getClass().getResource("ConfigurableContentRepositoryTest_repository.xml");

    @Before
    public void setUp() throws Exception {
        subject = new ConfigurableContentRepository(folder) {

            @Override
            protected void destroyRepository() {
            }

            @Override
            protected Repository createRepository() throws Exception {
                return repository;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateRepositoryConfiguration() throws Throwable {
        // prepare
        subject.setConfigUrl(configUrl);
        subject.before();

        // act
        final RepositoryConfig config = subject.createRepositoryConfiguration();

        // assert
        assertNotNull(config);
        assertEquals(folder.getRoot().getAbsolutePath(), config.getHomeDir());
    }

    @Test
    public void testSetGetConfigUrl() throws Exception {
        subject.setConfigUrl(configUrl);
        assertEquals(configUrl, subject.getConfigUrl());
    }

}
