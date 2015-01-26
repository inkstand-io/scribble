package io.inkstand.scribble.rules.builder;

import static org.junit.Assert.assertNotNull;
import io.inkstand.scribble.rules.jcr.InMemoryContentRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryContentRepositoryBuilderTest {

    @Mock
    private TemporaryFolder folder;

    private InMemoryContentRepositoryBuilder subject;

    @Before
    public void setUp() throws Exception {

        subject = new InMemoryContentRepositoryBuilder(folder);
    }

    @Test
    public void testBuild() throws Exception {

        final InMemoryContentRepository rule = subject.build();
        assertNotNull(rule);
    }

}
