package io.inkstand.scribble.net.matchers;

import io.inkstand.scribble.net.TcpPort;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Matcher for verifying a tcp port is available as server port.
 */
public class ListenPortMatcher extends BaseMatcher<TcpPort> {

    private boolean serverPort = true;

    @Override
    public boolean matches(final Object item) {
        if(!(item instanceof TcpPort)){
            return false;
        }
        int port = ((TcpPort)item).getPortNumber();

        if(serverPort) {
            try (ServerSocket socket = new ServerSocket(port)){
                //do nothing, saul goodman
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("tcp port is available");
    }
}
