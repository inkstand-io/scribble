package io.inkstand.scribble.http.rules;

/**
 * Class for fluently create http get responses.
 * Created by Gerald Muecke on 07.12.2015.
 */
public class GetResponseStubbing {

    private final HttpServer server;
    private String path;

    /**
     * Creates a get response stubbing for the given http server.
     * @param server
     */
    GetResponseStubbing(HttpServer server){
        this.server = server;
    }

    /**
     * Defines the content body of the response
     * @param someContent
     *  the content as string that should be responded
     * @return
     *  this stubbing
     *
     */
    public GetResponseStubbing respond(final String someContent) {
            server.addResource(this.path, someContent.getBytes());
        return this;
    }

    /**
     * Sets the resource that should be requested via GET, as in <pre>
     *     GET /pathToResource HTTP/1.1
     * </pre>
     * @param resource
     *  the path to the resource
     * @return
     *  this stubbing
     */
    GetResponseStubbing resource(final String resource) {
        this.path = resource;
        return this;
    }
}
