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

import io.inkstand.scribble.matchers.TimeoutSupport;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a> on 3/12/2015
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public class EndpointMatcher extends BaseMatcher<TcpPort> implements TimeoutSupport{

    private long timeout = 0;


    @Override
    public EndpointMatcher within(long duration, TimeUnit timeUnit) {
        timeout = timeUnit.toMillis(duration);
        return this;
    }


    @Override
    public boolean matches(final Object item) {

        if(!(item instanceof TcpPort)){
            return false;
        }

        SocketAddress addr = ((TcpPort)item).getSocketAddress();

        try(Socket socket = new Socket()){
            socket.connect(addr, (int) timeout);
            return true;
        } catch (IOException e) { //NOSONAR
            return false;
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("Port reachable");
    }
}
