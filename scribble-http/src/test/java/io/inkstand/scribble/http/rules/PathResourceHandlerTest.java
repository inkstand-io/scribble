package io.inkstand.scribble.http.rules;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;

import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.ServerConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Gerald Muecke on 11.12.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class PathResourceHandlerTest {

    @Mock
    private ServerConnection serverConnection;

    @Mock
    private ByteBufferPool byteBufferPool;

    @Mock
    private PooledByteBuffer pooledByteBuffer;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Path path;

    @Mock
    private FileSystemProvider provider;

    /**
     * The class under test
     */
    @InjectMocks
    private PathResourceHandler subject;

    @Test
    public void testHandleRequest() throws Exception {
        //prepare
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        final HttpServerExchange exchange = new HttpServerExchange(serverConnection);
        when(serverConnection.getByteBufferPool()).thenReturn(byteBufferPool);
        when(byteBufferPool.allocate()).thenReturn(pooledByteBuffer);
        when(pooledByteBuffer.getBuffer()).thenReturn(buffer);
        when(path.getFileSystem().provider()).thenReturn(provider);
        when(provider.newInputStream(path)).thenReturn(new ByteArrayInputStream("test".getBytes()));

        //act
        subject.handleRequest(exchange);

        //assert
        buffer.rewind();
        byte[] data = new byte[4];
        buffer.get(data);
        assertEquals("test", new String(data));
    }
}
