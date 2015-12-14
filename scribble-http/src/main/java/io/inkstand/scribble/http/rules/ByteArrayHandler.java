package io.inkstand.scribble.http.rules;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Handler for serving data from a byte array.
 * Created by Gerald Muecke on 11.12.2015.
 */
public class ByteArrayHandler extends ResourceHttpHandler {

    private final byte[] data;

    /**
     * Creates a new byte array handler.
     * @param resource
     *  the data to be served by this handler. The array is copied so that modifications to it won't affect the data
     *  served by this handler.
     */
    public ByteArrayHandler(final byte[] resource) {
        this.data = new byte[resource.length];
        System.arraycopy(resource, 0, this.data, 0, resource.length);
    }

    @Override
    protected void writeResource(final OutputStream outputStream) throws IOException {
        outputStream.write(data, 0, data.length);
    }
}
