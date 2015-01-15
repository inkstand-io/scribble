package li.moskito.scribble.rules.builder;

import li.moskito.scribble.rules.jcr.ContentLoader;
import li.moskito.scribble.rules.jcr.ContentRepository;
import li.moskito.scribble.rules.jcr.JCRSession;

/**
 * Abstract Builder for {@link ContentRepository} rules.
 *
 * @author Gerald Muecke, gerald@moskito.li
 * @param <T>
 *            the Type of the {@link ContentRepository}
 */
public abstract class ContentRepositoryBuilder<T extends ContentRepository> extends Builder<T> {

    /**
     * Creates a {@link JCRSessionBuilder} for building a {@link JCRSession} with the {@link ContentRepository} as outer
     * rule.
     *
     * @return
     */
    public JCRSessionBuilder aroundJCRSession() {
        return new JCRSessionBuilder(build());
    }

    /**
     * Creates a {@link ContentLoaderBuilder} for building a {@link ContentLoader} with the {@link ContentRepository} as
     * outer rule.
     * 
     * @return
     */
    public ContentLoaderBuilder aroundContentLoader() {
        return new ContentLoaderBuilder(build());
    }

}