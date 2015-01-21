package io.inkstand.scribble.rules.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import io.inkstand.scribble.rules.TemporaryFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TemporaryFolderBuilderTest {

    @Mock
    private Statement base;
    @Mock
    private Description description;

    private TemporaryFolderBuilder subject;

    @Before
    public void setUp() throws Exception {
        subject = new TemporaryFolderBuilder();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAroundStandaloneContentRepository() throws Exception {
        final StandaloneContentRepositoryBuilder repository = subject.aroundStandaloneContentRepository();
        assertNotNull(repository);
    }

    @Test
    public void testAroundInMemoryContentRepository() throws Exception {
        final InMemoryContentRepositoryBuilder repository = subject.aroundInMemoryContentRepository();
        assertNotNull(repository);
    }

    @Test
    public void testAroundTempFile() throws Throwable {
        // act
        final TemporaryFileBuilder builder = subject.aroundTempFile("filename.txt");

        // assert
        assertNotNull(builder);

        // to verify the filename we must actually apply the rule to a statement as the filename is
        // only accessible via the file created by the rule. And as the file is deleted after the rule
        // has been applied, we must check the name of the file inside the statement.
        // to ensure the evaluate method is actually invoked, we verify the method invocation
        final TemporaryFile tempFile = builder.build();
        final Statement stmt = spy(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                assertEquals("filename.txt", tempFile.getFile().getName());
            }

        });
        tempFile.apply(stmt, description).evaluate();
        verify(stmt).evaluate();
    }

    @Test
    public void testBuild() throws Exception {
        final TemporaryFolder tempFolder = subject.build();
        assertNotNull(tempFolder);
    }

}
