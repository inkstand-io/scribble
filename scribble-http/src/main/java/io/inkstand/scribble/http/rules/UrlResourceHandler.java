package io.inkstand.scribble.http.rules;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.io.IOUtils;

/**
 * Created by Gerald Muecke on 11.12.2015.
 */
public class UrlResourceHandler implements HttpHandler {

    private final URL resource;

    public UrlResourceHandler(URL resource ){
        this.resource = resource;
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        if(exchange.isInIoThread()){
            exchange.dispatch(this);
        } else {
            try(InputStream is = resource.openStream()){
                exchange.startBlocking();
                final OutputStream os = exchange.getOutputStream();
                IOUtils.copy(is, os);
            }
        }
    }
}
