package io.inkstand.scribble.rules;

import static org.junit.Assert.assertNotNull;

import java.util.zip.ZipFile;

import io.inkstand.scribble.rules.builder.TemporaryFileBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TemporaryFolder;

/**
 * Created by Gerald Muecke on 23.11.2015.
 */
public class TemporaryFileExample {

    public TemporaryFolder folder = new TemporaryFolder();

    //@formatter:off
    public TemporaryFile file = new TemporaryFileBuilder(folder, "example.zip").asZip()
                                       .addEntryFromClasspath("/text1.txt","exampleTestContent1.txt")
                                       .addEntryFromClasspath("/test/text2.txt","exampleTestContent2.txt")
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
        assertNotNull(zf.getEntry("text1.txt"));
        assertNotNull(zf.getEntry("test/text2.txt"));

    }
}
