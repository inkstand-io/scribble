/*
 * Copyright 2015 Gerald Muecke, gerald.muecke@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.inkstand.scribble.net;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

import static io.inkstand.scribble.net.NetworkUtils.findAvailablePort;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class NetworkUtilsTest {

    private int defaultOffset;

    @Before
    public void setUp() throws Exception {
        defaultOffset = NetworkUtils.PORT_OFFSET.get();
    }

    @After
    public void tearDown() throws Exception {
        NetworkUtils.PORT_OFFSET.set(defaultOffset);
    }

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
    public void testFindAvailablePort_noPortAvailable() throws Exception {
        //prepare
        NetworkUtils.PORT_OFFSET.set(65536-1024-1);
        try(ServerSocket socket = new ServerSocket(65535)){
            //act
            int port = NetworkUtils.findAvailablePort();
            fail("AssumptionViolatedException expected");
        } catch ( AssumptionViolatedException e){
            //Saul Goodman
        }


    }

    @Test
    public void testIsPortAvailable() throws Exception {

        //prepare
        int port = new Random().nextInt(65535 - 1024) + 1024;
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
    public void testRandomPort_twoPorts() throws Exception {

        int port1 = NetworkUtils.randomPort();
        int port2 = NetworkUtils.randomPort();

        assertTrue(port1 > 1024);
        assertTrue(port2 > 1024);

        assertTrue(port1 < 65536);
        assertTrue(port2 < 65536);

        assertNotEquals(port1, port2);

    }

    @Test
    public void testRandomPort_manyPorts() throws Exception {

        for (int i = 0, len = 65536 * 20; i < len; i++) {
            int port = NetworkUtils.randomPort();
            assertTrue(port >= 1024);
            assertTrue(port < 65536);
        }
    }

    @Test
    public void testGetPortOffset_default() throws Exception {
        //prepare

        //act
        int offset = NetworkUtils.getPortOffset();

        //assert
        assertEquals(1024, offset);

    }

    @Test
    public void testGetPortOffset_customOffset() throws Exception {
        //prepare
        NetworkUtils.PORT_OFFSET.set(10000);

        //act
        int offset = NetworkUtils.getPortOffset();

        //assert
        assertEquals(11024, offset);

    }

    private boolean portAvailable(final int port) throws IOException {

        try (ServerSocket socket = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
