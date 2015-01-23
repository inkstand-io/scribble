package io.inkstand.scribble.rules.builder;

import static org.junit.Assert.assertNotNull;
import io.inkstand.scribble.rules.TemporaryFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TemporaryFileBuilderTest {

    private String fileName;

    @Mock
    private TemporaryFolder folder;

    private TemporaryFileBuilder subject;

    @Before
    public void setUp() throws Exception {
        fileName = "testFile.txt";
        subject = new TemporaryFileBuilder(folder, fileName);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBuild() throws Exception {
        final TemporaryFile file = subject.build();
        assertNotNull(file);
    }

    @Test
    public void testFromClasspathResource() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testFromResource() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testWithContent() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

}
