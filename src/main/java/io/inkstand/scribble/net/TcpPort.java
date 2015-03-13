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

    private int portNumber;

    public TcpPort(final int portNumber) {

        this.portNumber = portNumber;
    }

    public int getPortNumber() {

        return portNumber;
    }

    public SocketAddress getSocketAddress() {
        return new InetSocketAddress(portNumber);
    }
}
