/*
 * Copyright 2015 Gerald Muecke, gerald.muecke@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package io.inkstand.scribble.rules.jcr;

import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;

import javax.jcr.Repository;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * Abstract {@link TestRule} for providing a JCR {@link Repository} that requires a configuration an a working
 * directory.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
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
        final File repositoryLocation = getOuterRule().getRoot();
        final URL configurationUrl = getConfigUrl();
        assertNotNull("No Repository Configuration found", configurationUrl);

        return RepositoryConfig.create(configurationUrl.openStream(), repositoryLocation.getAbsolutePath());
    }

}
