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

import io.inkstand.scribble.rules.TemporaryFile;
import org.junit.rules.TemporaryFolder;

import java.net.URL;

/**
 * Builder for creating a temporary file in a temporary folder.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public class TemporaryFileBuilder extends Builder<TemporaryFile> {

    private final TemporaryFile temporaryFile;

    public TemporaryFileBuilder(final TemporaryFolder folder, final String fileName) {

        temporaryFile = new TemporaryFile(folder, fileName);
    }

    @Override
    public TemporaryFile build() {

        return temporaryFile;
    }

    /**
     * Defines the classpath resource from where the content of the file should be retrieved
     * 
     * @param pathToResource
     *            the path to the classpath resource
     * @return the builder
     */
    public TemporaryFileBuilder fromClasspathResource(final String pathToResource) {

        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        URL contentUrl = null;
        if (ccl != null) {
            contentUrl = ccl.getResource(pathToResource);
        }
        if (contentUrl == null) {
            contentUrl = getClass().getResource(pathToResource);
        }
        temporaryFile.setContentUrl(contentUrl);
        return this;
    }

    /**
     * Defines the resource by URL from where the content of the file should be retrieved
     * 
     * @param resource
     *            the resource whose content will be used for the temporary file as content
     * @return the builder
     */
    public TemporaryFileBuilder fromResource(final URL resource) {

        temporaryFile.setContentUrl(resource);
        return this;
    }

    /**
     * Defines, that the external file must not be empty, which means, the rule enforces, the contentUrl is set. The
     * resource addressed by the URL may be empty nevertheless.
     * 
     * @return the builder
     */
    public TemporaryFileBuilder withContent() {

        temporaryFile.setForceContent(true);
        return this;
    }

}
