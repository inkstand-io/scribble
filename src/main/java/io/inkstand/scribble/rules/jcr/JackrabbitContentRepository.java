package io.inkstand.scribble.rules.jcr;

import static org.slf4j.LoggerFactory.getLogger;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;

/**
 * The base implementation for Jackrabbit based repositories. Jackrabbit is the reference
 * implementation of the JCR standard.
 *
 * Created by gerald on 03.06.15.
 */
public abstract class JackrabbitContentRepository extends ConfigurableContentRepository {

    private static final Logger LOG = getLogger(JackrabbitContentRepository.class);

    public JackrabbitContentRepository(final TemporaryFolder workingDirectory) {
        super(workingDirectory);

    }


    public User addUser(final String username, final String password) throws RepositoryException {
        Session session = getAdminSession();
        final UserManager userManager = ((JackrabbitSession) session).getUserManager();
        final User user = userManager.createUser(username, password);
        session.save();
        return user;
    }

    public boolean deleteUser(String username) throws RepositoryException {
        Session session = getAdminSession();
        final UserManager userManager = ((JackrabbitSession) session).getUserManager();
        final User user = (User) userManager.getAuthorizable(username);
        if(user == null){
            return false;
        }
        user.remove();
        session.save();
        return true;
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
}
