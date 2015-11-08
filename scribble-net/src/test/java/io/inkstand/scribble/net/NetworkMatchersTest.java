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

import static io.inkstand.scribble.net.NetworkMatchers.isAvailable;
import static io.inkstand.scribble.net.NetworkMatchers.isReachable;
import static io.inkstand.scribble.net.NetworkMatchers.port;
import static io.inkstand.scribble.net.NetworkMatchers.remotePort;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public class NetworkMatchersTest {

    @Test
    public void testIsAvailable() throws Exception {
        //prepare

        //act
        ResourceAvailabilityMatcher matcher = isAvailable();

        //assert
        assertNotNull(matcher);
    }

    @Test
    public void testIsReachable() throws Exception {
        //prepare

        //act
        EndpointMatcher matcher = isReachable();

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

    @Test
    public void testRemotePort() throws Exception {
        //prepare
        String host = "localhost";
        int expectedPort = 123;

        //act
        TcpPort port = remotePort(host, expectedPort);

        //assert
        assertNotNull(port);
        assertEquals(expectedPort, port.getPortNumber());
        assertEquals(host + "/127.0.0.1:" + expectedPort, port.getSocketAddress().toString());

    }

}
