package io.inkstand.scribble.http.rules;

import io.inkstand.scribble.Builder;

/**
 * Created by Gerald Muecke on 07.12.2015.
 */
public class HttpServerBuilder extends Builder<HttpServer> {

    private int port  = -1;
    private String hostname;

    @Override
    public HttpServer build() {
        if(hostname == null && port < 0){
            return new HttpServer();
        }
        return new HttpServer(hostname, port);
    }

    public HttpServerBuilder port(final int port) {
        this.port = port;
        return this;
    }

    public HttpServerBuilder hostname(final String hostname) {
        this.hostname = hostname;
        return this;
    }
}
