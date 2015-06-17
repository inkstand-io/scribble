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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

import java.net.ServerSocket;
import java.net.URL;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import io.inkstand.scribble.net.NetworkUtils;
import io.inkstand.scribble.net.ResourceAvailabilityMatcher;
import io.inkstand.scribble.net.TcpPort;

@RunWith(MockitoJUnitRunner.class)
public class ResourceAvailabilityMatcherTest {

    private static final Logger LOG = getLogger(ResourceAvailabilityMatcherTest.class);
    /**
     * The class under test
     */
    @InjectMocks
    private ResourceAvailabilityMatcher subject;

    @Mock
    private Description description;

    @Mock
    private TcpPort tcpPort;

    @Test
    public void testMatches_anyObject_false() throws Exception {
        assertFalse(subject.matches(new Object()));
    }

    @Test
    public void testMatches_null_false() throws Exception {

        assertFalse(subject.matches(null));
    }

    @Test
    public void testMatches_availableUrl_ok() throws Exception {
        //prepare
        URL url = getClass().getResource("ResourceAvailabilityMatcherTest_resource.txt");

        //act
        boolean result = subject.matches(url);

        //assert
        assertTrue(result);

    }

    @Test
    public void testMatches_unavailableUrl_fail() throws Exception {
        //prepare
        URL url = new URL("file:///notexisting.txt");

        //act
        boolean result = subject.matches(url);

        //assert
        assertFalse(result);

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
        verify(description).appendText("resource is available");
    }

    @Test
    public void testAssertThatIntegration() throws Exception {
        //prepare
        final URL url = getClass().getResource("ResourceAvailabilityMatcherTest_resource.txt");

        //act
        assertThat(url, subject);

        //assert

    }
}
