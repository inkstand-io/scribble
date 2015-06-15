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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jcr.Session;
import java.net.URL;
import org.apache.deltaspike.core.api.config.ConfigProperty;
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
import io.inkstand.scribble.rules.ldap.Directory;
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
    public void testInjectIntoAll() throws Exception {
        //prepare
        String var = "123";
        SimpleInjectionTarget target = new SimpleInjectionTarget();

        //act
        Scribble.inject(var).intoAll(target);

        //assert
        assertEquals(var, target.injectionTarget1);
        assertEquals(var, target.injectionTarget2);
        assertEquals(var, target.injectionTarget3);

    }

    @Test
    public void testInjectInto() throws Exception {
        //prepare
        String var = "123";
        SimpleInjectionTarget target = new SimpleInjectionTarget();

        //act
        Scribble.inject(var).into(target);

        //assert
        assertEquals(var, target.injectionTarget1);
        assertNull(var, target.injectionTarget2);
        assertNull(var, target.injectionTarget3);

    }

    @Test
    public void testInjectAsConfigPropertyInto() throws Exception {
        //prepare
        String var = "123";
        SimpleInjectionTarget target = new SimpleInjectionTarget();

        //act
        Scribble.inject(var).asConfigProperty("property1").into(target);

        //assert
        assertEquals(var, target.injectionTarget2);
        assertNull(target.injectionTarget1);
        assertNull(target.injectionTarget3);
    }

    @Test
    public void testInjectAsResourceInto() throws Exception {
        //prepare
        String var = "123";
        SimpleInjectionTarget target = new SimpleInjectionTarget();

        //act
        Scribble.inject(var).asResource().byLookup("java:/any/name").into(target);

        //assert
        assertEquals(var, target.injectionTarget3);
        assertNull(target.injectionTarget1);
        assertNull(target.injectionTarget2);
    }

    @Test
    public void testInjectPrimitiveAsConfigPropertyInto() throws Exception {
        //prepare
        int var = 123;
        SimpleInjectionTarget target = new SimpleInjectionTarget();

        //act
        Scribble.inject(var).asConfigProperty("property2").into(target);

        //assert
        assertEquals(var, target.primitiveInt);
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
        final InMemoryContentRepository result = Scribble.newInMemoryContentRepository()
                                                         .withNodeTypes(cndModel)
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
        final StandaloneContentRepository result = Scribble.newStandaloneContentRepository()
                                                           .withNodeTypes(cndModel)
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
    public void testNewDirectory() throws Exception {
        //prepare

        //act
        final Directory dir = Scribble.newDirectory().build();

        //assert
        assertNotNull(dir);

    }

    @Test
    public void testNewDirectoryServer() throws Exception {
        //prepare

        //act
        final DirectoryServer ds = Scribble.newDirectoryServer().build();

        //assert
        assertNotNull(ds);
    }

    /**
     * Test to verify the rule is properly applicable and initialized with users from an ldif.
     *
     * @throws Throwable
     */
    @Test
    public void testNewDirectoryServerWithContent() throws Throwable {
        //prepare

        //act
        final URL ldif = getClass().getResource("ScribbleTest_testUsers.ldif");
        final DirectoryServer ds = Scribble.newDirectory()
                                           .withPartition("inkstand", "dc=scribble")
                                           .importLdif(ldif)
                                           .aroundDirectoryServer()
                                           .onAvailablePort()
                                           .build();
        //assert
        assertNotNull(ds);
        ds.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {

                assertTrue(ds.getDirectoryService().getSession().exists("uid=testuser,ou=users,dc=scribble"));
            }
        }, description).evaluate();
    }

    static class SimpleInjectionTarget {

        String injectionTarget1;

        @Inject
        @ConfigProperty(name = "property1")
        String injectionTarget2;

        @Resource(lookup = "java:/any/name")
        String injectionTarget3;

        @Inject
        @ConfigProperty(name = "property2")
        int primitiveInt;
    }
}
