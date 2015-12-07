package io.inkstand.scribble.http.rules;

import io.inkstand.scribble.net.NetworkUtils;
import io.inkstand.scribble.rules.ExternalResource;
import io.undertow.Undertow;

/**
 * Created by Gerald Muecke on 04.12.2015.
 */
public class HttpServer extends ExternalResource{

    private final String hostname;
    private final int port;
    private Undertow server;

    public HttpServer(){
        this("localhost", NetworkUtils.findAvailablePort());
    }

    public HttpServer(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    protected void beforeClass() throws Throwable {

        before();
    }

    @Override
    protected void afterClass() {

        after();
    }

    @Override
    protected void before() throws Throwable {

        this.server = Undertow.builder().build();
        this.server.start();
    }

    @Override
    protected void after() {
        this.server.stop();
    }

    public String getHostname() {

        return hostname;
    }

    public int getPort() {

        return port;
    }

    public GetResponseStubbing onGet(final String resource) {

        return null;
    }
}
