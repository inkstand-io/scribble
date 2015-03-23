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

package io.inkstand.scribble.rules.jcr;

import io.inkstand.scribble.inject.InjectableHolder;
import io.inkstand.scribble.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

/**
 * Rule for testing with java content repositories (JCR). The rule implementations rely on the reference implementation
 * Jackrabbit. The Rule provides access to the {@link Repository} instance and logging in.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public abstract class ContentRepository extends ExternalResource<TemporaryFolder> implements
        InjectableHolder<Repository> {

    /**
     * The JCR Repository
     */
    private Repository repository;

    private final TemporaryFolder workingDirectory;

    private boolean beforeExecuted;

    /**
     * Creates the content repository in the working directory
     *
     * @param workingDirectory
     */
    public ContentRepository(final TemporaryFolder workingDirectory) {
        super(workingDirectory);
        this.workingDirectory = workingDirectory;
    }

    /**
     * Initializes the repository
     */
    @Override
    protected void beforeClass() throws Throwable {
        super.before();
        repository = createRepository();

    }

    /**
     * Destroys the repository
     */
    @Override
    protected void afterClass() {
        super.after();
        destroyRepository();

    }

    @Override
    protected void before() throws Throwable {
        if (!isInitialized()) {
            beforeClass();
            beforeExecuted = true;
        }
    }

    @Override
    protected void after() {
        if (beforeExecuted) {
            afterClass();
        }

    }

    /**
     * @return the repository
     */
    public Repository getRepository() {
        assertInitialized();
        return repository;
    }

    @Override
    public Repository getInjectionValue() {
        assertInitialized();
        return repository;
    }

    /**
     * @return the {@link TemporaryFolder} referring to the workingDirectory in which the repository and its
     *         configuration is located. Some implementations may not need a working directory and therefore this value
     *         may be <code>null</code>
     */
    public TemporaryFolder getWorkingDirectory() {
        assertInitialized();
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
        assertInitialized();
        return repository.login(new SimpleCredentials(userId, password.toCharArray()));
    }

    /**
     * Creates a JCR {@link Repository}
     *
     * @return the created repository
     * @throws Exception
     *             if the creation of the repository failed.
     */
    protected abstract Repository createRepository() throws Exception; // NOSONAR

    /**
     * Is invoked after the test has been executed. Implementation may perform actions to shutdown the repository
     * properly.
     */
    protected abstract void destroyRepository();

}