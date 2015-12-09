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

    public HttpServerBuilder port(final int port) {
        this.port = port;
        return this;
    }

    public HttpServerBuilder hostname(final String hostname) {
        this.hostname = hostname;
        return this;
    }

    public HttpServerBuilder contentFromZip(String contextRoot, String zipContent){
        URL resource = resolver.resolve(zipContent,CallStack.getCallerClass());
        resources.put(contextRoot, resource);
        return this;
    }

    public HttpServerBuilder contentFromZip(String contextRoot, TemporaryFile zipContent){
        resources.put(contextRoot, zipContent);
        return this;
    }
}
