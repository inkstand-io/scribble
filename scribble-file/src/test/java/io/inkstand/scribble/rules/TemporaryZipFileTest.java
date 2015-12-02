package io.inkstand.scribble.rules;

import static io.inkstand.scribble.rules.ZipAssert.assertZipContent;
import static io.inkstand.scribble.rules.ZipAssert.assertZipFolderExists;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

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
        assertZipFolderExists(zf, "emptyFolder/");
    }


}
