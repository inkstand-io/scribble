package li.moskito.scribble.rules.builder;

import li.moskito.scribble.rules.jcr.MockContentRepository;

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
