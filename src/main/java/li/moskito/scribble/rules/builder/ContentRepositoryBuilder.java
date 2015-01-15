package li.moskito.scribble.rules.builder;

import li.moskito.scribble.rules.jcr.ContentRepository;

public abstract class ContentRepositoryBuilder<T extends ContentRepository> extends Builder<T> {

    public JCRSessionBuilder forJCRSession() {
        return new JCRSessionBuilder(getContentRepository());
    }

    public abstract T getContentRepository();
}