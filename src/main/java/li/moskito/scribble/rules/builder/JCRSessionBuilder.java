package li.moskito.scribble.rules.builder;

import li.moskito.scribble.rules.jcr.ContentRepository;
import li.moskito.scribble.rules.jcr.JCRSession;

/**
 * A builder for a {@link JCRSession}. The {@link JCRSession} rule requires {@link ContentRepository} as outer rule.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
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