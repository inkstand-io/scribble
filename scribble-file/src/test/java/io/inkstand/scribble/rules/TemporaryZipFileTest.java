package io.inkstand.scribble.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;

/**
 * Created by Gerald Muecke on 02.12.2015.
 */
public class TemporaryZipFileTest {

    /**
     * The class under test
     */
    @InjectMocks
    private TemporaryZipFile subject;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Map<String, URL> content = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        subject = new TemporaryZipFile(folder, "testfile.zip", content);
    }
    @Test
    public void testCreateTempFile() throws Exception {
        //prepare
        content.put("emptyFolder", null);
        content.put("folder/file1.txt", getClass().getResource("TemporaryZipFileTest_content1.txt"));
        content.put("file2.txt", getClass().getResource("TemporaryZipFileTest_content2.txt"));

        //act
        File file = subject.createTempFile();

        //assert
        assertNotNull(file);
        assertTrue(file.exists());

        final ZipFile zf = new ZipFile(file);
        assertZipContent(zf, "file2.txt", "content2\r\n");
        assertZipContent(zf, "folder/file1.txt", "content1\r\n");
        assertZipFolderExists(zf, "emptyFolder");
    }

    private void assertZipFolderExists(final ZipFile zf, final String entryPath) {
        final ZipEntry entry = zf.getEntry(entryPath);
        assertNotNull("Entry " + entryPath + " does not exist", entry);
        assertTrue("Entry "+entryPath+" is no folder", entry.isDirectory());
    }

    private void assertZipContent(final ZipFile zf, final String entryPath, final String expectedContent)
            throws IOException {

        final ZipEntry entry = zf.getEntry(entryPath);
        assertNotNull("Entry " + entryPath + " does not exist", entry);
        assertFalse("Entry "+entryPath+" is a directory", entry.isDirectory());
        try(InputStream is = zf.getInputStream(entry)){
            assertNotNull("Entry " + entryPath + " has no content" , is);
            final String actualContent = IOUtils.toString(is);
            assertEquals(expectedContent, actualContent);
        }
    }
}
