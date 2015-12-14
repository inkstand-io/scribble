package io.inkstand.scribble.http.rules;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;

/**
 * A {@link ResourceHttpHandler} that serves the content of a URL. It's intended for serving classpath resources
 * referenced by URL but it may also serves resource from the local filesystem or from a network resource.
 * Created by Gerald Muecke on 11.12.2015.
 */
public class UrlResourceHandler extends ResourceHttpHandler{

    private final URL resource;

    public UrlResourceHandler(URL resource ){
        this.resource = resource;
    }

    @Override
    protected void writeResource(final OutputStream outputStream) throws IOException {
            try(InputStream is = resource.openStream()){
                IOUtils.copy(is, outputStream);
            }
    }
}
