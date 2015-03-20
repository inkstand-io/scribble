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

import io.inkstand.scribble.rules.BaseRuleHelper;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Repository;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurableContentRepositoryTest {

    @Mock
    private Repository repository;
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private ConfigurableContentRepository subject;

    private final URL configUrl = getClass().getResource("ConfigurableContentRepositoryTest_repository.xml");

    @Before
    public void setUp() throws Exception {
        subject = new ConfigurableContentRepository(folder) {

            @Override
            protected void destroyRepository() {
            }

            @Override
            protected Repository createRepository() throws Exception {
                return repository;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateRepositoryConfiguration() throws Throwable {
        // prepare
        subject.setConfigUrl(configUrl);
        BaseRuleHelper.setInitialized(subject);

        // act
        final RepositoryConfig config = subject.createRepositoryConfiguration();

        // assert
        assertNotNull(config);
        assertEquals(folder.getRoot().getAbsolutePath(), config.getHomeDir());
    }

    @Test
    public void testSetGetConfigUrl() throws Exception {
        subject.setConfigUrl(configUrl);
        assertEquals(configUrl, subject.getConfigUrl());
    }

}
