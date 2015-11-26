package io.inkstand.scribble.rules;

import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.Files.createDirectories;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.junit.rules.TemporaryFolder;

/**
 * Created by Gerald Muecke on 25.11.2015.
 */
public class TemporaryZipFile extends TemporaryFile {

    private final Map<String, URL> contentMap;

    /**
     * Creates an ExternalFile in the specified temporary folder with the specified filename
     *
     * @param folder
     *  the temporary folder to create the file in
     * @param filename
     *  the name of the zip file
     * @param content
     *  the content for the zip file. The map contains a mapping for path names in the zip file to the URL containing
     *  the content for that entry
     */
    public TemporaryZipFile(final TemporaryFolder folder, final String filename, Map<String, URL> content) {
        super(folder, filename);
        this.contentMap = content;
    }


    @Override
    protected File createTempFile() throws IOException {
        File file = newFile();
        final Map<String, String> env = new HashMap<String, String>() {{
            put("create", "true");
        }};

        try(FileSystem zipFs = newFileSystem(URI.create("jar:" + file.toURI()), env)) {
            for(Map.Entry<String, URL> entry : contentMap.entrySet()){
                final Path pathToFile = zipFs.getPath(entry.getKey());
                createDirectories(pathToFile.getParent());
                try (InputStream is = entry.getValue().openStream()){
                    Files.copy(is, pathToFile);
                }
            }
        }
        return file;
    }

}
