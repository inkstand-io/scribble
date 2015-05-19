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

package io.inkstand.scribble.net.matchers;

import io.inkstand.scribble.net.ListenPortMatcher;
import io.inkstand.scribble.net.NetworkUtils;
import io.inkstand.scribble.net.TcpPort;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.net.ServerSocket;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

@RunWith(MockitoJUnitRunner.class)
public class ListenPortMatcherTest {

    private static final Logger LOG = getLogger(ListenPortMatcherTest.class);
    /**
     * The class under test
     */
    @InjectMocks
    private ListenPortMatcher subject;

    @Mock
    private Description description;

    @Mock
    private TcpPort tcpPort;

    final Random rand = new Random();
    /**
     * Amount of retries before the test is ignored
     */
    public static final int MAX_RETRY_COUNT = 100;

    @Test
    public void testMatches_nonTcpPort() throws Exception {
        assertFalse(subject.matches(new Object()));
    }

    @Test
    public void testMatches_portAvailable_true() throws Exception {

        //prepare
        //first, find a random port that is free

        int randomPort = NetworkUtils.findAvailablePort();
        LOG.debug("Port {} is available", randomPort);
        when(tcpPort.getPortNumber()).thenReturn(randomPort);

        //act
        boolean matches = subject.matches(tcpPort);

        //assert
        assertTrue(matches);

    }

    @Test
    public void testMatches_portNotAvailable_false() throws Exception {

        //prepare
        //first, find a random port that is free

        int randomPort = NetworkUtils.findAvailablePort();
        LOG.debug("Port {} is available", randomPort);
        when(tcpPort.getPortNumber()).thenReturn(randomPort);

        //act
        boolean matches;
        //open a server socket to make the port unavailable
        try(ServerSocket socket = new ServerSocket(randomPort)) {
            matches = subject.matches(tcpPort);
        }

        //assert
        assertFalse(matches);

    }


    @Test
    public void testDescribeTo() throws Exception {
        //prepare

        //act
        subject.describeTo(description);


        //assert
        verify(description).appendText("tcp port is available");
    }
}
