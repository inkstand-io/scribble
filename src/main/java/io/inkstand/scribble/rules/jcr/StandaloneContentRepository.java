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

/**
 *
 */
package io.inkstand.scribble.rules.jcr;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.rules.TemporaryFolder;

import javax.jcr.Repository;
import java.net.URL;

/**
 * Repository that supports a fully functional repository including persistence. The default configuration used by the
 * repository is a pure in memory persistence. If actual persistence is required, an according configuration has to be
 * configured.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
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
