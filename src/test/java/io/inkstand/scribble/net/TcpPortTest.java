package io.inkstand.scribble.net;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
