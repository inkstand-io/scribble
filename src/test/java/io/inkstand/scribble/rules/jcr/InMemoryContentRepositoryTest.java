package io.inkstand.scribble.rules.jcr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import javax.jcr.Repository;

import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.apache.jackrabbit.core.config.VersioningConfig;
import org.apache.jackrabbit.core.config.WorkspaceConfig;
import org.apache.jackrabbit.core.fs.mem.MemoryFileSystem;
import org.apache.jackrabbit.core.persistence.mem.InMemBundlePersistenceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryContentRepositoryTest {
    @Rule
    public final TemporaryFolder workingDirectory = new TemporaryFolder();

    private InMemoryContentRepository subject;

    @Before
    public void setUp() throws Exception {
        subject = new InMemoryContentRepository(workingDirectory);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDestroyRepository() throws Throwable {
        // prepare
        subject.before();

        // act
        subject.destroyRepository();

        // assert
        // there is no assertion to be made, just ensure, no exception occurs on destroy
    }

    @Test
    public void testGetConfigUrl() throws Exception {
        final URL configUrl = subject.getConfigUrl();

        // assert
        assertNotNull(configUrl);
    }

    /**
     * Verifies the persistence parts of the configuration use in-memory components.
     *
     * @throws Throwable
     */
    @Test
    public void testInMemoryConfiguraton() throws Throwable {
        subject.before();

        final RepositoryConfig config = subject.createRepositoryConfiguration();

        assertTrue(config.getFileSystem() instanceof MemoryFileSystem);

        final VersioningConfig vconfig = config.getVersioningConfig();
        assertTrue(vconfig.getFileSystem() instanceof MemoryFileSystem);
        assertEquals(InMemBundlePersistenceManager.class.getName(), vconfig.getPersistenceManagerConfig()
                .getClassName());

        final WorkspaceConfig wsconfig = config.getWorkspaceConfig("default");
        assertTrue(wsconfig.getFileSystem() instanceof MemoryFileSystem);
        assertEquals(InMemBundlePersistenceManager.class.getName(), wsconfig.getPersistenceManagerConfig()
                .getClassName());

    }

    @Test
    public void testCreateRepository() throws Throwable {

        // prepare
        subject.before();
        // act
        final Repository repository = subject.createRepository();

        // assert
        assertNotNull(repository);

    }

}
