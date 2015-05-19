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

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.net.URL;
import org.apache.jackrabbit.core.RepositoryImpl;
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
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
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
    protected RepositoryImpl createRepository() throws IOException {
        try {
            final RepositoryConfig config = createRepositoryConfiguration();
            return RepositoryImpl.create(config);
        } catch (final ConfigurationException e) {
            LOG.error("Configuration invalid", e);
            throw new AssertionError(e.getMessage(), e);
        } catch (RepositoryException e) {
            LOG.error("Could not create repository", e);
            throw new AssertionError(e.getMessage(), e);
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
        final RepositoryImpl repository = (RepositoryImpl) getRepository();
        repository.shutdown();
        LOG.info("Destroyed repository at {}", repository.getConfig().getHomeDir());
    }

    /**
     * Sets the URL pointing to the node type definition to be loaded upon initialization.
     * @param nodeTypeDefinitions
     *  resource locator for the CND note type definitions, {@see http://jackrabbit.apache.org/jcr/node-type-notation.html}
     */
    @Override
    public void setCndUrl(final URL nodeTypeDefinitions) {

        super.setCndUrl(nodeTypeDefinitions);
    }
}
