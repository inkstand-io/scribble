package io.inkstand.scribble.rules.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import io.inkstand.scribble.rules.TemporaryFile;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;

/**
 * Created by Gerald Muecke on 25.11.2015.
 */
public class ZipFileBuilderTest  {

    /**
     * The class under test
     */
    @InjectMocks
    private ZipFileBuilder subject;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        subject = new ZipFileBuilder(folder, "test.zip");
    }

    @Test
    public void testBuild() throws Exception {
        //prepare

        //act
        TemporaryFile file = subject.build();
        //assert
        assertNotNull(file);
    }

    @Test(expected = AssertionError.class)
    public void testAddEntryFromClasspath_resourceMissing_fail() throws Exception {

        //prepare

        //act
        subject.addEntryFromClasspath("/root", "nonexisting");

        //assert
    }

    @Test
    public void testAddEntryFromClasspath_fluidApi() throws Exception {

        //prepare

        //act
        ZipFileBuilder builder = subject.addEntryFromClasspath("/root","ZipFileBuilderTest_testContent.txt");

        //assert
        assertSame(subject, builder);
    }
}
