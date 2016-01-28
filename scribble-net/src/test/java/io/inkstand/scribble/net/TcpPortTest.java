/*
 * Copyright 2015-2016 DevCon5 GmbH, info@devcon5.ch
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.junit.Test;

public class TcpPortTest {

    @Test
    public void testGetPortNumber() throws Exception {

        //prepare
        int expected = 123;

        //act
        TcpPort port = new TcpPort(expected);


        //assert
        assertEquals(expected,port.getPortNumber() );
    }

    @Test
    public void testGetSocketAddress() throws Exception {
        //prepare
        int portNumber = 123;
        TcpPort port = new TcpPort(portNumber);

        //act
        SocketAddress addr = port.getSocketAddress();

        //assert
        assertNotNull(addr);
        assumeTrue(addr instanceof InetSocketAddress);
        assertEquals(portNumber, ((InetSocketAddress)addr).getPort());

    }

    @Test
    public void testToString() throws Exception {
        //prepare
        TcpPort port = new TcpPort(123);

        //act
        String toString = port.toString();

        //assert
        assertEquals("tcp:123", toString);

    }
}
