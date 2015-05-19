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

package io.inkstand.scribble.rules.ldap;

import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.InstanceLayout;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.factory.DirectoryServiceFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetSocketAddress;
import java.net.Socket;

import static io.inkstand.scribble.net.NetworkUtils.findAvailablePort;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DirectoryServerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private DirectoryService directoryService;

    @Mock
    private Description description;

    @Mock
    private Directory directory;

    /**
     * The class under test
     */
    @InjectMocks
    private DirectoryServer subject;

    @Before
    public void setUp() throws Exception {

        final DirectoryServiceFactory factory = new DefaultDirectoryServiceFactory();
        factory.init("scribble-test");
        directoryService = factory.getDirectoryService();
        directoryService.setInstanceLayout(new InstanceLayout(this.folder.getRoot()));
        directoryService.startup();
        when(directory.getDirectoryService()).thenReturn(directoryService);
    }

    @After
    public void tearDown() throws Exception {

        if (directoryService != null) {
            directoryService.shutdown();
        }
        if(subject != null && subject.getLdapServer() != null) {
            subject.getLdapServer().stop();
        }
    }

    @Test
    public void testApply() throws Throwable {
        //prepare
        final int port = findAvailablePort();
        subject.setTcpPort(port);
        Statement base = spy(new Statement(){

            @Override
            public void evaluate() throws Throwable {

                try(Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(port));
                } catch (Exception e) {
                    throw new AssertionError("Could not connect to ldap server running on tcp port " + port, e);
                }
                
            }
        });

        //act
        final Statement statement = subject.apply(base, description);
        

        //assert
        assertNotNull(statement);
        statement.evaluate();
        verify(base).evaluate();
    }

    @Test
    public void testStartAndStopServer() throws Exception {
        //prepare
        int port = findAvailablePort();
        subject.setTcpPort(port);

        //act
        boolean started, stopped;
        try {
            subject.startServer();
            started = subject.getLdapServer().isStarted();
        } finally {
            subject.shutdownServer();
            stopped = !subject.getLdapServer().isStarted();

        }

        //assert
        assertTrue("Server was expected to be started",started);
        assertTrue("Server was expected to be stopped", stopped);


    }

    @Test
    public void testSubjectGetTcpPort() throws Exception {
        //prepare
        int expected = 12345;

        //act
        subject.setTcpPort(expected);
        int actual = subject.getTcpPort();

        //assert
        assertEquals(expected, actual);
    }


}
