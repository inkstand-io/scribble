package io.inkstand.scribble.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;

import io.inkstand.scribble.rules.builder.TemporaryFileBuilder;
import org.apache.commons.io.IOUtils;
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
    public TemporaryFile file = new TemporaryFileBuilder(folder, "example.zip")
                                        .withContent()
                                        .fromClasspathResource("exampleTestContent1.txt").build();

    //@formatter:on

    @Rule
    public RuleChain chain = RuleChain.outerRule(folder).around(file);

    @Test
    public void testZipStructure() throws Exception {
        //prepare

        //act
        File f = file.getFile();

        //assert
        assertTrue(f.exists());
        try(InputStream is = f.toURI().toURL().openStream()){
            String content = IOUtils.toString(is);
            assertEquals("content1", content);
        }
    }
}
