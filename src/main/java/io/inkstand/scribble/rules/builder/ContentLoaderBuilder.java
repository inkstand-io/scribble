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

import io.inkstand.scribble.rules.jcr.ContentLoader;
import io.inkstand.scribble.rules.jcr.ContentRepository;

import java.net.URL;

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
