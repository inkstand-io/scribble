package io.inkstand.scribble.rules.builder;

import static org.junit.Assert.assertNotNull;
import io.inkstand.scribble.rules.jcr.MockContentRepository;

import org.junit.Before;
import org.junit.Test;

public class MockContentRepositoryBuilderTest {

    private MockContentRepositoryBuilder subject;

    @Before
    public void setUp() throws Exception {

        subject = new MockContentRepositoryBuilder();
    }

    @Test
    public void testBuild() throws Exception {

        final MockContentRepository repository = subject.build();

        assertNotNull(repository);
    }

}
