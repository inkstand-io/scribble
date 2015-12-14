package io.inkstand.scribble.http.rules;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * ResourceHandler that serves resources from a FileSystem {@link java.nio.file.Path}. The path may be of a physical
 * {@link java.nio.file.FileSystem} or a virtual one, such as a ZipFileSystem.
 * Created by Gerald Muecke on 11.12.2015.
 */
public class PathResourceHandler extends ResourceHttpHandler {

    private final Path path;

    /**
     * Creates a resource handler for the specified path.
     * @param resourcePath
     *  the path to the resource in the filesystem.
     */
    public PathResourceHandler(final Path resourcePath) {
        this.path = resourcePath;
    }

    @Override
    protected void writeResource(final OutputStream outputStream) throws IOException {
            Files.copy(path, outputStream);
    }
}
