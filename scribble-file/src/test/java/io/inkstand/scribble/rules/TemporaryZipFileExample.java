package io.inkstand.scribble.rules;

import static io.inkstand.scribble.rules.ZipAssert.assertZipContent;
import static io.inkstand.scribble.rules.ZipAssert.assertZipFolderExists;

import java.util.zip.ZipFile;

import io.inkstand.scribble.rules.builder.TemporaryFileBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TemporaryFolder;

/**
 * Created by Gerald Muecke on 23.11.2015.
 */
public class TemporaryZipFileExample {

    public TemporaryFolder folder = new TemporaryFolder();

    //@formatter:off
    public TemporaryFile file = new TemporaryFileBuilder(folder, "example.zip")
                                        .fromClasspathResource("exampleTestContent1.txt")
                                        .asZip()
                                        .addFolder("/emptyFolder")
                                        .addClasspathResource("/text1.txt","exampleTestContent1.txt")
                                        .addClasspathResource("/test/text2.txt","exampleTestContent2.txt")
                                        .build();

    //@formatter:on

    @Rule
    public RuleChain chain = RuleChain.outerRule(folder).around(file);

    @Test
    public void testZipStructure() throws Exception {
        //prepare

        //act
        ZipFile zf = new ZipFile(file.getFile());

        //assert
        assertZipFolderExists(zf, "emptyFolder");
        assertZipContent(zf, "exampleTestContent1.txt", "content1\r\n");
        assertZipContent(zf, "text1.txt", "content1\r\n");
        assertZipContent(zf, "test/text2.txt", "content2\r\n");

    }
}
