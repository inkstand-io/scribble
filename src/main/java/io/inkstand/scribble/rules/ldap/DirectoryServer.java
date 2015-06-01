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

package io.inkstand.scribble.rules.ldap;

import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.inkstand.scribble.net.NetworkUtils;
import io.inkstand.scribble.rules.RuleSetup;

/**
 * The directory server provides an LDAP service as a {@link TestRule}. It requires a {@link Directory} test rule that
 * contains the LDAP service's content. The server may be configured regarding port and listen address. If neither is
 * configured, it listens on localhost:10389
 */
public class DirectoryServer implements TestRule {

    /**
     * the test rule that provides the {@link DirectoryService} providing the content
     */
    private final Directory directory;
    /**
     * The server instance managed by this rule
     */
    private LdapServer ldapServer;
    /**
     * the tcp port the server will accept connections
     */
    private int tcpPort = 10389;

    /**
     * the listen address of the server
     */
    private String listenAddress = null;
    private boolean autoBind;

    public DirectoryServer(final Directory directory) {
        this.directory = directory;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                startServer();
                try {
                    base.evaluate();
                } finally {
                    shutdownServer();
                }

            }

        };
    }

    /**
     * Starts the server on the configured listen address and tcp port (default localhost:10389) and assigns the {@link
     * DirectoryService} provided by the {@link Directory} rule to it
     *
     * @throws Exception
     */
    protected void startServer() throws Exception { // NOSONAR

        if (this.autoBind) {
            setTcpPort(NetworkUtils.findAvailablePort());
        }

        ldapServer = new LdapServer();
        ldapServer.setDirectoryService(directory.getDirectoryService());
        ldapServer.setTransports(new TcpTransport(getTcpPort()));
        ldapServer.start();
    }

    /**
     * Shuts down the ldap server. This method is invoked by the apply statement. Override to add additional
     * shutdown behavior.
     */
    protected void shutdownServer() {

        ldapServer.stop();

    }

    /**
     * The tcp port the ldap server listens for incoming connections
     * @return
     *  the tcp port number
     */
    public int getTcpPort() {

        return tcpPort;
    }

    /**
     * Sets the TCP port the LDAP server will listen on for incoming connections. The port must be set before
     * the rule is applied
     * @param tcpPort
     *  the tcp port number
     */
    @RuleSetup
    public void setTcpPort(final int tcpPort) {

        this.tcpPort = tcpPort;
    }

    /**
     * Provides access to the {@link org.apache.directory.server.ldap.LdapServer}
     *
     * @return
     *  the LdapServer used by this rule
     */
    public LdapServer getLdapServer() {

        return ldapServer;
    }

    /**
     * @return the listen address of the ldap server
     */
    public String getListenAddress() {

        return listenAddress;
    }

    /**
     * Sets the listen address of the server. If none is set, localhost (null) is used.
     * @param listenAddress
     *  the new listen address of the server
     */
    @RuleSetup
    public void setListenAddress(final String listenAddress) {

        this.listenAddress = listenAddress;
    }

    /**
     * The directory service manages the entries provided by the LdapServer
     *
     * @return the DirectoryService to access the entries of the LDAP server directly
     */
    public DirectoryService getDirectoryService() {

        return directory.getDirectoryService();
    }

    /**
     * Sets the rule to auto-bind, that will find an available port on each application. The port may be
     * access using the {@code getTcpPort()} method.
     * @param autoBind
     *  <code>true</code> to activate the auto-bind mode
     */
    public void setAutoBind(final boolean autoBind) {

        this.autoBind = autoBind;
    }
}
