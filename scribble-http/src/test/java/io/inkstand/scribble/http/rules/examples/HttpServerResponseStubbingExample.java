package io.inkstand.scribble.http.rules.examples;

import io.inkstand.scribble.http.rules.HttpServer;
import io.inkstand.scribble.http.rules.HttpServerBuilder;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Gerald Muecke on 04.12.2015.
 */
public class HttpServerResponseStubbingExample {


    @Rule
    public HttpServer server = new HttpServerBuilder().port(55555).build();

    @Test
    public void testHttpServerGet() throws Exception {
        //prepare
        server.onGet("some URI").respond("someContent");

        //act

        //assert

    }
}
