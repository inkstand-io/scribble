package li.moskito.scribble.rules.builder;

import org.junit.rules.TemporaryFolder;

public class TemporaryFolderBuilder extends Builder<TemporaryFolder> {
    private final TemporaryFolder temporaryFolder;

    public TemporaryFolderBuilder() {
        temporaryFolder = new TemporaryFolder();
    }

    public InMemoryContentRepositoryBuilder forInMemoryRepository() {
        return new InMemoryContentRepositoryBuilder(temporaryFolder);
    }

    @Override
    public TemporaryFolder build() {
        return temporaryFolder;
    }
}