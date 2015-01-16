package li.moskito.scribble.rules.jcr;

import java.io.IOException;
import java.net.URL;

import junit.framework.AssertionFailedError;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link InMemoryContentRepository} rule is intended for self-sufficient unit tests. It is based on the
 * {@link TransientRepository} of Jackrabitt that is an in-memory repository. Nevertheless it requires a filesystem
 * location to put the configuration file (repository.xml) to.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class InMemoryContentRepository extends ConfigurableContentRepository {

    /**
     * SLF4J Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryContentRepository.class);

    public InMemoryContentRepository(final TemporaryFolder workingDirectory) {
        super(workingDirectory);
    }

    @Override
    public URL getConfigUrl() {
        return getClass().getResource("inMemoryRepository.xml");
    }

    /**
     * Creates a transient repository with files in the local temp directory.
     *
     * @return the created repository
     * @throws IOException
     * @throws ConfigurationException
     */
    @Override
    protected TransientRepository createRepository() throws IOException {
        try {
            final RepositoryConfig config = createRepositoryConfiguration();
            return new TransientRepository(config);
        } catch (final ConfigurationException e) {
            throw new AssertionFailedError(e.getMessage());
        }
    }

    /**
     * Closes the admin session, and in case of local transient respository for unit test, shuts down the repository and
     * cleans all temporary files.
     *
     * @throws IOException
     */
    @Override
    protected void destroyRepository() {
        final TransientRepository repository = (TransientRepository) getRepository();
        repository.shutdown();
        LOG.info("Destroyed repository at {}", repository.getHomeDir());
    }
}
