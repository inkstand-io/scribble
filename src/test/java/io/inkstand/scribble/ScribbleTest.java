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

package io.inkstand.scribble;

import static io.inkstand.scribble.JCRAssert.assertNodeTypeExists;
import static org.junit.Assert.assertNotNull;

import javax.jcr.Session;
import java.net.URL;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.inkstand.scribble.inject.Injection;
import io.inkstand.scribble.rules.builder.InMemoryContentRepositoryBuilder;
import io.inkstand.scribble.rules.builder.JNDIContentRepositoryBuilder;
import io.inkstand.scribble.rules.builder.MockContentRepositoryBuilder;
import io.inkstand.scribble.rules.builder.StandaloneContentRepositoryBuilder;
import io.inkstand.scribble.rules.builder.TemporaryFolderBuilder;
import io.inkstand.scribble.rules.jcr.InMemoryContentRepository;
import io.inkstand.scribble.rules.jcr.StandaloneContentRepository;
import io.inkstand.scribble.rules.ldap.DirectoryServer;

/**
 * Created by Gerald on 18.05.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class ScribbleTest {

    @Mock
    private Description description;

    @Test
    public void testInject() throws Exception {

        //prepare
        String value = "123";

        //act
        Injection result = Scribble.inject(value);

        //assert
        assertNotNull(result);
    }

    @Test
    public void testNewTempFolder() throws Exception {

        //act
        TemporaryFolderBuilder result = Scribble.newTempFolder();

        //assert
        assertNotNull(result);
    }

    @Test
    public void testNewMockContentRepository() throws Exception {

        //act
        MockContentRepositoryBuilder result = Scribble.newMockContentRepository();

        //assert
        assertNotNull(result);
    }

    @Test
    public void testNewJNDIContextRepository() throws Exception {

        //act
        JNDIContentRepositoryBuilder result = Scribble.newJndiContentRepository();

        //assert
        assertNotNull(result);
    }

    @Test
    public void testNewInMemoryContentRepository() throws Exception {

        //act
        InMemoryContentRepositoryBuilder result = Scribble.newInMemoryContentRepository();

        //assert
        assertNotNull(result);
    }

    @Test
    public void testNewInMemoryContentRepository_withCnd() throws Throwable {

        //prepare
        final URL cndModel = getClass().getResource("ScribbleTest_testModel.cnd");

        //act
        //this is the actual line how the rule builder should be used in a test
        final InMemoryContentRepository result = Scribble.newInMemoryContentRepository().withNodeTypes(cndModel)
                                                    .build();

        //assert
        assertNotNull(result);
        result.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                final Session session = result.login("admin", "admin");
                assertNodeTypeExists(session, "test:testType");
            }
        }, description).evaluate();
    }

    @Test
    public void testNewStandaloneContentRepository() throws Exception {
        //act
        StandaloneContentRepositoryBuilder result = Scribble.newStandaloneContentRepository();

        //assert
        assertNotNull(result);
    }

    @Test
    public void testNewStandaloneContentRepository_withCnd() throws Throwable {

        //prepare
        final URL cndModel = getClass().getResource("ScribbleTest_testModel.cnd");

        //act
        //this is the actual line how the rule builder should be used in a test
        final StandaloneContentRepository result =
                Scribble.newStandaloneContentRepository().withNodeTypes(cndModel).build();

        //assert
        assertNotNull(result);

        result.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                final Session session = result.login("admin","admin");
                assertNodeTypeExists(session, "test:testType");
            }
        }, description).evaluate();
    }

    @Test
    public void testNewDirectoryServer() throws Exception {
        //prepare

        //act
        final DirectoryServer ds = Scribble.newDirectoryServer().build();

        //assert
        assertNotNull(ds);
    }
}
