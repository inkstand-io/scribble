package io.inkstand.scribble.rules.builder;

import io.inkstand.scribble.rules.jcr.ActiveSession;
import io.inkstand.scribble.rules.jcr.ContentRepository;

/**
 * A builder for a {@link ActiveSession}. The {@link ActiveSession} rule requires {@link ContentRepository} as outer rule.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class JCRSessionBuilder extends Builder<ActiveSession> {

    private final ActiveSession jcrSession;

    public JCRSessionBuilder(final ContentRepository contentRepository) {
        jcrSession = new ActiveSession(contentRepository);
    }

    @Override
    public ActiveSession build() {
        return jcrSession;
    }

}