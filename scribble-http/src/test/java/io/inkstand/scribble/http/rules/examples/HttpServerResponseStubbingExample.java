package io.inkstand.scribble.http.rules.examples;

import static org.junit.Assert.assertEquals;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
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
        server.onGet("/index.html").respond("someContent");

        //act
        try (final WebClient webClient = new WebClient()) {

            final TextPage page = webClient.getPage(server.getBaseUrl() + "/index.html");
            final String pageAsText = page.getContent();

            //assert
            assertEquals("someContent", pageAsText);
        }

    }
}
