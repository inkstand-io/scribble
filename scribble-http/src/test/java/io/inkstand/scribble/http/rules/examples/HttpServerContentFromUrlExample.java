package io.inkstand.scribble.http.rules.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.inkstand.scribble.http.rules.HttpServer;
import io.inkstand.scribble.http.rules.HttpServerBuilder;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Gerald Muecke on 04.12.2015.
 */
public class HttpServerContentFromUrlExample {

    private final URL resource = HttpServerContentFromUrlExample.class.getResource("index.html");

    @Rule
    public HttpServer server = new HttpServerBuilder().contentFrom("/index.html", resource).build();

    @Test
    public void testHttpServerGet() throws Exception {
        //prepare

        //act
        try (final WebClient webClient = new WebClient()) {

            final HtmlPage page = webClient.getPage(server.getBaseUrl() + "/index.html");
            final String pageAsXml = page.asXml();
            final String pageAsText = page.asText();

            //assert
            assertEquals("Test Content", page.getTitleText());
            assertTrue(pageAsXml.contains("<body>"));
            assertTrue(pageAsText.contains("Test Content Body"));
        }
    }
}
