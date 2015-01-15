package li.moskito.scribble.rules.builder;

import li.moskito.scribble.rules.TemporaryFile;
import li.moskito.scribble.rules.jcr.InMemoryContentRepository;

import org.junit.rules.TemporaryFolder;

/**
 * Builder for the Temporary Folder.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class TemporaryFolderBuilder extends Builder<TemporaryFolder> {

    private final TemporaryFolder temporaryFolder;

    public TemporaryFolderBuilder() {
        temporaryFolder = new TemporaryFolder();
    }

    /**
     * Creates a builder for an {@link InMemoryContentRepository} that is chained inside the {@link TemporaryFolder}
     *
     * @return an {@link InMemoryContentRepositoryBuilder}
     */
    public InMemoryContentRepositoryBuilder aroundInMemoryContentRepository() {
        return new InMemoryContentRepositoryBuilder(build());
    }

    /**
     * Creates a builder for a {@link TemporaryFile} that is chained inside the {@link TemporaryFolder}
     * 
     * @param filename
     *            the name of the temporary file
     * @return a {@link TemporaryFileBuilder}
     */
    public TemporaryFileBuilder aroundTemporaryFile(final String filename) {
        return new TemporaryFileBuilder(build(), filename);
    }

    @Override
    public TemporaryFolder build() {
        return temporaryFolder;
    }
}