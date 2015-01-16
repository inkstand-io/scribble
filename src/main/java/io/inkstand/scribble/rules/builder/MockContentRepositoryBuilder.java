package io.inkstand.scribble.rules.builder;

import io.inkstand.scribble.rules.jcr.MockContentRepository;

/**
 * A builder for a {@link MockContentRepository}
 * 
 * @author Gerald Muecke, gerald@moskito.li
 */
public class MockContentRepositoryBuilder extends Builder<MockContentRepository> {

    @Override
    public MockContentRepository build() {
        return new MockContentRepository();
    }

}
