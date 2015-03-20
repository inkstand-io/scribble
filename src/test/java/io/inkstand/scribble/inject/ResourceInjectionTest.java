package io.inkstand.scribble.inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Resource;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ResourceInjectionTest {

    private String injectionValue;
    private ResourceInjection subject;

    @Before
    public void setUp() throws Exception {
        injectionValue = "testValue";
        subject = new ResourceInjection(injectionValue);
    }

    @Test
    public void testIsMatching_noResource_false() throws Exception {
        final Field field = SimpleInjectionTarget.class.getDeclaredField("noResource");
        assertFalse(subject.isMatching(field));
    }

    @Test
    public void testIsMatching_byMatchingTypeOnly_false() throws Exception {
        final Field field = SimpleInjectionTarget.class.getDeclaredField("simpleResource");
        assertFalse(subject.isMatching(field));
    }

    @Test
    public void testIsMatching_byNoMatchingType_false() throws Exception {
        final Field field = SimpleInjectionTarget.class.getDeclaredField("noMatchingType");
        assertFalse(subject.isMatching(field));
    }

    @Test
    public void testByName_match() throws Exception {
        // prepare
        final Field field = SimpleInjectionTarget.class.getDeclaredField("namedResource");
        // act
        subject.byName("target2");
        // assert
        assertTrue(subject.isMatching(field));
    }

    @Test
    public void testByName_noMatch() throws Exception {
        // prepare
        final Field field = SimpleInjectionTarget.class.getDeclaredField("namedResource");
        // act
        subject.byName("noMatch");
        // assert
        assertFalse(subject.isMatching(field));
    }

    @Test
    public void testByMappedName_match() throws Exception {
        // prepare
        final Field field = SimpleInjectionTarget.class.getDeclaredField("mappedNameResource");
        // act
        subject.byMappedName("target3");
        // assert
        assertTrue(subject.isMatching(field));
    }

    @Test
    public void testByMappedName_noMatch() throws Exception {
        // prepare
        final Field field = SimpleInjectionTarget.class.getDeclaredField("mappedNameResource");
        // act
        subject.byMappedName("noMatch");
        // assert
        assertFalse(subject.isMatching(field));
    }

    @Test
    public void testByLookup_match() throws Exception {
        // prepare
        final Field field = SimpleInjectionTarget.class.getDeclaredField("lookupResource");
        // act
        subject.byLookup("target1");
        // assert
        assertTrue(subject.isMatching(field));
    }

    @Test
    public void testHashCode_allNulls() throws Exception {
        //prepare
        ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();
        //act

        //assert
        assertEquals(literal.hashCode(), other.hashCode());

    }

    @Test
    public void testHashCode_equalFields() throws Exception {
        //prepare
        ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();

        literal.setLookup("lookup");
        other.setLookup("lookup");
        literal.setName("name");
        other.setName("name");
        literal.setMappedName("mappedName");
        other.setMappedName("mappedName");
        //act

        //assert
        assertEquals(literal.hashCode(), other.hashCode());

    }

    @Test
     public void testHashCode_differentLookup() throws Exception {
        //prepare
        ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();

        literal.setLookup("lookup");
        other.setLookup("differentLookup");
        literal.setName("name");
        other.setName("name");
        literal.setMappedName("mappedName");
        other.setMappedName("mappedName");
        //act

        //assert
        assertNotEquals(literal.hashCode(), other.hashCode());

    }

    @Test
    public void testHashCode_differentName() throws Exception {
        //prepare
        ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();

        literal.setLookup("lookup");
        other.setLookup("lookup");
        literal.setName("name");
        other.setName("differentName");
        literal.setMappedName("mappedName");
        other.setMappedName("mappedName");
        //act

        //assert
        assertNotEquals(literal.hashCode(), other.hashCode());

    }

    @Test
    public void testHashCode_differentMappedName() throws Exception {
        //prepare
        ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();

        literal.setLookup("lookup");
        other.setLookup("lookup");
        literal.setName("name");
        other.setName("name");
        literal.setMappedName("mappedName");
        other.setMappedName("differentMappedName");
        //act

        //assert
        assertNotEquals(literal.hashCode(), other.hashCode());

    }

    @Test
    public void testByLookup_noMatch() throws Exception {
        // prepare
        final Field field = SimpleInjectionTarget.class.getDeclaredField("lookupResource");
        // act
        subject.byLookup("noMatch");
        // assert
        assertFalse(subject.isMatching(field));
    }

    @Test
    public void testResourceLiteral_equals_same_true() throws Exception {
        //prepare
        ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        //act
        boolean result = literal.equals(literal);

        //assert
        assertTrue(result);

    }

    @Test
    public void testResourceLiteral_equals_otherObject_false() throws Exception {
        //prepare
        ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        Resource nonEqualResource = mock(Resource.class);

        //act
        boolean result = literal.equals(nonEqualResource);

        //assert
        assertFalse(result);

    }

    @Test
    public void testResourceLiteral_equals_allNullsResource_true() throws Exception {
        //prepare
        ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        ResourceInjection.ResourceLiteral equalResource = new ResourceInjection.ResourceLiteral();

        //act
        boolean result = literal.equals(equalResource);

        //assert
        assertTrue(result);

    }

    @Test
    public void testResourceLiteral_equals_nonResource_false() throws Exception {
        //prepare
        ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        Object nonResource = new Object();
        //act
        boolean result = literal.equals(nonResource);

        //assert
        assertFalse(result);

    }

    @Test
    public void testResourceLiteral_equals_Resource_equalLookup_true() throws Exception {
        //prepare
        final String lookup = "lookup";

        final ResourceInjection.ResourceLiteral literal =  new ResourceInjection.ResourceLiteral();
        literal.setLookup(lookup);
        final ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();
        other.setLookup(lookup);

        //act
        boolean result = literal.equals(other);

        //assert
        assertTrue(result);

    }

    @Test
    public void testResourceLiteral_equals_Resource_nullLookup_false() throws Exception {
        //prepare

        final ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        final ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();
        other.setLookup("nonNull");

        //act
        boolean result = literal.equals(other);

        //assert
        assertFalse(result);

    }

    @Test
    public void testResourceLiteral_equals_Resource_otherLookup_false() throws Exception {
        //prepare

        final ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        literal.setLookup("lookup");
        final ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();
        other.setLookup("other");

        //act
        boolean result = literal.equals(other);

        //assert
        assertFalse(result);

    }

    @Test
    public void testResourceLiteral_equals_Resource_equalName_true() throws Exception {
        //prepare
        final String name = "name";

        final ResourceInjection.ResourceLiteral literal =  new ResourceInjection.ResourceLiteral();
        literal.setName(name);
        final ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();
        other.setName(name);

        //act
        boolean result = literal.equals(other);

        //assert
        assertTrue(result);

    }

    @Test
    public void testResourceLiteral_equals_Resource_nullName_false() throws Exception {
        //prepare

        final ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        final ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();
        other.setName("nonNull");

        //act
        boolean result = literal.equals(other);

        //assert
        assertFalse(result);

    }

    @Test
    public void testResourceLiteral_equals_Resource_otherName_false() throws Exception {
        //prepare

        final ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        literal.setName("name");
        final ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();
        other.setName("other");

        //act
        boolean result = literal.equals(other);

        //assert
        assertFalse(result);

    }

    @Test
    public void testResourceLiteral_equals_Resource_equalMappedName_true() throws Exception {
        //prepare
        final String name = "name";

        final ResourceInjection.ResourceLiteral literal =  new ResourceInjection.ResourceLiteral();
        literal.setMappedName(name);
        final ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();
        other.setMappedName(name);

        //act
        boolean result = literal.equals(other);

        //assert
        assertTrue(result);

    }

    @Test
    public void testResourceLiteral_equals_Resource_nullMappedName_false() throws Exception {
        //prepare

        final ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        final ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();
        other.setMappedName("nonNull");

        //act
        boolean result = literal.equals(other);

        //assert
        assertFalse(result);

    }

    @Test
    public void testResourceLiteral_equals_Resource_otherMappedName_false() throws Exception {
        //prepare

        final ResourceInjection.ResourceLiteral literal = new ResourceInjection.ResourceLiteral();
        literal.setMappedName("name");
        final ResourceInjection.ResourceLiteral other = new ResourceInjection.ResourceLiteral();
        other.setMappedName("other");

        //act
        boolean result = literal.equals(other);

        //assert
        assertFalse(result);

    }


    static class SimpleInjectionTarget {

        @Resource(lookup = "target1")
        String lookupResource;

        @Resource(name = "target2")
        String namedResource;

        @Resource(mappedName = "target3")
        String mappedNameResource;

        @Resource
        String simpleResource;

        @Resource
        Integer noMatchingType;

        String noResource;
    }

}
