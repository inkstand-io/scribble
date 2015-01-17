package io.inkstand.scribble.rules.jcr;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.jcr.Repository;

import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;

/**
 * Abstract {@link TestRule} for providing a JCR {@link Repository} that requires a configuration an a working
 * directory.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public abstract class ConfigurableContentRepository extends ContentRepository {

    private URL configUrl;

    public ConfigurableContentRepository(final TemporaryFolder workingDirectory) {
        super(workingDirectory);
    }

    /**
     * The URL referring to the resource containing the configuration for the repository.
     *
     * @return the URL pointing to the configuration of the repository.
     */
    protected URL getConfigUrl() {
        return configUrl;
    }

    /**
     * Sets the URL that refers to the resource containing the configuration for the repository. An implementation of
     * the class should provide a default configuration for convenience therefore the method is marked as optional.
     *
     * @param configUrl
     *            the configuration to use for the repository
     */
    protected void setConfigUrl(final URL configUrl) {
        this.configUrl = configUrl;
    }

    /**
     * Creates a {@link RepositoryConfig} for instantiating the repository.
     *
     * @return {@link RepositoryConfig} to use
     * @throws ConfigurationException
     *             if the configuration is not valid
     * @throws IOException
     *             if the configuration can not be read
     */
    protected RepositoryConfig createRepositoryConfiguration() throws ConfigurationException, IOException {
        final File repositoryLocation = getWorkingDirectory().getRoot();
        final URL configurationUrl = getConfigUrl();
        assertNotNull("No Repository Configuration found", configurationUrl);

        return RepositoryConfig.create(configurationUrl.openStream(), repositoryLocation.getAbsolutePath());
    }

}
