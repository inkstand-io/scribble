package io.inkstand.scribble.http.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import io.undertow.server.handlers.resource.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Gerald Muecke on 09.12.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class FileSystemResourceManagerTest {

    /**
     * The class under test
     */
    @InjectMocks
    private FileSystemResourceManager subject;

    @Mock
    private FileSystem fileSystem;

    @Mock
    private Path path;

    @Test
    public void testGetResource() throws Exception {
        //prepare
        when(path.getFileName()).thenReturn(path);
        when(path.toString()).thenReturn("TEST");
        when(fileSystem.getPath("test")).thenReturn(path);

        //act
        Resource res = subject.getResource("test");

        //assert
        assertNotNull(res);
        assertEquals("TEST", res.getName());
    }

    @Test
    public void testIsResourceChangeListenerSupported() throws Exception {

        //prepare

        //act
        assertFalse(subject.isResourceChangeListenerSupported());

        //assert
    }

    @Test
    public void testClose() throws Exception {

        //prepare

        //act
        subject.close();

        //assert
        verify(fileSystem).close();
    }
}
