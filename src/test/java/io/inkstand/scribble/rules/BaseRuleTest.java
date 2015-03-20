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
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuleTest {

    @Mock
    private TestRule outer;
    @Mock
    private Statement base;
    @Mock
    private Statement statement;
    @Mock
    private Description description;

    private BaseRule<TestRule> subject;

    @Before
    public void setUp() throws Exception {
        subject = new BaseRule<TestRule>() {
        };
    }

    @Test
    public void testBaseRule_withOuterTestRule() throws Exception {

        // prepare
        when(outer.apply(base, description)).thenReturn(statement);
        // act
        final BaseRule<TestRule> subject = new BaseRule<TestRule>(outer) {
        };
        final Statement stmt = subject.apply(base, description);

        // assert
        verify(outer).apply(base, description);
        assertNotNull(stmt);
        assertEquals(statement, stmt);
    }

    @Test
    public void testApply() throws Exception {
        final Statement stmt = subject.apply(base, description);
        assertNotNull(stmt);
        assertEquals(base, stmt);
    }

    @Test
    public void testAssertNotInitialized_notInitialized_ok() throws Exception {
        subject.assertNotInitialized();
    }

    @Test(expected = AssertionError.class)
    public void testAssertNotInitialized_initialized_fail() throws Exception {
        subject.setInitialized();
        subject.assertNotInitialized();
    }

    @Test(expected = AssertionError.class)
    public void testAssertInitialized_notInitialized_fail() throws Exception {
        subject.assertInitialized();
    }

    @Test
    public void testAssertInitialized_initialized_ok() throws Exception {
        subject.setInitialized();
        subject.assertInitialized();
    }

    @Test
    public void testGetOuterRule() throws Exception {
        final BaseRule<TestRule> subject = new BaseRule<TestRule>(outer) {
        };

        assertEquals(outer, subject.getOuterRule());
    }

}
