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

import static org.junit.Assert.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import javax.jcr.PropertyType;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.PropertyDefinition;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.commons.cnd.ParseException;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.slf4j.Logger;

/**
 * Abstract {@link TestRule} for providing a JCR {@link Repository} that requires a configuration an a working
 * directory.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public abstract class ConfigurableContentRepository extends ContentRepository {

    private static final Logger LOG = getLogger(ConfigurableContentRepository.class);

    /**
     * Url pointing to the configuration file.
     */
    private URL configUrl;

    /**
     * Url pointing to the node type definitions. If null, no nodetypes will be registered in initialization
     */
    private URL cndUrl;

    /**
     * Creates an instance of the ContentRepository rule using the specified temporary folder as working directory.
     * @param workingDirectory
     *  the working directory where the repository stores working data
     *
     */
    public ConfigurableContentRepository(final TemporaryFolder workingDirectory) {
        super(workingDirectory);
    }

    /**
     * The URL referring to the resource containing the configuration for the repository.
     *
     * @return the URL pointing to the configuration of the repository.
     */
    protected URL getConfigUrl() {
        return this.configUrl;
    }

    /**
     * Sets the URL that refers to the resource containing the configuration for the repository. An implementation of
     * the class should provide a default configuration for convenience therefore the method is marked as optional.
     *
     * @param configUrl
     *            the configuration to use for the repository
     */
    protected void setConfigUrl(final URL configUrl) {
        assertStateBefore(State.INITIALIZED);
        this.configUrl = configUrl;
    }

    /**
     * The node type definitions that are loaded on content repository initialization.
     * @return
     *  the URL pointing to the CND resource
     */
    protected URL getCndUrl() {

        return this.cndUrl;
    }

    /**
     * Sets the URL pointing to the node type definition to be loaded upon initialization.
     * @param cndUrl
     *  resource locator for the CND note type definitions, {@see http://jackrabbit.apache.org/jcr/node-type-notation.html}
     */
    protected void setCndUrl(final URL cndUrl) {
        assertStateBefore(State.INITIALIZED);
        this.cndUrl = cndUrl;
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
        final File jcrHome = getOuterRule().getRoot();
        final URL configUrl = this.getConfigUrl();
        assertNotNull("No Repository Configuration found", configUrl);

        return RepositoryConfig.create(configUrl.openStream(), jcrHome.getAbsolutePath());
    }

    @Override
    protected void initialize() {

        if(this.cndUrl != null) {
            Session session = null;
            try(InputStream cndStream = this.cndUrl.openStream();
                InputStreamReader cndReader = new InputStreamReader(cndStream, Charset.forName("UTF-8"))) {
                //TODO SCRIB-14 replace with admin login method
                session = login("admin", "admin");
                this.logNodeTypes(CndImporter.registerNodeTypes(cndReader, session));
            } catch (IOException e) {
                throw new AssertionError("Could not load CND resource", e);
            } catch (RepositoryException e) {
                throw new AssertionError("Could not perform repository operation", e);
            } catch (ParseException e) {
                throw new AssertionError("Could not parse CND resource", e);
            } finally {
                if(session != null) {
                    session.logout();
                }
            }

        }

    }

    private void logNodeTypes(final NodeType... nodeTypes) {

        if(LOG.isDebugEnabled()){
            StringBuilder buf = new StringBuilder(32);
            buf.append("[\n");
            for(NodeType nt : nodeTypes){
                buf.append(nt.getName()).append(" > ");
                for(NodeType st : nt.getSupertypes()){
                    buf.append(st.getName()).append(", ");
                }
                buf.append('\n');
                for(PropertyDefinition pd : nt.getPropertyDefinitions()){
                    buf.append("\t").append(pd.getName()).append(" (")
                       .append(PropertyType.nameFromValue(pd.getRequiredType()))
                       .append(")\n");
                }
            }
            buf.append(']');
            LOG.debug("Registered NodeTypes: {}",buf.toString());
        }
    }

}
