package io.inkstand.scribble.net;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

import static io.inkstand.scribble.net.NetworkUtils.findAvailablePort;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class NetworkUtilsTest {

    @Test
    public void testFindAvailablePort_defaultRetries() throws Exception {

        //prepare

        //act
        int port = findAvailablePort();

        //assert
        assertTrue("Port " + port + " is not available",  portAvailable(port));
    }

    @Test
    public void testFindAvailablePort_maxRetries() throws Exception {

        //prepare

        //act
        int port = findAvailablePort(10);

        //assert
        assertTrue("Port " + port + " is not available",  portAvailable(port));
    }

    @Test
    public void testFindAvailablePort_maxRetries_retryLimitReached() throws Exception {

        //prepare

        //act
        //only one try
        int port = findAvailablePort(0);

        //assert
        assertTrue("Port " + port + " is not available",  portAvailable(port));
    }

    @Test
    public void testIsPortAvailable() throws Exception {

        //prepare
        int port = new Random().nextInt(65536 - 1024) + 1024;
        assumeTrue("Port " + port + " is not available", portAvailable(port));

        //act
        boolean portAvailable = NetworkUtils.isPortAvailable(port);

        //assert
        assertTrue(portAvailable);

    }

    @Test
    public void testIsPortAvailable_portNotAvailable() throws Exception {

        //prepare
        int port = new Random().nextInt(65536 - 1024) + 1024;
        assumeTrue("Port " + port + " is not available", portAvailable(port));
        //block port by using it
        boolean portAvailable;
        try(ServerSocket socket = new ServerSocket(port)) {
            //act
            portAvailable = NetworkUtils.isPortAvailable(port);
        }
        //assert
        assertFalse(portAvailable);


    }

    @Test
    public void testRandomPort() throws Exception {

        int port1 = NetworkUtils.randomPort();
        int port2 = NetworkUtils.randomPort();

        assertTrue(port1 > 1024);
        assertTrue(port2 > 1024);

        assertTrue(port1 < 65536);
        assertTrue(port2 < 65536);

        assertNotEquals(port1, port2);

    }

    private boolean portAvailable(final int port) throws IOException {

        try (ServerSocket socket = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
