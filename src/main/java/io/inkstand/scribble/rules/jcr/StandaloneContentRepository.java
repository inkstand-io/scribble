/**
 *
 */
package io.inkstand.scribble.rules.jcr;

import java.net.URL;

import javax.jcr.Repository;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.rules.TemporaryFolder;

/**
 * Repository that supports a fully functional repository including persistence. The default configuration used by the
 * repository is a pure in memory persistence. If actual persistence is required, an according configuration has to be
 * configured.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class StandaloneContentRepository extends ConfigurableContentRepository {

    public StandaloneContentRepository(final TemporaryFolder workingDirectory) {
        super(workingDirectory);
        // set the default configuration
        setConfigUrl(getClass().getResource("inMemoryRepository.xml"));
    }

    /**
     * Sets the URL pointing to the configuration to use. The configuration has to be a valid Jackrabbit configuration.
     *
     * @see {@link http://jackrabbit.apache.org/jackrabbit-configuration.html}
     */
    @Override
    public void setConfigUrl(final URL configUrl) {
        super.setConfigUrl(configUrl);
    }

    @Override
    protected Repository createRepository() throws Exception {
        final RepositoryConfig config = createRepositoryConfiguration();
        final RepositoryImpl repository = RepositoryImpl.create(config);
        return repository;
    }

    @Override
    protected void destroyRepository() {
        ((RepositoryImpl) getRepository()).shutdown();

    }

}
