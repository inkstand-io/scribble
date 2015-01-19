package io.inkstand.scribble.inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        final Field field = SimpleInjectionTarget.class.getDeclaredField("congfigPropertyWithDefault");
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
        final Field field = SimpleInjectionTarget.class.getDeclaredField("congfigProperty");
        assertTrue(subject.isMatching(field));
    }

    @Test
    public void testIsMatching_configPropertyNameMismatch_false() throws Exception {
        final Field field = SimpleInjectionTarget.class.getDeclaredField("congfigPropertyWithDefault");
        assertFalse(subject.isMatching(field));
    }

    @Test
    public void testIsMatching_noConfigProperty_false() throws Exception {
        final Field field = SimpleInjectionTarget.class.getDeclaredField("noConfigProperty");
        assertFalse(subject.isMatching(field));
    }

    static class SimpleInjectionTarget {

        @ConfigProperty(name = "config.property")
        String congfigProperty;

        @ConfigProperty(name = "config.property.default", defaultValue = "defaultValue")
        String congfigPropertyWithDefault;

        String noConfigProperty;
    }

}
