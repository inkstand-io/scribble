package li.moskito.scribble.rules.builder;

import li.moskito.scribble.rules.jcr.InMemoryContentRepository;

import org.junit.rules.TemporaryFolder;

public class InMemoryContentRepositoryBuilder extends ContentRepositoryBuilder<InMemoryContentRepository> {

    private final InMemoryContentRepository contentRepository;

    public InMemoryContentRepositoryBuilder(final TemporaryFolder temporaryFolder) {
        contentRepository = new InMemoryContentRepository(temporaryFolder);
    }

    @Override
    public InMemoryContentRepository getContentRepository() {
        return contentRepository;
    }

    @Override
    public InMemoryContentRepository build() {
        return contentRepository;
    }

}