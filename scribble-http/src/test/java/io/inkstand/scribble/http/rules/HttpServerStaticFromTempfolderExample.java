package io.inkstand.scribble.http.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.nio.file.Files;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TemporaryFolder;

/**
 * Created by Gerald Muecke on 10.12.2015.
 */
public class HttpServerStaticFromTempfolderExample {

    public TemporaryFolder folder = new TemporaryFolder();
    public HttpServer server = new HttpServerBuilder().contentFrom("/", folder).build();
    @Rule
    public RuleChain rule = RuleChain.outerRule(folder).around(server);

    @Test
    public void testHttpServerGet() throws Exception {
        //prepare
        //create a file in the tempfolder
        try(InputStream is = HttpServerStaticFromTempfolderExample.class.getResourceAsStream("index.html")) {
            Files.copy(is, folder.getRoot().toPath().resolve("index.html"));
        }

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
