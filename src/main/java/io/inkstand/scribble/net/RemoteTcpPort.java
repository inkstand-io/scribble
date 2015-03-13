package io.inkstand.scribble.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * A TCP port that describes a remote address and a port number.
 * Created by <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a> on 3/12/2015
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public class RemoteTcpPort extends TcpPort {

    private final String hostname;

    public RemoteTcpPort(final String hostname, final int port) {
        super(port);
        this.hostname = hostname;

    }

    /**
     * The hostname for the remote port
     * @return
     */
    public String getHostname() {

        return hostname;
    }

    @Override
    public SocketAddress getSocketAddress() {
        return new InetSocketAddress(getHostname(), getPortNumber());
    }
}
