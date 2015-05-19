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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

@RunWith(MockitoJUnitRunner.class)
public class RemoteTcpPortTest {


    @Test
    public void testRemoteTcpPort_getHostname() throws Exception {

        //prepare
        String hostname = "local";
        int port = 123;

        //act
        RemoteTcpPort tcp = new RemoteTcpPort(hostname,port);

        //assert
        assertEquals(hostname, tcp.getHostname() );
        assertEquals(port, tcp.getPortNumber());
    }

    @Test
    public void testGetSocketAddress() throws Exception {
        //prepare
        String hostname = "localhost";
        int portNumber = 123;
        RemoteTcpPort port = new RemoteTcpPort(hostname, portNumber);

        //act
        SocketAddress addr = port.getSocketAddress();

        //assert
        assertNotNull(addr);
        assumeTrue(addr instanceof InetSocketAddress);
        assertEquals(hostname, ((InetSocketAddress)addr).getHostName());
        assertEquals(portNumber, ((InetSocketAddress)addr).getPort());

    }
}
