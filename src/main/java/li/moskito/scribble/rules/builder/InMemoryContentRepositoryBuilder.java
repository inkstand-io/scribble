package li.moskito.scribble.rules.builder;

import li.moskito.scribble.rules.jcr.InMemoryContentRepository;

import org.junit.rules.TemporaryFolder;

/**
 * A Builder for an {@link InMemoryContentRepository}. The {@link InMemoryContentRepository} requires a temporary folder
 * as outer rule.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class InMemoryContentRepositoryBuilder extends ContentRepositoryBuilder<InMemoryContentRepository> {

    private final InMemoryContentRepository contentRepository;

    public InMemoryContentRepositoryBuilder(final TemporaryFolder temporaryFolder) {
        contentRepository = new InMemoryContentRepository(temporaryFolder);
    }

    @Override
    public InMemoryContentRepository build() {
        return contentRepository;
    }

}