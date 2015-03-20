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

import io.inkstand.scribble.rules.BaseRule;
import io.inkstand.scribble.rules.RuleSetup;
import io.inkstand.scribble.rules.RuleSetup.RequirementLevel;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import java.net.URL;

public class ContentLoader extends BaseRule<ContentRepository> {

    // TODO add proper content loading and refactor class

    /**
     * SLF4J Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(ContentLoader.class);

    private final ContentRepository repository;
    private URL contentDescriptorUrl;

    public ContentLoader(final ContentRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Sets the resource containing the description of the content ro
     *
     * @param contentDescriptorUrl
     */
    @RuleSetup(RequirementLevel.REQUIRED)
    public void setContentDescriptorUrl(final URL contentDescriptorUrl) {
        assertNotInitialized();
        this.contentDescriptorUrl = contentDescriptorUrl;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {

        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                try {
                    LOG.info("Loading Content");
                    final Repository repo = repository.getRepository();
                    final Session session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
                    // TODO replace with content provider
                    // JCRUtil.loadContent(session, contentDescriptorUrl);
                    System.out.println(contentDescriptorUrl);
                    session.logout();
                    base.evaluate();

                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }

            }

        };

    }

}
