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
 * limitations under the License.
 */

package io.inkstand.scribble.rules.builder;

import org.junit.rules.TemporaryFolder;

import io.inkstand.scribble.rules.jcr.InMemoryContentRepository;

/**
 * A Builder for an {@link InMemoryContentRepository}. The {@link InMemoryContentRepository} requires a
 * {@link TemporaryFolder} as outer rule.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public class InMemoryContentRepositoryBuilder extends ContentRepositoryBuilder<InMemoryContentRepository> {

    private final TemporaryFolder temporaryFolder;

    public InMemoryContentRepositoryBuilder(final TemporaryFolder temporaryFolder) {
        this.temporaryFolder = temporaryFolder;
    }

    @Override
    public InMemoryContentRepository build() {

        InMemoryContentRepository repository = new InMemoryContentRepository(temporaryFolder);
        repository.setCndUrl(getCndModelResource());
        return repository;
    }

    public InMemoryContentRepositoryBuilder enableSecurity() {

        return this;

    }
}
