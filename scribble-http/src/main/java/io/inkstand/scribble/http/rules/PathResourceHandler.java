package io.inkstand.scribble.http.rules;

import java.nio.file.Files;
import java.nio.file.Path;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 * Created by Gerald Muecke on 11.12.2015.
 */
public class PathResourceHandler implements HttpHandler {

    private final Path path;

    public PathResourceHandler(final Path resourcePath) {
        this.path = resourcePath;
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        if(exchange.isInIoThread()){
            exchange.dispatch(this);
        } else{
            exchange.startBlocking();
            Files.copy(path, exchange.getOutputStream());

        }
    }
}
