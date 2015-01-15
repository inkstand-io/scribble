package li.moskito.scribble.rules.builder;

import li.moskito.scribble.rules.jcr.ContentRepository;
import li.moskito.scribble.rules.jcr.JCRSession;

public class JCRSessionBuilder extends Builder<JCRSession> {

    private final JCRSession jcrSession;

    public JCRSessionBuilder(final ContentRepository contentRepository) {
        jcrSession = new JCRSession(contentRepository);
    }

    @Override
    public JCRSession build() {
        return jcrSession;
    }

}