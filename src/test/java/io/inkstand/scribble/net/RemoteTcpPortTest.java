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
