package io.inkstand.scribble.http.rules;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.nio.ByteBuffer;

import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.ServerConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Gerald Muecke on 14.12.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class UrlResourceHandlerTest {

    @Mock
    private ServerConnection serverConnection;

    @Mock
    private ByteBufferPool byteBufferPool;

    @Mock
    private PooledByteBuffer pooledByteBuffer;

    private static final URL resource = UrlResourceHandlerTest.class.getResource("UrlResourceHandlerTest_test.txt");

    /**
     * The class under test
     */
    private UrlResourceHandler subject = new UrlResourceHandler(resource);

    @Test
    public void testHandleRequest() throws Exception {
        //prepare
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        final HttpServerExchange exchange = new HttpServerExchange(serverConnection);
        when(serverConnection.getByteBufferPool()).thenReturn(byteBufferPool);
        when(byteBufferPool.allocate()).thenReturn(pooledByteBuffer);
        when(pooledByteBuffer.getBuffer()).thenReturn(buffer);

        //act
        subject.handleRequest(exchange);

        //assert
        buffer.rewind();
        byte[] data = new byte[4];
        buffer.get(data);
        assertEquals("test", new String(data));
    }
}
