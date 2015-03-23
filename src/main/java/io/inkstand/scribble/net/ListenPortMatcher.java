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
 * limitations under the License
 */

package io.inkstand.scribble.net;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.assertTrue;

/**
 * Matcher for verifying a tcp port is available as server port.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public class ListenPortMatcher extends BaseMatcher<TcpPort> {

    @Override
    public boolean matches(final Object item) {

        if (!(item instanceof TcpPort)) {
            return false;
        }
        int port = ((TcpPort) item).getPortNumber();

        try (ServerSocket socket = new ServerSocket(port)) {
            assertTrue(socket.isBound());
        } catch (IOException e) { //NOSONAR
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(final Description description) {

        description.appendText("tcp port is available");
    }
}
