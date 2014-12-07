package li.moskito.scribble;

import java.net.URL;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import li.moskito.scribble.rules.ContentRepository;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentLoader implements TestRule {

    /**
     * SLF4J Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(ContentLoader.class);

    private final ContentRepository repository;
    private final URL contentDescriptorUrl;

    public ContentLoader(final ContentRepository repository, final URL contentDescriptorUrl) {
        this.repository = repository;
        this.contentDescriptorUrl = contentDescriptorUrl;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {

        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                try {
                    LOG.info("Loading Content");
                    final Repository repo = repository.getRepository();
                    final Session session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
                    // TODO replace with content provider
                    // JCRUtil.loadContent(session, contentDescriptorUrl);
                    session.logout();
                    base.evaluate();

                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }

            }

        };

    }

}
