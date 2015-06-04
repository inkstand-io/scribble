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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import java.net.URL;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.inkstand.scribble.rules.BaseRule;
import io.inkstand.scribble.rules.BaseRuleHelper;

@RunWith(MockitoJUnitRunner.class)
public class ContentRepositoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Mock
    private Repository repository;

    @Mock
    private Session session;

    private ContentRepository subject;

    @Before
    public void setUp() throws Exception {
        subject = new ContentRepository(folder) {

            private URL nodeTypeDefinitions;

            public void setNodeTypeDefinitions(final URL nodeTypeDefinitions) {

                this.nodeTypeDefinitions = nodeTypeDefinitions;
            }

            @Override
            protected void destroyRepository() {

            }

            @Override
            protected Repository createRepository() throws Exception {

                return repository;
            }
        };
    }

    @Test
    public void testBeforeClass() throws Throwable {
        // act
        subject.beforeClass();

        // assert
        assertNotNull(subject.getRepository());
        assertEquals(BaseRule.State.INITIALIZED, BaseRuleHelper.getState(subject));
    }

    @Test
    public void testAfterClass() throws Throwable {
        // prepare
        final ContentRepository spy = spy(subject);
        spy.beforeClass();
        // act
        spy.afterClass();
        // assert
        verify(spy).destroyRepository();
        assertEquals(BaseRule.State.DESTROYED, BaseRuleHelper.getState(spy));
    }

    @Test
    public void testBefore_alreadyCreated_noSetup() throws Throwable {
        //prepare
        BaseRuleHelper.setState(subject, BaseRule.State.CREATED);

        //act
        subject.before();

        //assert
        assertEquals(BaseRule.State.CREATED, BaseRuleHelper.getState(subject));
    }

    @Test
    public void testBefore_new_setup() throws Throwable {
        //prepare
        BaseRuleHelper.setState(subject, BaseRule.State.NEW);

        //act
        subject.before();

        //assert
        assertEquals(BaseRule.State.BEFORE_EXECUTED, BaseRuleHelper.getState(subject));
    }

    @Test
    public void testAfter_notBeforeExecuted_noTeardown() throws Exception {
        //prepare
        BaseRuleHelper.setState(subject, BaseRule.State.CREATED);

        //act
        subject.after();

        //assert
        assertEquals(BaseRule.State.CREATED, BaseRuleHelper.getState(subject));
    }

    @Test
    public void testAfter_beforeExecuted_teardown() throws Exception {
        //prepare
        BaseRuleHelper.setState(subject, BaseRule.State.BEFORE_EXECUTED);

        //act
        subject.after();

        //assert
        assertEquals(BaseRule.State.AFTER_EXECUTED, BaseRuleHelper.getState(subject));
    }

    @Test
    public void testAfter_beforeExecuted_and_activeAdminSession_teardownAndLogout() throws Throwable {
        //prepare
        BaseRuleHelper.setState(subject, BaseRule.State.NEW);
        subject.before();
        when(repository.login(any(SimpleCredentials.class))).thenReturn(session);
        when(session.isLive()).thenReturn(true);
        subject.getAdminSession();

        //act
        subject.after();

        //assert
        assertEquals(BaseRule.State.AFTER_EXECUTED, BaseRuleHelper.getState(subject));
        verify(session).logout();
    }

    @Test
    public void testAfter_beforeExecuted_and_activeAdminSession_teardownAndNoLogout() throws Throwable {
        //prepare
        BaseRuleHelper.setState(subject, BaseRule.State.NEW);
        subject.before();
        when(repository.login(any(SimpleCredentials.class))).thenReturn(session);
        when(session.isLive()).thenReturn(false);
        subject.getAdminSession();

        //act
        subject.after();

        //assert
        assertEquals(BaseRule.State.AFTER_EXECUTED, BaseRuleHelper.getState(subject));
        verify(session, times(0)).logout();
    }

    @Test(expected = AssertionError.class)
    public void testGetRepository_uninitialized_fail() throws Exception {
        subject.getRepository();
    }

    @Test
    public void testGetRepository_initialized_ok() throws Throwable {
        // prepare
        subject.before();
        BaseRuleHelper.setState(subject, BaseRule.State.INITIALIZED);
        // act
        final Repository repo = subject.getRepository();
        // assert
        assertEquals(repository, repo);
    }

    @Test(expected = AssertionError.class)
    public void testGetInjectionValue_uninitialized_fail() throws Exception {
        subject.getInjectionValue();
    }

    @Test
    public void testGetInjectionValue_initialized_ok() throws Throwable {
        // prepare
        subject.before();
        BaseRuleHelper.setState(subject, BaseRule.State.INITIALIZED);
        // act
        final Repository repo = subject.getInjectionValue();
        // assert
        assertEquals(repository, repo);
    }

    @Test
    public void testGetWorkingDirectory_initialized_ok() throws Throwable {
        // prepare
        BaseRuleHelper.setState(subject, BaseRule.State.INITIALIZED);
        // act
        final TemporaryFolder workingDirectory = subject.getWorkingDirectory();
        // aasert
        assertEquals(folder, workingDirectory);
    }

    @Test(expected = AssertionError.class)
    public void testGetWorkingDirectory_notInitialized_fail() throws Throwable {
        subject.getWorkingDirectory();
    }

    @Test(expected = AssertionError.class)
    public void testLogin_notInitialized_fail() throws Throwable {
        subject.login("aUser", "aPassword");
    }

    @Test
    public void testLogin_initialized() throws Throwable {
        // prepare
        when(repository.login(any(SimpleCredentials.class))).thenReturn(session);
        subject.before();
        BaseRuleHelper.setState(subject, BaseRule.State.INITIALIZED);
        // act
        final Session userSession = subject.login("aUser", "aPassword");
        // assert
        assertNotNull(userSession);
        assertEquals(session, userSession);

        final ArgumentCaptor<SimpleCredentials> captor = ArgumentCaptor.forClass(SimpleCredentials.class);
        verify(repository).login(captor.capture());
        final SimpleCredentials passedParam = captor.getValue();
        assertEquals("aUser", passedParam.getUserID());
        assertEquals("aPassword", String.valueOf(passedParam.getPassword()));
    }

    @Test
    public void testGetAdminSession_firstLogin() throws Throwable {

        //prepare
        when(repository.login(any(SimpleCredentials.class))).thenReturn(session);
        subject.before();
        BaseRuleHelper.setState(subject, BaseRule.State.INITIALIZED);

        // act
        final Session adminSession = subject.getAdminSession();

        // assert
        assertNotNull(adminSession);
        assertEquals(session, adminSession);

        //verify an admin login has been performed
        final ArgumentCaptor<SimpleCredentials> captor = ArgumentCaptor.forClass(SimpleCredentials.class);
        verify(repository).login(captor.capture());
        final SimpleCredentials passedParam = captor.getValue();
        assertEquals("admin", passedParam.getUserID());
        assertEquals("admin", String.valueOf(passedParam.getPassword()));
    }

    @Test
    public void testGetAdminSession_consecutiveLogin() throws Throwable {

        //prepare
        when(repository.login(any(SimpleCredentials.class))).thenReturn(session);
        when(session.isLive()).thenReturn(true);
        subject.before();
        BaseRuleHelper.setState(subject, BaseRule.State.INITIALIZED);
        //the first login
        subject.getAdminSession();

        // act
        //the second login
        Session adminSession = subject.getAdminSession();

        // assert
        assertNotNull(adminSession);
        assertEquals(session, adminSession);

        //verify no new admin login has been performed, only 1 login from the prepare phase
        verify(repository, times(1)).login(any(SimpleCredentials.class));
        //and the session was refreshed
        verify(session).refresh(false);
    }

    @Test
    public void testGetAdminSession_inactiveSession() throws Throwable {

        //prepare
        when(repository.login(any(SimpleCredentials.class))).thenReturn(session);
        when(session.isLive()).thenReturn(false);
        subject.before();
        BaseRuleHelper.setState(subject, BaseRule.State.INITIALIZED);
        //the first login
        subject.getAdminSession();

        // act
        //the second login
        Session adminSession = subject.getAdminSession();

        // assert
        assertNotNull(adminSession);
        assertEquals(session, adminSession);

        //verify an new login has been performed
        final ArgumentCaptor<SimpleCredentials> captor = ArgumentCaptor.forClass(SimpleCredentials.class);
        //two logins are performed, 1 in prepare and 1 in act
        verify(repository, times(2)).login(captor.capture());
        final SimpleCredentials passedParam = captor.getValue();
        assertEquals("admin", passedParam.getUserID());
        assertEquals("admin", String.valueOf(passedParam.getPassword()));
    }
}
