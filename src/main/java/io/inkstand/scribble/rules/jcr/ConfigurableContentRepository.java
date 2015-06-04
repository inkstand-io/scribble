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
 * limitations under the License.
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
import java.security.Principal;
import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.commons.cnd.ParseException;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.slf4j.Logger;

import io.inkstand.scribble.rules.RuleSetup;

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
     * The node type definitions that are loaded on content repository initialization.
     *
     * @return the URL pointing to the CND resource
     */
    protected URL getCndUrl() {

        return this.cndUrl;
    }

    /**
     * Sets the URL pointing to the node type definition to be loaded upon initialization.
     * @param cndUrl
     *  resource locator for the CND note type definitions, {@see http://jackrabbit.apache.org/jcr/node-type-notation.html}
     */
    @RuleSetup
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
    @RuleSetup
    protected void setConfigUrl(final URL configUrl) {

        assertStateBefore(State.INITIALIZED);
        this.configUrl = configUrl;
    }

    @Override
    protected void initialize() {

        if(this.cndUrl != null) {
            Session session = null;
            try(InputStream cndStream = this.cndUrl.openStream();
                InputStreamReader cndReader = new InputStreamReader(cndStream, Charset.forName("UTF-8"))) {
                session = getAdminSession();
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

    /**
     * Adds a user with the given password to the repository. <p> <b>Note:</b> in case the rule is used as a class rule
     * you should ensure that you delete each created user properly. Otherwise consecutive calls will fail. You may also
     * invoke to cleanup the users created in one session using the {@code resetUsers()} method. </p>
     *
     * @param username
     *         the name of the user to add
     * @param password
     *         the password for the user
     *
     * @return the Principal representing the the newly created user.
     */
    public abstract Principal addUser(final String username, final String password);

    /**
     * Removes a user from the repository.
     *
     * @param username
     *         the name of the user to remove
     *
     * @return <code>true</code> if the user was found and successfully deleted. <code>false</code> if no such user
     * existed
     */
    public abstract boolean deleteUser(String username);

    /**
     * Removes all users from the repository that have been created using this rule. If users are already removed the
     * method will not fail.
     */
    public abstract void resetUsers();

    /**
     * Grants the specified principal (user or group) on the specified resource one or more JCR permissions.
     * @param principalId
     *  the id of the principal to grant privileges
     * @param path
     *  the path of the node to which a privilege should be applied
     * @param privilege
     *  the privileges to grant.
     */
    public void grant(String principalId, String path, String... privilege){

    };

    /**
     * Denies the specified principal (user or group) on the specified resource one or more JCR permissions.
     * @param principalId
     *  the id of the principal to deny privileges
     * @param path
     *  the path of the node to which a privilege should be applied
     * @param privilege
     *  the privileges to deny.
     */
    public void deny(String principalId, String path, String... privilege) {

    }

    /**
     * Removes all ACLs on the node specified by the path.
     * @param path
     *  the absolute path to the node
     * @param user
     *  the user(s) whose ACL entries should be removed. If none is provided, all ACLs will be removed.
     */
    public void clearACLs(String path, String... user) {

    }

}
