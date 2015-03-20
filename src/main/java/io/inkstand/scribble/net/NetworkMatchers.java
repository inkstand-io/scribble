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

import io.inkstand.scribble.net.matchers.EndpointMatcher;
import io.inkstand.scribble.net.matchers.ListenPortMatcher;

/**
 * Created by Gerald M&uuml;cke on 11.03.2015.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public class NetworkMatchers {

    /**
     * Matcher to verify if a {@link io.inkstand.scribble.net.TcpPort} is available to be used as server port.
     *
     * @return a matcher to verify the avilability of a port
     */
    public static ListenPortMatcher isAvailable() {

        return new ListenPortMatcher();
    }

    /**
     * Matcher to verify if a {@link io.inkstand.scribble.net.TcpPort} is available to be used as server port.
     *
     * @return a matcher to verify the avilability of a port
     */
    public static EndpointMatcher isReachable() {

        return new EndpointMatcher();
    }

    /**
     * Creates a type-safe tcp port to be verified using matchers
     *
     * @param port
     *         the tcp port number to be wrapped
     *
     * @return a TcpPort instance describing the tcp port
     */
    public static TcpPort port(int port) {

        return new TcpPort(port);
    }

    public static TcpPort remotePort(String hostname, int port){
        return new RemoteTcpPort(hostname, port);
    }

}
