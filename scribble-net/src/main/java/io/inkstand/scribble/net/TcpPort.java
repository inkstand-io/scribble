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

package io.inkstand.scribble.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Descriptor for a tcp port that provides type-safety and can be used in combination with {@link org.hamcrest
 * .Matcher}s
 * <p/>
 * Created by Gerald M&uuml;cke on 11.03.2015.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public class TcpPort {

    private final int portNumber;

    public TcpPort(final int portNumber) {

        this.portNumber = portNumber;
    }

    public SocketAddress getSocketAddress() {

        return new InetSocketAddress(this.getPortNumber());
    }

    /**
     * An integer value between 0 and 65536 (both excluding) representing the tcp port number.
     *
     * @return the tcp port number
     */
    public int getPortNumber() {

        return this.portNumber;
    }

    @Override
    public String toString() {

        return "tcp:" + this.getPortNumber();
    }
}
