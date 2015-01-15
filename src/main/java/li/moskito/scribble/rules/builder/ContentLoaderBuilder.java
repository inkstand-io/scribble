package li.moskito.scribble.rules.builder;

import java.net.URL;

import li.moskito.scribble.rules.jcr.ContentLoader;
import li.moskito.scribble.rules.jcr.ContentRepository;

public class ContentLoaderBuilder extends Builder<ContentLoader> {

    private final ContentLoader contentLoader;

    public ContentLoaderBuilder(final ContentRepository repository) {
        contentLoader = new ContentLoader(repository);
    }

    /**
     * Uses the content descriptor specified by the URL.
     *
     * @param contentDescriptorUrl
     *            the URL referencing a content descriptor file
     * @return this builder
     */
    public ContentLoaderBuilder fromUrl(final URL contentDescriptorUrl) {
        contentLoader.setContentDescriptorUrl(contentDescriptorUrl);
        return this;
    }

    @Override
    public ContentLoader build() {
        return contentLoader;
    }

}
