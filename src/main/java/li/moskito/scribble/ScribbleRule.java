package li.moskito.scribble;

import li.moskito.scribble.rules.ContentRepository;
import li.moskito.scribble.rules.InMemoryContentRepository;

import org.junit.rules.RuleChain;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ScribbleRule implements TestRule {

    private TemporaryFolder tempFolder = new TemporaryFolder();
    private ContentRepository repository = new InMemoryContentRepository(tempFolder);
    private JCRSession jcrSession = new JCRSession(repository);
    private RuleChain baseRule;

    public ScribbleRule() {
        this.baseRule = RuleChain.outerRule(getTempFolder()).around(getRepository()).around(getJcrSession());
    }

    public TemporaryFolder getTempFolder() {
        return tempFolder;
    }

    public ContentRepository getRepository() {
        return repository;
    }

    public JCRSession getJcrSession() {
        return jcrSession;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return baseRule.apply(base, description);
    }

}
