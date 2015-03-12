package io.inkstand.scribble.net;

import io.inkstand.scribble.matchers.ListenPortMatcher;

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

}
