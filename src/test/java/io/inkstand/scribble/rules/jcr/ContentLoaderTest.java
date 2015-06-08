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

package io.inkstand.scribble.rules.jcr;

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

import io.inkstand.scribble.JCRAssert;
import io.inkstand.scribble.Scribble;

/**
 * Created by Gerald on 08.06.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class ContentLoaderTest {

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
    public void testApply_withContentDescriptorUrl() throws Throwable {

        //prepare
        URL contentResource = getClass().getResource("ContentLoaderTest_inkstandJcrImport_v1-0.xml");
        subject.setContentDescriptorUrl(contentResource);

        //act
        subject.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {

                //assert
                Session session = repository.login();
                JCRAssert.assertStringPropertyEquals(session.getNode("/root"), "jcr:title", "TestTitle");

            }
        }, description).evaluate();
    }
}
