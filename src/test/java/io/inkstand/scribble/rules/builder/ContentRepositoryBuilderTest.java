package io.inkstand.scribble.rules.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.net.URL;
import org.junit.Test;
import org.junit.rules.TestRule;

/**
 * Created by Gerald on 18.05.2015.
 */
public class ContentRepositoryBuilderTest {

    private ContentRepositoryBuilder subject = new ContentRepositoryBuilder() {

        @Override
        public TestRule build() {

            return null;
        }
    };

    @Test
    public void testAroundSession() throws Exception {

        //prepare

        //act
        JCRSessionBuilder result = subject.aroundSession();

        //assert
        assertNotNull(result);
    }

    @Test
    public void testAroundPreparedContent() throws Exception {

        //act
        ContentLoaderBuilder result = subject.aroundPreparedContent();

        //assert
        assertNotNull(result);
    }

    @Test
    public void testWithNodeTypesFromCnd() throws Exception {

        //prepare
        URL resource = new URL("http://localhost");

        //act
        ContentRepositoryBuilder result = subject.withNodeTypes(resource);
        URL actualResource = subject.getCndModelResource();

        //assert
        assertNotNull(result);
        assertSame(result, subject);
        assertEquals(resource, actualResource);
    }
}
