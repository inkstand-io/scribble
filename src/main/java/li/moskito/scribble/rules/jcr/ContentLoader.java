package li.moskito.scribble.rules.jcr;

import java.net.URL;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import li.moskito.scribble.rules.BaseRule;
import li.moskito.scribble.rules.RuleSetup;
import li.moskito.scribble.rules.RuleSetup.RequirementLevel;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentLoader extends BaseRule {

    /**
     * SLF4J Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(ContentLoader.class);

    private final ContentRepository repository;
    private URL contentDescriptorUrl;

    public ContentLoader(final ContentRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Sets the resource containing the description of the content ro
     *
     * @param contentDescriptorUrl
     */
    @RuleSetup(RequirementLevel.REQUIRED)
    public void setContentDescriptorUrl(final URL contentDescriptorUrl) {
        assertNotInitialized();
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
                    System.out.println(contentDescriptorUrl);
                    session.logout();
                    base.evaluate();

                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }

            }

        };

    }

}
