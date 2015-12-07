package io.inkstand.scribble.http.rules;

import static io.inkstand.scribble.net.NetworkMatchers.isAvailable;
import static io.inkstand.scribble.net.NetworkMatchers.port;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import io.inkstand.scribble.rules.TemporaryFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Gerald Muecke on 07.12.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class HttpServerBuilderTest {

    /**
     * The class under test
     */
    @InjectMocks
    private HttpServerBuilder subject;

    @Test
    public void testBuild_defaultValues() throws Exception {
        //prepare

        //act
        HttpServer server = subject.build();

        //assert
        assertNotNull(server);
        assertEquals("localhost", server.getHostname());
        assertTrue(server.getPort() > 1024);
        assertThat(port(server.getPort()), isAvailable());
    }

    @Test
    public void testBuild_onlyCustomPort() throws Exception {
        //prepare

        //act
        HttpServer server = subject.port(55555).build();

        //assert
        assertNotNull(server);
        assertEquals("localhost", server.getHostname());
        assertEquals(55555, server.getPort());
    }

    @Test
    public void testBuild_onlyCustomHost() throws Exception {
        //prepare

        //act
        HttpServer server = subject.hostname("someHost").build();

        //assert
        assertNotNull(server);
        assertEquals("someHost", server.getHostname());
        assertTrue(server.getPort() > 1024);
        assertThat(port(server.getPort()), isAvailable());
    }


    @Test
    public void testBuild_withPort_and_Hostname() throws Exception {
        //prepare

        //act
        HttpServer server = subject.port(55555).hostname("somehost").build();

        //assert
        assertNotNull(server);
        assertEquals("somehost", server.getHostname());
        assertEquals(55555, server.getPort());
    }

    @Test
    public void testContentFromZip_resourceExists() throws Exception {
        //prepare

        //act
        HttpServerBuilder builder = subject.contentFromZip("/", "HttpServerBuilderTest_testZipContent.zip");

        //assert
        assertSame(subject, builder);

    }
    @Test(expected = AssertionError.class)
    public void testContentFromZip_resourceNotExists_fail() throws Exception {
        //prepare

        //act
        HttpServerBuilder builder = subject.contentFromZip("/", "nonExisting");

        //assert
        assertSame(subject, builder);

    }
    @Test
    public void testContentFromZip_temporaryFile() throws Exception {
        //prepare
        TemporaryFile file = mock(TemporaryFile.class);

        //act
        HttpServerBuilder builder = subject.contentFromZip("/", file);

        //assert
        assertSame(subject, builder);

    }
}
