package io.inkstand.scribble.net.matchers;

import io.inkstand.scribble.matchers.TimeoutSupport;
import io.inkstand.scribble.net.TcpPort;
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
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("Port reachable");
    }
}
