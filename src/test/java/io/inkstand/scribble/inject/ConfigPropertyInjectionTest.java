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

package io.inkstand.scribble.inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import java.lang.reflect.Field;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.inkstand.scribble.Scribble;

public class ConfigPropertyInjectionTest {

    private ConfigPropertyInjection subject;

    @Before
    public void setUp() throws Exception {
        subject = new ConfigPropertyInjection("config.property", "testValue");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInjectInto_NullValue_defaultInjected() throws Exception {
        //prepare
        // create an injection with no value (null) for a config property with a default value
        final SimpleInjectionTarget target = new SimpleInjectionTarget();

        // act
        Scribble.inject(null).asConfigProperty("config.property.default").into(target);

        // assert
        assertEquals("defaultValue", target.configPropertyWithDefault);
    }

    @Test
    public void testInjectInto_ValidValue_valueInjected() throws Exception {
        //prepare
        // create an injection with no value (null) for a config property with a default value
        final SimpleInjectionTarget target = new SimpleInjectionTarget();

        // act
        Scribble.inject("testString").asConfigProperty("config.property.default").into(target);

        // assert
        assertEquals("testString", target.configPropertyWithDefault);
    }

    @Test
    public void testGetValue() throws Exception {

        assertEquals("testValue", subject.getValue());
    }

    @Test
    public void testGetValue_defaultValueOfMatchingConfigProperty() throws Exception {
        // prepare
        // create an injection with no value (null) for a config property with a default value
        final ConfigPropertyInjection subject = new ConfigPropertyInjection("config.property.default", null);
        assertNull(subject.getValue());
        // get the field of the config property that has a default value and matches the injection name
        final Field field = SimpleInjectionTarget.class.getDeclaredField("configPropertyWithDefault");
        // trigger a successful match
        subject.isMatching(field);

        // act
        final String value = (String) subject.getValue();

        // assert
        assertNotNull(value);
        assertEquals("defaultValue", value);
    }

    @Test
    public void testIsMatching_configPropertyNameMatch_True() throws Exception {

        final Field field = SimpleInjectionTarget.class.getDeclaredField("configProperty");
        assertTrue(subject.isMatching(field));
    }

    @Test
    public void testIsMatching_configPropertyNameMismatch_false() throws Exception {

        final Field field = SimpleInjectionTarget.class.getDeclaredField("configPropertyWithDefault");
        assertFalse(subject.isMatching(field));
    }

    @Test
    public void testIsMatching_noInjectConfigProperty_false() throws Exception {

        final Field field = SimpleInjectionTarget.class.getDeclaredField("noInjectConfigProperty");
        assertFalse(subject.isMatching(field));
    }

    @Test
    public void testIsMatching_noConfigProperty_false() throws Exception {

        final Field field = SimpleInjectionTarget.class.getDeclaredField("noConfigProperty");
        assertFalse(subject.isMatching(field));
    }

    static class SimpleInjectionTarget {

        @ConfigProperty(name = "config.property")
        String noInjectConfigProperty;

        @Inject
        @ConfigProperty(name = "config.property")
        String configProperty;

        @Inject
        @ConfigProperty(name = "config.property.default", defaultValue = "defaultValue")
        String configPropertyWithDefault;

        String noConfigProperty;
    }

}
