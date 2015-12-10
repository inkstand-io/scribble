package io.inkstand.scribble.http.rules;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.inkstand.scribble.Builder;
import io.inkstand.scribble.net.NetworkUtils;
import io.inkstand.scribble.rules.TemporaryFile;
import io.inkstand.scribble.util.CallStack;
import io.inkstand.scribble.util.ResourceResolver;

/**
 * A Builder for creating an embedded HTTP server. Hostname and port can be optionally specified and which resources
 * the server should host.
 * Created by Gerald Muecke on 07.12.2015.
 */
public class HttpServerBuilder extends Builder<HttpServer> {

    private int port  = -1;
    private String hostname = "localhost";
    private ResourceResolver resolver = new ResourceResolver(true);
    private Map<String, Object> resources = new HashMap<>();

    @Override
    public HttpServer build() {
        int port = this.port;
        if(port < 0){
            port = NetworkUtils.findAvailablePort();
        }
        return new HttpServer(hostname, port, resources);
    }

    /**
     * Sets the port of the http server. The server will use a random, available port, if no port is specified.
     * @param port
     *  the port the http server will accept incoming requests.
     * @return
     *  this builder
     */
    public HttpServerBuilder port(final int port) {
        this.port = port;
        return this;
    }

    /**
     * Sets the hostnam for the http server. The server will always run on localhost but may have a different hostname.
     * @param hostname
     *  the hostname of the http server.
     * @return
     *  this builder
     */
    public HttpServerBuilder hostname(final String hostname) {
        this.hostname = hostname;
        return this;
    }

    /**
     * Defines a ZIP resource on the classpath that provides the static content the server should host.
     * @param contextRoot
     *  the root path to the content
     * @param zipContent
     *  the name of the classpath resource denoting a zip file containing the content. The paht may be absolute or
     *  relative to the caller of the method.
     * @return
     *  this builder
     */
    public HttpServerBuilder contentFromZip(String contextRoot, String zipContent){
        URL resource = resolver.resolve(zipContent,CallStack.getCallerClass());
        resources.put(contextRoot, resource);
        return this;
    }

    /**
     * Defines a ZIP resource that is dynamically created for the test using the {@link io.inkstand.scribble.rules.TemporaryFile}
     * rule.
     * @param contextRoot
     *  the root path to the content
     * @param zipContent
     *  the rule that creates the temporary zip file containing the resource to be hosted by the rule
     * @return
     *  this builder
     */
    public HttpServerBuilder contentFromZip(String contextRoot, TemporaryFile zipContent){
        resources.put(contextRoot, zipContent);
        return this;
    }
}
