/*
 * Copyright 2015-2016 DevCon5 GmbH, info@devcon5.ch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.inkstand.scribble.http.rules;

import static java.nio.file.FileSystems.newFileSystem;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import io.inkstand.scribble.net.NetworkUtils;
import io.inkstand.scribble.rules.ExternalResource;
import io.inkstand.scribble.rules.TemporaryFile;
import io.inkstand.scribble.rules.TemporaryZipFile;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;

/**
 * Server rule that starts an embedded http server, that serves static content. The server may be instantiated directly
 * or by using the {@link HttpServerBuilder}. Content may be provided as generated zip, using the {@link io.inkstand
 * .scribble.rules.TemporaryFile} rule or as predefinded zip from the classpath.
 * Created by Gerald Muecke on 04.12.2015.
 */
public class HttpServer extends ExternalResource {

    private static final Logger LOG = getLogger(HttpServer.class);

    private final String hostname;
    private final int port;
    private final Map<String, Object> resources;
    private Undertow server;
    private PathHandler pathHandler;

    /**
     * Creates a http server on localhost, running on an available tcp port. The server won't server any static content.
     */
    public HttpServer() {

        this("localhost", NetworkUtils.findAvailablePort());
    }

    /**
     * Creates a http server for the specified hostname and tcp port. The server won't server any static content.
     * @param hostname
     *  the hostname the server listens on.
     * @param port
     *  the tcp port the server is accepting incoming connections.
     */
    public HttpServer(String hostname, int port) {

        this(hostname, port, Collections.<String, Object>emptyMap());
    }

    /**
     * Creates a http server for the specified hostname and tcp port. The server serves the content on the context paths
     * provided in the resource map.
     * @param hostname
     *  the hostname the server listens on.
     * @param port
     *  the tcp port the server is accepting incoming connections.
     * @param resources
     */
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

        LOG.info("Creating http server {}:{}", getHostname(), getPort());
        this.pathHandler = new PathHandler();
        for (Map.Entry<String, Object> entry : this.resources.entrySet()) {
            final String path = entry.getKey();
            final Object resource = entry.getValue();
            addResource(path, resource);
        }
        this.server = Undertow.builder().addHttpListener(this.port, this.hostname).setHandler(pathHandler).build();
        LOG.info("Starting HTTP server");
        this.server.start();
        LOG.info("HTTP Server running");
    }

    /**
     * Adds a resource to the path handler under the specified context path. Resources may be of various types:
     * <ul>
     *     <li>{@link io.inkstand.scribble.rules.TemporaryZipFile} - zip file that is created for test execution.
     *     All files in the zip are hosted on the specified path as root folder.
     *     </li>
     *     <li>{@link java.net.URL} pointing to a zip resource, same as the TemporaryZipFile but the zip has to
     *     be predined</li>
     * </ul>
     * @param path
     *  the path to the resource
     * @param resource
     *  a resource to add. The method can handle various types of resources.
     * @throws IOException
     * @throws URISyntaxException
     */
    void addResource(final String path, final Object resource) {

        try {
            if (resource instanceof TemporaryZipFile) {
                final URL url = ((TemporaryZipFile) resource).getFile().toURI().toURL();
                this.pathHandler.addPrefixPath(path, createZipResourceHandler(url));
            } else if (resource instanceof TemporaryFolder) {
                final Path resourcePath = ((TemporaryFolder) resource).getRoot().toPath();
                this.pathHandler.addPrefixPath(path, new ResourceHandler(new PathResourceManager(resourcePath, 1024)));
            } else if (resource instanceof TemporaryFile) {
                final Path resourcePath = ((TemporaryFile) resource).getFile().toPath();
                this.pathHandler.addExactPath(path, new PathResourceHandler(resourcePath));
            } else if (resource instanceof URL) {
                final URL url = (URL) resource;
                if (url.getPath().endsWith(".zip")) {
                    this.pathHandler.addPrefixPath(path, createZipResourceHandler(url));
                } else {
                    this.pathHandler.addExactPath(path, new UrlResourceHandler(url));
                }
            } else if (resource instanceof byte[]) {
                this.pathHandler.addExactPath(path, new ByteArrayHandler((byte[]) resource));
            }
        }catch(IOException e){
            throw new AssertionError("Could not add Resource", e);
        }
    }

    /**
     * Creates the resource handle for a zip file, specified by the URL.
     * @param zipFile
     *  url to a zip file
     * @return
     *  the resource handler to handle requests to files in the zip
     * @throws IOException
     */
    private ResourceHandler createZipResourceHandler(final URL zipFile) throws IOException {

        final FileSystem fileSystem = newFileSystem(URI.create("jar:" + zipFile), Collections.<String, Object>emptyMap());
        final ResourceManager resMgr = new FileSystemResourceManager(fileSystem);
        return new ResourceHandler(resMgr);
    }

    @Override
    protected void after() {

        LOG.info("Stopping HTTP server");
        this.server.stop();
        LOG.info("HTTP Server stopped");
    }

    /**
     * Provides the hostname of the http server. The server always runs on localhost, but possibly under another
     * alias of it.
     * @return
     *  the hostname of the server
     */
    public String getHostname() {

        return hostname;
    }

    /**
     * The tcp port the server accepts incoming requests.
     * @return
     *  the tcp port.
     */
    public int getPort() {

        return port;
    }

    /**
     * Entry point for fluently defining response for http GET requests.
     * @param resource
     *  the resource that should be retrieved via http GET.
     * @return
     *  a stubbing defining what to respond on a get request on the specified resource.
     */
    public GetResponseStubbing onGet(final String resource) {

        return new GetResponseStubbing(this).resource(resource);
    }

    /**
     * Creates an URL to the root path of the http server, i.e. 'http://localhost:8080/'
     * @return
     *  the base URL to the http server
     */
    public URL getBaseUrl() {

        try {
            return new URL("http", getHostname(), getPort(), "/");
        } catch (MalformedURLException e) {
            throw new AssertionError("Invalid base URL", e);
        }
    }

}
