package io.inkstand.scribble;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.inkstand.scribble.inject.Injection;
import io.inkstand.scribble.rules.builder.InMemoryContentRepositoryBuilder;
import io.inkstand.scribble.rules.builder.JNDIContentRepositoryBuilder;
import io.inkstand.scribble.rules.builder.MockContentRepositoryBuilder;
import io.inkstand.scribble.rules.builder.StandaloneContentRepositoryBuilder;
import io.inkstand.scribble.rules.builder.TemporaryFolderBuilder;

/**
 * Created by Gerald on 18.05.2015.
 */
public class ScribbleTest {

    @Test
    public void testInject() throws Exception {

        //prepare
        String value = "123";

        //act
        Injection result = Scribble.inject(value);

        //assert
        assertNotNull(result);
    }

    @Test
    public void testNewTempFolder() throws Exception {

        //act
        TemporaryFolderBuilder result = Scribble.newTempFolder();

        //assert
        assertNotNull(result);
    }

    @Test
    public void testNewMockContentRepository() throws Exception {

        //act
        MockContentRepositoryBuilder result = Scribble.newMockContentRepository();

        //assert
        assertNotNull(result);
    }

    @Test
    public void testNewJNDIContextRepository() throws Exception {

        //act
        JNDIContentRepositoryBuilder result = Scribble.newJNDIContentRepository();

        //assert
        assertNotNull(result);
    }

    @Test
    public void testNewInMemoryContentRepository() throws Exception {

        //act
        InMemoryContentRepositoryBuilder result = Scribble.newInMemoryContentRepository();

        //assert
        assertNotNull(result);
    }

    @Test
    public void testNewStandaloneContentRepository() throws Exception {
        //act
        StandaloneContentRepositoryBuilder result = Scribble.newStandaloneContentRepository();

        //assert
        assertNotNull(result);
    }
}
