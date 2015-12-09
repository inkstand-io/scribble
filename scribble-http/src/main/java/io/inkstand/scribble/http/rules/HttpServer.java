package io.inkstand.scribble.http.rules;

import static java.nio.file.FileSystems.newFileSystem;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.util.Collections;
import java.util.Map;

import io.inkstand.scribble.net.NetworkUtils;
import io.inkstand.scribble.rules.ExternalResource;
import io.inkstand.scribble.rules.TemporaryZipFile;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;
import org.slf4j.Logger;

/**
 * Created by Gerald Muecke on 04.12.2015.
 */
public class HttpServer extends ExternalResource {

    private static final Logger LOG = getLogger(HttpServer.class);

    private final String hostname;
    private final int port;
    private final Map<String, Object> resources;
    private Undertow server;

    public HttpServer() {

        this("localhost", NetworkUtils.findAvailablePort());
    }

    public HttpServer(String hostname, int port) {

        this(hostname, port, Collections.EMPTY_MAP);
    }

    public HttpServer(final String hostname, final int port, final Map<String, Object> resources) {

        this.hostname = hostname;
        this.port = port;
        this.resources = resources;
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

        LOG.info("Crating http server {}:{}", getHostname(), getPort());
        final PathHandler pathHandler = new PathHandler();
        for (Map.Entry<String, Object> entry : this.resources.entrySet()) {
            final String path = entry.getKey();
            final Object resource = entry.getValue();
            if (resource instanceof TemporaryZipFile) {
                final ResourceHandler handler = createZipResourceHandler((TemporaryZipFile) resource);
                pathHandler.addPrefixPath(path, handler);
            }
        }
        this.server = Undertow.builder().addHttpListener(this.port, this.hostname).setHandler(pathHandler).build();
        LOG.info("Starting HTTP server");
        this.server.start();
        LOG.info("HTTP Server running");
    }

    private ResourceHandler createZipResourceHandler(final TemporaryZipFile resource) throws IOException {

        final File zipFile = resource.getFile();
        final FileSystem fs = newFileSystem(URI.create("jar:" + zipFile.toURI()),
                                            Collections.<String, Object>emptyMap());
        final ResourceManager resMgr = new FileSystemResourceManager(fs);
        return new ResourceHandler(resMgr);
    }

    @Override
    protected void after() {

        LOG.info("Stopping HTTP server");
        this.server.stop();
        LOG.info("HTTP Server stopped");
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

    public URL getBaseUrl() {

        try {
            return new URL("http", getHostname(), getPort(), "/");
        } catch (MalformedURLException e) {
            throw new AssertionError("Invalid base URL", e);
        }
    }

}
