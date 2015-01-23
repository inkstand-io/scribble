package io.inkstand.scribble.rules.jcr;

import io.inkstand.scribble.rules.ExternalResource;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.junit.rules.TestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link TestRule} for creating an active JCR session for a test.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class ActiveSession extends ExternalResource<ContentRepository> {

    /**
     * SLF4J Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(ActiveSession.class);

    private Session adminSession;
    private Session anonSession;
    private final Map<String, Session> userSessions = new HashMap<>();

    private final ContentRepository repositoryRule;
    private final String password;
    private final String username;

    public ActiveSession(final ContentRepository repository) {
        this(repository, null, null);
    }

    public ActiveSession(final ContentRepository repository, final String username, final String password) {
        super(repository);
        repositoryRule = repository;
        this.username = username;
        this.password = password;
    }

    /**
     * Closes all sessions
     */
    @Override
    protected void after() {
        super.after();
        if (adminSession != null) {
            LOG.info("Logging off {}", adminSession.getUserID());
            adminSession.logout();
            adminSession = null;
        }
        if (anonSession != null) {
            LOG.info("Logging off {}", anonSession.getUserID());
            anonSession.logout();
            anonSession = null;
        }
        for (final Session session : userSessions.values()) {
            LOG.info("Logging off {}", session.getUserID());
            session.logout();
        }
        userSessions.clear();
        LOG.info("Closed all sessions");
    }

    /**
     * @return the repository
     */
    public Repository getRepository() {
        assertInitialized();
        return repositoryRule.getRepository();
    }

    /**
     * Logs into the repository. If a username and password has been specified, is is used for the login, otherwise an
     * anonymous login is done.
     *
     * @return the session for the login
     * @throws RepositoryException
     * @throws LoginException
     */
    public Session login() throws RepositoryException {
        assertInitialized();
        final Session session;
        if (username != null && password != null) {
            session = getRepository().login(new SimpleCredentials(username, password.toCharArray()));
            userSessions.put(username, session);
        } else if (anonSession == null) {
            anonSession = getRepository().login();
            session = anonSession;
        } else {
            session = anonSession;
        }
        return session;
    }

    /**
     * Creates a login for the given username and password
     *
     * @param username
     *            the username to log in
     * @param password
     *            the password to log in
     * @return the session for the user
     * @throws RepositoryException
     */
    public Session login(final String username, final String password) throws RepositoryException {
        assertInitialized();
        if (!userSessions.containsKey(username)) {
            userSessions.put(username, getRepository().login(new SimpleCredentials(username, password.toCharArray())));
        }
        return userSessions.get(username);
    }

    /**
     * @return a session with priviledges access rights
     * @throws RepositoryException
     */
    public Session getAdminSession() throws RepositoryException {
        assertInitialized();
        if (adminSession == null) {
            adminSession = getRepository().login(new SimpleCredentials("admin", "admin".toCharArray()));
        }
        return adminSession;
    }
}
