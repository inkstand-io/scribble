package io.inkstand.scribble.rules.builder;

import static io.inkstand.scribble.util.CallStack.getCallerClass;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.inkstand.scribble.Builder;
import io.inkstand.scribble.rules.TemporaryFile;
import io.inkstand.scribble.rules.TemporaryZipFile;
import io.inkstand.scribble.util.ResourceResolver;
import org.junit.rules.TemporaryFolder;

/**
 * A builder to build a temporary zip file from resources.
 * Created by Gerald Muecke on 24.11.2015.
 */
public class ZipFileBuilder extends Builder<TemporaryFile> {

    private final TemporaryFolder folder;
    private final String filename;
    private final Map<String, URL> entryMap;
    private final ResourceResolver resolver;

    public ZipFileBuilder(final TemporaryFolder folder, String filename){
        this.folder = folder;
        this.filename = filename;
        this.entryMap = new HashMap<>();
        this.resolver = new ResourceResolver(true);
    }

    @Override
    public TemporaryFile build() {
        return new TemporaryZipFile(folder, filename, entryMap);
    }

    /**
     * Adds an entry to the zip file from a classpath resource.
     * @param zipEntryPath
     *  the path of the entry in the zip file. If the path denotes a path (ends with '/') the resource is
     *  put under its own name on that location. If it denotes a file, it will be put as this file into the zip.
     * @param pathToResource
     *  the path to the resource in the classpath
     * @return
     *  this builder
     */
    public ZipFileBuilder addEntryFromClasspath(final String zipEntryPath, final String pathToResource) {
        final Class<?> callerClass = getCallerClass();
        final URL resource = resolver.resolve(pathToResource, callerClass);
        this.entryMap.put(zipEntryPath, resource);
        return this;
    }


}
