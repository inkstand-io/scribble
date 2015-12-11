package io.inkstand.scribble.http.rules;

/**
 * Class for fluently create http responses
 * Created by Gerald Muecke on 07.12.2015.
 */
public class GetResponseStubbing {

    private final HttpServer server;
    private String path;

    public GetResponseStubbing(HttpServer server){
        this.server = server;
    }

    public GetResponseStubbing respond(final String someContent) {
            server.addResource(this.path, someContent.getBytes());
        return this;
    }

    public GetResponseStubbing resource(final String resource) {
        this.path = resource;
        return this;
    }
}
