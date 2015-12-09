package io.inkstand.scribble.http.rules;

import java.io.IOException;
import java.nio.file.FileSystem;

import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceChangeListener;
import io.undertow.server.handlers.resource.ResourceManager;

/**
 * Created by Gerald Muecke on 08.12.2015.
 */
public class FileSystemResourceManager implements ResourceManager {

    private final FileSystem fs;

    public FileSystemResourceManager(FileSystem fs){
        this.fs = fs;
    }

    @Override
    public Resource getResource(final String s) throws IOException {

        return new FileSystemResource(fs.getPath(s));
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
        fs.close();
    }
}
