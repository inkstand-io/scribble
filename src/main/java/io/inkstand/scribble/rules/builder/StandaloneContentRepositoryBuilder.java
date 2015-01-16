package io.inkstand.scribble.rules.builder;

import io.inkstand.scribble.rules.jcr.StandaloneContentRepository;

import java.net.URL;

import org.junit.rules.TemporaryFolder;

/**
 * A Builder for an {@link StandaloneContentRepository}. The {@link StandaloneContentRepository} requires a
 * {@link TemporaryFolder} as outer rule.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class StandaloneContentRepositoryBuilder extends ContentRepositoryBuilder<StandaloneContentRepository> {

    private final StandaloneContentRepository repository;

    public StandaloneContentRepositoryBuilder(final TemporaryFolder workingDirectory) {
        repository = new StandaloneContentRepository(workingDirectory);
    }

    public StandaloneContentRepositoryBuilder withConfiguration(final URL configUrl) {
        repository.setConfigUrl(configUrl);
        return this;
    }

    @Override
    public StandaloneContentRepository build() {
        return repository;
    }

}
