package io.inkstand.scribble.net;

import io.inkstand.scribble.net.matchers.ListenPortMatcher;
import org.junit.Test;

import static io.inkstand.scribble.net.NetworkMatchers.isAvailable;
import static io.inkstand.scribble.net.NetworkMatchers.port;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class NetworkMatchersTest {

    @Test
    public void testIsAvailable() throws Exception {
        //prepare

        //act
        ListenPortMatcher matcher = isAvailable();

        //assert
        assertNotNull(matcher);
    }

    @Test
    public void testPort() throws Exception {

        //prepare

        int expected = 123;

        //act
        TcpPort port = port(expected);

        //assert
        assertNotNull(port);
        assertEquals(expected, port.getPortNumber());
    }
}
