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

package io.inkstand.scribble.rules.jcr;

import org.mockito.Mockito;

import javax.jcr.Repository;
import java.io.IOException;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

/**
 * A ContentRepository implementation that creates a deep-stubbed mock using Mockito. Mock behavior can be defined by
 * using the {@code when} method of {@link Mockito} on the {@link Repository} that can be obtained by
 * {@code getRepository} <br>
 * Example:<br>
 *
 * <pre>
 * <code>
 * final Repository mock = mockRepository.injectTo(subject).getRepository();
 * when(mock.login()).thenThrow(LoginException.class);
 * </code>
 * </pre>
 *
 * The MockContentRepository can be initialized per test or per class.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class MockContentRepository extends ContentRepository {

    public MockContentRepository() {
        super(null);
    }

    /**
     * Creates a repository mock.
     */
    @Override
    protected Repository createRepository() throws IOException {
        return mock(Repository.class, RETURNS_DEEP_STUBS);
    }

    @Override
    protected void destroyRepository() { // NOSONAR

    }

}
