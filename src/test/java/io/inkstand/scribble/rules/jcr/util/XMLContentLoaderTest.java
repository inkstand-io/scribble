/*
 * Copyright 2015 Gerald Muecke, gerald.muecke@gmail.com
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

package io.inkstand.scribble.rules.jcr.util;

import static io.inkstand.scribble.JCRAssert.assertMixinNodeType;
import static io.inkstand.scribble.JCRAssert.assertNodeExistByPath;
import static io.inkstand.scribble.JCRAssert.assertPrimaryNodeType;
import static io.inkstand.scribble.JCRAssert.assertStringPropertyEquals;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import io.inkstand.scribble.Scribble;
import io.inkstand.scribble.rules.jcr.ContentRepository;

public class XMLContentLoaderTest {

    @ClassRule
    public static final ContentRepository repository = Scribble.newTempFolder()
                                                               .aroundInMemoryContentRepository()
                                                               .build();

    @Rule
    public TemporaryFolder folder = Scribble.newTempFolder().build();

    private XMLContentLoader subject;

    @Before
    public void setUp() throws Exception {

        subject = new XMLContentLoader();
    }

    @Test
    public void testLoadContent_notValidating_validResource() throws Exception {
        // prepare
        final URL resource = getClass().getResource("XMLContentLoaderTest_inkstandJcrImport_v1-0.xml");
        final Session actSession = repository.getAdminSession();
        // act
        subject.loadContent(actSession, resource);
        // assert
        final Session verifySession = repository.getRepository().login();
        verifySession.refresh(true);
        assertNodeExistByPath(verifySession, "/root");
        final Node root = verifySession.getNode("/root");
        assertPrimaryNodeType(root, "nt:unstructured");
        assertMixinNodeType(root, "mix:title");
        assertStringPropertyEquals(root, "jcr:title", "TestTitle");
    }

    @Test
    public void testLoadContent_notValidating_invalidResource() throws Exception {
        // prepare
        final URL resource = getClass().getResource("XMLContentLoaderTest_inkstandJcrImport_v1-0_invalid.xml");
        final Session actSession = repository.getAdminSession();
        // act
        subject.loadContent(actSession, resource);
        // assert
        final Session verifySession = repository.getRepository().login();
        verifySession.refresh(true);
        assertNodeExistByPath(verifySession, "/root");
        final Node root = verifySession.getNode("/root");
        assertPrimaryNodeType(root, "nt:unstructured");
        assertMixinNodeType(root, "mix:title");
        assertStringPropertyEquals(root, "jcr:title", "TestTitle");
    }

    @Test
    public void testLoadContent_validating_validResource() throws Exception {
        // prepare
        final URL resource = getClass().getResource("XMLContentLoaderTest_inkstandJcrImport_v1-0.xml");
        final Session actSession = repository.getAdminSession();
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = schemaFactory.newSchema(getClass().getResource("inkstandJcrImport_v1-0.xsd"));
        // act
        subject.setSchema(schema);
        subject.loadContent(actSession, resource);
        // assert
        final Session verifySession = repository.getRepository().login();
        verifySession.refresh(true);
        assertNodeExistByPath(verifySession, "/root");
        final Node root = verifySession.getNode("/root");
        assertPrimaryNodeType(root, "nt:unstructured");
        assertMixinNodeType(root, "mix:title");
        assertStringPropertyEquals(root, "jcr:title", "TestTitle");
    }

    @Test(expected = AssertionError.class)
    public void testLoadContent_validating_invalidResource() throws Exception {
        // prepare
        final URL resource = getClass().getResource("XMLContentLoaderTest_inkstandJcrImport_v1-0_invalid.xml");
        final Session actSession = repository.getAdminSession();
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = schemaFactory.newSchema(getClass().getResource("inkstandJcrImport_v1-0.xsd"));
        // act
        subject.setSchema(schema);
        subject.loadContent(actSession, resource);
    }

    /**
     * This tests implements an exploit to the XML External Entity Attack {@see http://www.ws-attacks.org/index.php/XML_Entity_Reference_Attack}.
     * The attack targets a file in the filesystem containing a secret, i.e. a password, configurations, etc. The
     * attacking file defines an entity that resolves to the file containing the secret. The entity (&amp;xxe;) is used
     * in the xml file and will be resolved to provide the title of the test node. If the code is not vulnerable, the
     * attack will fail.
     *
     * @throws Throwable
     */
    @Test(expected = AssertionError.class)
    public void testLoadContent_ExternalitEntityAttack_notVulnerable() throws Throwable {
        //prepare
        //the attacked file containing the secret
        final File attackedFile = folder.newFile("attackedFile.txt");
        try (FileOutputStream fos = new FileOutputStream(attackedFile)) {
            //the lead-padding of 4-chars is ignored for some mysterious reasons...
            IOUtils.write("    secretContent", fos, Charset.forName("UTF-8"));
        }
        //as attacker file we use a template and replacing a %s placeholder with the url of the attacked file
        //in a real-world attack we would use a valuable target such as /etc/passwd
        final File attackerFile = folder.newFile("attackerFile.xml");

        //load the template file from the classpath
        try (InputStream is = getClass().getResourceAsStream(
                "XMLContentLoaderTest_inkstandJcrImport_v1-0_xxe-attack.xml");
             FileOutputStream fos = new FileOutputStream(attackerFile)) {

            final String attackerContent = prepareAttackerContent(is, attackedFile);
            IOUtils.write(attackerContent, fos);
        }
        final Session actSession = repository.getAdminSession();

        //act
        //when the code is not vulnerable, the following call will cause a runtime exception
        //as the dtd processing of external entities is not allowed.
        subject.loadContent(actSession, attackerFile.toURI().toURL());
    }

    private String prepareAttackerContent(final InputStream templateInputStream, final File attackedFile)
            throws IOException {

        final StringWriter writer = new StringWriter();
        IOUtils.copy(templateInputStream, writer, Charset.defaultCharset());
        return String.format(writer.toString(), attackedFile.toURI().toURL());
    }
}
