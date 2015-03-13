package io.inkstand.scribble.net;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

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
}
