package io.inkstand.scribble.rules.ldap;

import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DirectoryServer implements TestRule {

    private final Directory directory;

    private int tcpPort = 10389;
    private LdapServer ldapServer;

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

    protected void startServer() throws Exception { // NOSONAR

        ldapServer = new LdapServer();
        ldapServer.setDirectoryService(directory.getDirectoryService());
        ldapServer.setTransports(new TcpTransport(getTcpPort()));
        ldapServer.start();
    }

    /**
     * The tcp port the ldap server listens for incoming connections
     * @return
     *  the tcp port number
     */
    protected int getTcpPort() {

        return tcpPort;
    }

    /**
     * Sets the TCP port the LDAP server will listen on for incoming connections. The port must be set before
     * the rule is applied
     * @param tcpPort
     *  the tcp port number
     */
    protected void setTcpPort(final int tcpPort) {

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
     * The directory service manages the entries provided
     * by the LdapServer
     * @return
     *  the DirectoryService to access the entries of the LDAP server directly
     */
    public DirectoryService getDirectoryService() {

        return directory.getDirectoryService();
    }

    /**
     * Shuts down the ldap server. This method is invoked by the apply statement. Override to add additional
     * shutdown behavior.
     */
    protected void shutdownServer() {
        ldapServer.stop();

    }
}
