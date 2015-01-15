package li.moskito.scribble.rules.builder;

import li.moskito.scribble.rules.jcr.ActiveSession;
import li.moskito.scribble.rules.jcr.ContentLoader;
import li.moskito.scribble.rules.jcr.ContentRepository;

/**
 * Abstract Builder for {@link ContentRepository} rules.
 *
 * @author Gerald Muecke, gerald@moskito.li
 * @param <T>
 *            the Type of the {@link ContentRepository}
 */
public abstract class ContentRepositoryBuilder<T extends ContentRepository> extends Builder<T> {

    /**
     * Creates a {@link JCRSessionBuilder} for building a {@link ActiveSession} with the {@link ContentRepository} as
     * outer rule.
     *
     * @return
     */
    public JCRSessionBuilder aroundSession() {
        return new JCRSessionBuilder(build());
    }

    /**
     * Creates a {@link ContentLoaderBuilder} for building a {@link ContentLoader} with the {@link ContentRepository} as
     * outer rule.
     *
     * @return
     */
    public ContentLoaderBuilder aroundPreparedContent() {
        return new ContentLoaderBuilder(build());
    }

}