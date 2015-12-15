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

package io.inkstand.scribble.http.rules;

import java.io.IOException;
import java.nio.file.FileSystem;

import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceChangeListener;
import io.undertow.server.handlers.resource.ResourceManager;

/**
 * A {@link io.undertow.server.handlers.resource.ResourceManager} to provide access to resources on a FileSystem.
 * Using this manager, resources from various types of {@link java.nio.file.FileSystem} s can be hosted by undertow.
 * <br>
 * Created by Gerald Muecke on 08.12.2015.
 */
public class FileSystemResourceManager implements ResourceManager {

    private final FileSystem fileSystem;

    /**
     * Creates a ResourceManager on the specified FileSytem.
     * @param fileSystem
     *  the filesystem containing the resources to be hosted by Undertow.
     */
    public FileSystemResourceManager(FileSystem fileSystem){
        this.fileSystem = fileSystem;
    }

    @Override
    public Resource getResource(final String s) throws IOException {

        return new FileSystemResource(fileSystem.getPath(s));
    }

    @Override
    public boolean isResourceChangeListenerSupported() {

        return false;
    }

    @Override
    public void registerResourceChangeListener(final ResourceChangeListener resourceChangeListener) {

    }

    @Override
    public void removeResourceChangeListener(final ResourceChangeListener resourceChangeListener) {

    }

    @Override
    public void close() throws IOException {
        fileSystem.close();
    }
}
