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

import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.undertow.io.IoCallback;
import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.Resource;
import io.undertow.util.ETag;
import io.undertow.util.MimeMappings;
import org.slf4j.Logger;

/**
 * A resource inside a {@link java.nio.file.FileSystem}. This resource uses the java.nio Files API which makes it
 * flexible for hosting files from various types of filesystems, such as ZipFileSystem. Created by Gerald Muecke on
 * 08.12.2015.
 */
public class FileSystemResource implements Resource {

    private static final Logger LOG = getLogger(FileSystemResource.class);

    private final Path path;

    /**
     * Creates a FileSystemResource for the specified Path.
     * @param path
     *  the path to the resource in the filesystem
     */
    public FileSystemResource(Path path) {

        this.path = path;
    }

    @Override
    public String getPath() {

        return path.toString();
    }

    @Override
    public Date getLastModified() {

        try {
            return new Date(Files.getLastModifiedTime(path).toMillis());
        } catch (IOException e) {
            throw new AssertionError("Could not determine last modified time", e);
        }
    }

    @Override
    public String getLastModifiedString() {

        try {
            return Files.getLastModifiedTime(path).toString();
        } catch (IOException e) {
            throw new AssertionError("Could not determine last modified time", e);
        }
    }

    @Override
    public ETag getETag() {

        return null;
    }

    @Override
    public String getName() {

        return path.getFileName().toString();
    }

    @Override
    public boolean isDirectory() {

        return Files.isDirectory(path);
    }

    @Override
    public List<Resource> list() {

        final List<Resource> result;
        if (Files.isDirectory(path)) {
            result = new ArrayList<>();
            try {
                for (Path child : Files.newDirectoryStream(path)) {
                    result.add(new FileSystemResource(child));
                }
            } catch (IOException e) {
                LOG.error("Could not read directory", e);
            }

        } else {
            result = Collections.emptyList();
        }
        return result;
    }

    @Override
    public String getContentType(final MimeMappings mimeMappings) {

        String filename = path.getFileName().toString();
        int separator = filename.lastIndexOf('.');
        if (separator != -1) {
            return mimeMappings.getMimeType(filename.substring(separator + 1));
        }
        return null;
    }

    @Override
    public void serve(final Sender sender, final HttpServerExchange httpServerExchange, final IoCallback ioCallback) {

        httpServerExchange.startBlocking();
        final OutputStream outStream = httpServerExchange.getOutputStream();
        try {
            Files.copy(path, outStream);
            ioCallback.onComplete(httpServerExchange, sender);
        } catch (IOException e) {
            LOG.error("Could not serve content file", e);
            ioCallback.onException(httpServerExchange, sender, e);
        }
    }

    @Override
    public Long getContentLength() {

        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new AssertionError("Could not determine content length", e);
        }
    }

    @Override
    public String getCacheKey() {

        return null;
    }

    @Override
    public File getFile() {

        return path.toFile();
    }

    @Override
    public Path getFilePath() {

        return path;
    }

    @Override
    public File getResourceManagerRoot() {

        return path.getRoot().toFile();
    }

    @Override
    public Path getResourceManagerRootPath() {

        return path.getRoot();
    }

    @Override
    public URL getUrl() {

        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new AssertionError("Could not create URL", e);
        }
    }
}
