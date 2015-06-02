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

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.junit.rules.TemporaryFolder;

import io.inkstand.scribble.inject.InjectableHolder;
import io.inkstand.scribble.rules.ExternalResource;

/**
 * Rule for testing with java content repositories (JCR). The rule implementations rely on the reference implementation
 * Jackrabbit. The Rule provides access to the {@link Repository} instance and logging in.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public abstract class ContentRepository extends ExternalResource<TemporaryFolder> implements
        InjectableHolder<Repository> {

    private final TemporaryFolder workingDirectory;
    /**
     * The JCR Repository
     */
    private Repository repository;

    /**
     * Creates the content repository in the working directory
     *
     * @param workingDirectory
     */
    public ContentRepository(final TemporaryFolder workingDirectory) {
        super(workingDirectory);
        this.workingDirectory = workingDirectory;
    }


    @Override
    protected void beforeClass() throws Throwable {

        doBefore();

    }


    @Override
    protected void afterClass() {

        doAfter();

    }

    @Override
    protected void before() throws Throwable {
        //call the before method if the repository is not yet created.
        // this check is required if the rule is used as class rule, where the repository
        // is initialized before the before method, to avoid initializing the repository twice
        if (isBeforeState(State.CREATED)) {
            doBefore();
            doStateTransition(State.BEFORE_EXECUTED);
        }
    }

    @Override
    protected void after() {
        //only tear down the repository in the after method, if the before method has been executed, indicating
        //this rule is used a an instance and no class rule. For class rules the doAfter() is executed on
        //tearing down the classrule using afterClass()
        if (isInState(State.BEFORE_EXECUTED)) {
            doAfter();
            doStateTransition(State.AFTER_EXECUTED);
        }
    }

    /**
     * Destroys the repository
     */
    private void doAfter() {

        super.after();
        destroyRepository();
        doStateTransition(State.DESTROYED);
    }

    /**
     * Is invoked after the test has been executed. Implementation may perform actions to shutdown the repository
     * properly.
     */
    protected abstract void destroyRepository();

    /**
     * Creates and initializes the repository. At first the repository is created, transitioning the state to CREATED.
     * Afterwards the repository is initialized and transitioned to INITIALIZED.
     */
    private void doBefore() throws Throwable {

        super.before();
        repository = createRepository();
        doStateTransition(State.CREATED);
        initialize();
        doStateTransition(State.INITIALIZED);
    }

    /**
     * Creates a JCR {@link Repository}
     *
     * @return the created repository
     *
     * @throws Exception
     *         if the creation of the repository failed.
     */
    protected abstract Repository createRepository() throws Exception; // NOSONAR

    /**
     * Method that is invoked after creation to initialize the repository. Subclasses may override this
     * method to perform specific initialization logic.
     */
    protected void initialize() {
    }

    /**
     * @return the repository
     */
    public Repository getRepository() {
        assertStateAfterOrEqual(State.CREATED);
        return repository;
    }

    @Override
    public Repository getInjectionValue() {
        assertStateAfterOrEqual(State.CREATED);
        return repository;
    }

    /**
     * @return the {@link TemporaryFolder} referring to the workingDirectory in which the repository and its
     *         configuration is located. Some implementations may not need a working directory and therefore this value
     *         may be <code>null</code>
     */
    public TemporaryFolder getWorkingDirectory() {
        assertStateAfterOrEqual(State.CREATED);
        return workingDirectory;
    }

    /**
     * Logs into the repository with the given credentials. The created session is not managed and be logged out after
     * use by the caller.
     *
     * @param userId
     *            the user id to log in
     * @param password
     *            the password for the user
     * @return the {@link Session} for the user
     * @throws RepositoryException
     */
    public Session login(final String userId, final String password) throws RepositoryException {

        assertStateAfterOrEqual(State.CREATED);
        return repository.login(new SimpleCredentials(userId, password.toCharArray()));
    }

    /**
     * Logs into the repository as admin user. The session should be logged out after each test if the repository is
     * used as a {@link org.junit.ClassRule}.
     *
     * @return a session with admin privileges
     *
     * @throws RepositoryException
     *         if the login failed for any reason
     */
    public Session getAdminSession() throws RepositoryException {

        return repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
    }
}
