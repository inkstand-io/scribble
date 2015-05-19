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

package io.inkstand.scribble.rules.builder;

import static io.inkstand.scribble.JCRAssert.assertNodeTypeExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.jcr.Session;
import java.net.URL;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.inkstand.scribble.rules.jcr.StandaloneContentRepository;

/**
 * Created by Gerald on 19.05.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class StandaloneContentRepositoryBuilderTest {

    @Rule
    public  TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private Description description;

    /**
     * The class under test
     */
    private StandaloneContentRepositoryBuilder subject;

    @Before
    public void setUp() throws Exception {
        subject = new StandaloneContentRepositoryBuilder(temporaryFolder);
    }

    @Test
    public void testWithConfiguration_customConfig() throws Exception {

        //prepare
        URL configUrl = new URL("http://localhost");

        //act
        subject.withConfiguration(configUrl);

        //assert
        assertEquals(configUrl, subject.getConfigUrl());
    }

    @Test
    public void testBuild_defaultConfigAndNoCnd() throws Throwable {

        //prepare

        //act
        final StandaloneContentRepository result = subject.build();

        //assert
        assertNotNull(result);
        result.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                assertNotNull(result.getRepository());
            }
        }, description).evaluate();
    }

    @Test
     public void testBuild_customConfigAndNoCnd() throws Throwable {

        //prepare
        URL configUrl = getClass().getResource("StandaloneContentRepositoryBuilderTest_repository.xml");
        subject.withConfiguration(configUrl);

        //act
        final StandaloneContentRepository result = subject.build();

        //assert
        assertNotNull(result);
        result.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                assertNotNull(result.getRepository());
            }
        }, description).evaluate();

    }

    @Test
    public void testBuild_customConfigAndCnd() throws Throwable {

        //prepare
        final URL configUrl = getClass().getResource("StandaloneContentRepositoryBuilderTest_repository.xml");
        final URL cndUrl = getClass().getResource("StandaloneContentRepositoryBuilderTest_testModel.cnd");
        subject.withConfiguration(configUrl);
        subject.withNodeTypes(cndUrl);

        //act
        final StandaloneContentRepository result = subject.build();

        //assert
        assertNotNull(result);
        result.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {

                assertNotNull(result.getRepository());
                final Session session = result.login("admin","admin");
                assertNodeTypeExists(session, "test:testType");

            }
        }, description).evaluate();

    }


}
