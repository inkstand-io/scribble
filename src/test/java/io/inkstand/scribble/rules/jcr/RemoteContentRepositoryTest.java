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

import static org.junit.Assert.assertEquals;

import java.net.URL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Gerald on 19.05.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class RemoteContentRepositoryTest {

    /**
     * The class under test
     */
    @InjectMocks
    private RemoteContentRepository subject;

    @Test(expected = AssertionError.class)
    public void testBefore_invalidContainerQualifier() throws Throwable {
        //prepare
        System.setProperty("arquillian.launch", "& '1'='1'");
        URL arquillianContainerConfiguration = new URL("file:///notexisting");
        subject.onArquillianHost(arquillianContainerConfiguration);

        //act
        subject.before();

    }

    @Test
    public void testBefore_validContainerQualifier() throws Throwable {
        //prepare
        System.setProperty("arquillian.launch", "testContainer");
        URL arquillianContainerConfiguration = getClass().getResource("RemoteContentRepositoryTest_arquillian.xml");
        subject.onArquillianHost(arquillianContainerConfiguration);
        subject.setupManually();

        //act
        subject.before();

        //assert
        assertEquals("test.example.com", subject.getRemoteHost());
    }
}
