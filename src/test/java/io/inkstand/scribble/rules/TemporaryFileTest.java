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
 * limitations under the License
 */

package io.inkstand.scribble.rules;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TemporaryFileTest {

    private static final String TEST_FILE_NAME = "testfile.txt";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private TemporaryFile subject;
    @Mock
    private Description description;

    private URL getTestContentUrl() {
        return getClass().getResource("TemporaryFileTest_testContent.txt");
    }

    private URL getEmptyTestContentUrl() {
        return getClass().getResource("TemporaryFileTest_emptyContent.txt");
    }

    @Before
    public void setUp() throws Exception {
        subject = new TemporaryFile(folder, TEST_FILE_NAME);
    }

    @Test
    public void testBefore_noContent() throws Throwable {
        // act
        subject.before();

        // assert
        assertNotNull(subject.getFile());
        assertTrue(subject.getFile().exists());
        assertEquals(0, subject.getFile().length());
    }

    @Test(expected = AssertionError.class)
    public void testBefore_noContentForceContent() throws Throwable {
        // prepare
        subject.setForceContent(true);

        // act
        subject.before();
    }

    @Test
    public void testBefore_withContentNoForce() throws Throwable {
        // prepare
        subject.setContentUrl(getTestContentUrl());
        // act
        subject.before();
        // assert
        assertNotNull(subject.getFile());
        assertTrue(subject.getFile().exists());
        assertEquals(12, subject.getFile().length());
    }

    @Test
    public void testBefore_withContentWithForce() throws Throwable {
        // prepare
        subject.setForceContent(true);
        subject.setContentUrl(getTestContentUrl());
        // act
        subject.before();
        // assert
        assertNotNull(subject.getFile());
        assertTrue(subject.getFile().exists());
        assertEquals(12, subject.getFile().length());
    }

    @Test
    public void testBefore_withEmptyContentWithForce() throws Throwable {
        // prepare
        subject.setForceContent(true);
        subject.setContentUrl(getEmptyTestContentUrl());
        // act
        subject.before();
        // assert
        assertNotNull(subject.getFile());
        assertTrue(subject.getFile().exists());
        assertEquals(0, subject.getFile().length());
    }

    @Test
    public void testAfter() throws Throwable {
        // prepare
        subject.before();

        // act
        subject.after();

        // assert
        assertFalse(subject.getFile().exists());
    }

    @Test
    public void testApply() throws Throwable {

        final TemporaryFile subject = new TemporaryFile(new TemporaryFolder(), "testfile.txt");
        // prepare
        final Statement base = spy(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                assertTrue("Temporary file was not created", subject.getFile().exists());
                assertEquals("testfile.txt", subject.getFile().getName());
            }

        });

        // act
        subject.apply(base, description).evaluate();

        // assert
        verify(base).evaluate();

    }
}