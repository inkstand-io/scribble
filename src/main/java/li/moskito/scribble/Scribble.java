package li.moskito.scribble;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import li.moskito.scribble.inject.Injection;
import li.moskito.scribble.rules.BaseRule;
import li.moskito.scribble.rules.builder.Builder;
import li.moskito.scribble.rules.builder.MockContentRepositoryBuilder;
import li.moskito.scribble.rules.builder.TemporaryFolderBuilder;
import li.moskito.scribble.rules.jcr.MockContentRepository;

import org.junit.rules.RuleChain;
import org.junit.rules.TemporaryFolder;

/**
 * Main utility class for the Inkstand test framework Scribble. <h2>Injection support</h2> Scribble allows injecting
 * object into test subjects or any other objects without having to use a full-blown CDI container. This may be useful
 * and more lightweight for simple Unit Testing. If the test has more of an integration test character, a CDI container
 * should be used instead. <h2>Test Rules</h2> Scribble provides a set of technology specific test rules that were
 * initially written for testing inkstand components. But these test rules can be used in other projects that use the
 * specific technologies, too. At the moment the following technologies are supported:
 * <ul>
 * <li>Java Content Repository (JCR, JSR 170, JSR283)</li>
 * <li>Lightweight Directory Access Protocol (LDAP)</li>
 * <li>Folders and Files</li>
 * </ul>
 * <h3>Test Rules and Rule Chain, JUnit way</h3> To create a test rule, JUnit test rules may be instantiated directly:
 *
 * <pre>
 * {@literal @}Rule
 * TemporaryFolder folder = new TemporaryFolder();
 * </pre>
 *
 * If you need a chain of rules, that are nested, JUnit provides the {@link RuleChain}:
 *
 * <pre>
 * {@literal @}Rule
 * TestRule chain = RuleChain.outerRouler(new OuterRule()).around(new InnerRule());
 * </pre>
 *
 * This approach is perfectly alright but is a bit awkward when you have rules in the chain, that depend on each other.
 * In that case you have to instantiate the rules before creating the chain:
 *
 * <pre>
 * OuterRule outer = new OuterRule();
 * InnerRule inner = new InnerRule(outer); //inner depends on outer
 * {@literal @}Rule
 * TestRule chain = RuleChain.outerRouler(outer).around(inner);
 * </pre>
 *
 * <h3>Test Rule Chains, Scribble way</h3> Scribble provides a set of rules that have dependencies on other rules. But
 * the rules may be created using a builder pattern that allows the creation and configuration of interdependent rule in
 * single line statements, that make the test setup more readable. The dependency mechanism is provided in the
 * {@link BaseRule} on which all the Scribble rules depend on. Furhter, the {@link Scribble} utility class provides
 * factory methods for creating specific rule builders.<br>
 * Examples:
 * <ul>
 * <li>Creating a temporary file with content from a classpath resource in a temporary folder:<br>
 * Rule for a file with content
 *
 * <pre>
 * {@literal @}Rule
 * TemporaryFile file = Scribble.newTempFolder().aroundTempFile("example.txt").withContent().fromClasspathResource("/test/example.txt").build();
 * </pre>
 *
 * Rule for an empty file
 *
 * <pre>
 * {@literal @}Rule
 * TemporaryFile file = Scribble.newTempFolder().aroundTempFile("example.txt").build();
 * </pre>
 *
 * </li>
 * <li>Creating an in memory JCR content repository
 *
 * <pre>
 * {@literal @}Rule
 * InMemoryContentRepository jcr = Scribble.newTempFolder().aroundInMemoryContentRepository().build();
 * </pre>
 *
 * </li>
 * <li>Creating an active JCR Session
 *
 * <pre>
 * {@literal @}Rule
 * ActiveSession jcrSession = Scribble.newTempFolder().aroundInMemoryContentRepository().aroundSession().build();
 * </pre>
 *
 * </li>
 * <li>Creating an content repository with initialized content.
 *
 * <pre>
 * {@literal @}Rule
 * ContentLoader content = Scribble.newTempFolder().aroundInMemoryContentRepository().aroundPreparedContent().fromUrl(someUrl).build();
 * </pre>
 *
 * </li>
 * </ul>
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public final class Scribble {

    private Scribble() {
    }

    /**
     * Helper method to create an {@link Injection} support instance for the value.
     *
     * @param target
     *            the value to be injected
     * @return an {@link Injection} support for injecting the value into a target
     */
    public static Injection inject(final Object value) {
        return new Injection(value);
    }

    /**
     * Creates a new {@link Builder} for a {@link TemporaryFolder}. The {@link TemporaryFolderBuilder} allows further
     * chaining of rules that require a temporary folder. <br>
     * To create a new Temporary folder there are two options:
     * <ul>
     * <li>instantiate it directly, using the JUnit way:
     *
     * <pre>
     * TemporaryFolder folder = new TemporaryFolder();
     * </pre>
     *
     * </li>
     * <li>instantiate it with a builder (the Scribble way):
     *
     * <pre>
     * TemporaryFolder folder = Scribble.newTempFolder().build();
     * </pre>
     *
     * This is slightly longer but allows chaining of dependent rules.</li>
     * </ul>
     *
     * @return a {@link TemporaryFolderBuilder}.
     */
    public static TemporaryFolderBuilder newTempFolder() {
        return new TemporaryFolderBuilder();
    }

    /**
     * Creates a new {@link Builder} for a {@link MockContentRepository}
     *
     * @return a {@link MockContentRepositoryBuilder}
     */
    public static MockContentRepositoryBuilder newMockContentRepository() {
        return new MockContentRepositoryBuilder();
    }

    /**
     * Generates a random run id.
     *
     * @return
     */
    public static String generateRunId() {
        final String ts = new SimpleDateFormat("YYYYMMdd-mm-ss").format(new Date());
        final String runID = ts + "_" + Math.abs(new Random(System.currentTimeMillis()).nextInt()) + "";
        return runID;
    }
}
