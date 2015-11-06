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

package io.inkstand.scribble.jcr.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import java.net.URL;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import io.inkstand.scribble.JCRAssert;
import io.inkstand.scribble.Scribble;

/**
 * Created by Gerald on 08.06.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class ContentLoaderTest {

    private static final Logger LOG = getLogger(ContentLoaderTest.class);

    @Rule
    public InMemoryContentRepository repository = Scribble.newInMemoryContentRepository().build();

    @Mock
    private Description description;

    /**
     * The class under test
     */
    private ContentLoader subject;

    @Before
    public void setUp() throws Exception {

        subject = new ContentLoader(repository);

    }

    @Test
    public void testApply_withInitialContentDefinition() throws Throwable {

        //prepare
        URL contentResource = getClass().getResource("ContentLoaderTest_inkstandJcrImport_v1-0.xml");
        subject.setContentDefinition(contentResource);

        //act
        subject.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {

                //assert
                Session session = repository.login();
                JCRAssert.assertStringPropertyEquals(session.getNode("/root"), "rules:title", "TestTitle");

            }
        }, description).evaluate();
    }

    @Test
    public void testApply_withoutInitialContentDefinition() throws Throwable {

        //prepare

        //act
        subject.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {

                //assert
                Session session = repository.login();

                NodeIterator nit = session.getRootNode().getNodes();
                assertEquals(1, nit.getSize());
                assertEquals("/rules:system", nit.nextNode().getPath());

            }
        }, description).evaluate();
    }

    @Test
    public void testLoadContent_noInitialContent() throws Throwable {

        //prepare
        final URL contentResource = getClass().getResource("ContentLoaderTest_inkstandJcrImport_v1-0.xml");

        //act
        subject.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {

                //act
                Node root = subject.loadContent(contentResource);

                //assert
                assertNotNull(root);
                assertEquals("/root", root.getPath());
            }
        }, description).evaluate();

    }
}
