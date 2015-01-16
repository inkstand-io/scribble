package io.inkstand.scribble.rules.ldap;

import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DirectoryServer implements TestRule {

    private final Directory directory;

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

    protected void startServer() throws Exception {

        ldapServer = new LdapServer();
        ldapServer.setDirectoryService(directory.getDirectoryService());
        // Set LDAP port to 10389
        final TcpTransport ldapTransport = new TcpTransport(getLdapPort());
        ldapServer.setTransports(ldapTransport);

        ldapServer.start();

    }

    private int getLdapPort() {
        return 10389;
    }

    protected void shutdownServer() {
        ldapServer.stop();

    }
}
