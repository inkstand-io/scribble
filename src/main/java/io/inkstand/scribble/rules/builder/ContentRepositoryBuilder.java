/*
 * Copyright 2015 Gerald Muecke, gerald.muecke@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package io.inkstand.scribble.rules.builder;

import io.inkstand.scribble.rules.jcr.ActiveSession;
import io.inkstand.scribble.rules.jcr.ContentLoader;
import io.inkstand.scribble.rules.jcr.ContentRepository;

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
