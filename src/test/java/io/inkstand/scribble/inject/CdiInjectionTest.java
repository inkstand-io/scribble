package io.inkstand.scribble.inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import java.lang.reflect.Field;
import org.junit.Test;

/**
 * Created by Gerald on 18.05.2015.
 */
public class CdiInjectionTest {

    @Test
    public void testGetValue() throws Exception {
        //prepare

        String value = "123";

        //act
        CdiInjection subject = new CdiInjection(value);

        //assert
        assertEquals(value, subject.getValue());

    }

    @Test
    public void testIsMatching_matchingTypeAndInjectPresent() throws Exception {


        //prepare
        String value = "123";
        CdiInjection subject = new CdiInjection(value);

        //act

        Field field = InjectField.class.getDeclaredField("field");
        boolean result = subject.isMatching(field);

        //assert
        assertTrue(result);

    }

    @Test
    public void testIsMatching_matchingTypeAndNoInjectPresent() throws Exception {


        //prepare
        String value = "123";
        CdiInjection subject = new CdiInjection(value);

        //act

        Field field = NoInjectField.class.getDeclaredField("field");
        boolean result = subject.isMatching(field);

        //assert
        assertFalse(result);

    }

    @Test
    public void testIsMatching_noMatchingTypeAndInjectPresent() throws Exception {


        //prepare
        String value = "123";
        CdiInjection subject = new CdiInjection(value);

        //act

        Field field = NoMatchingField.class.getDeclaredField("field");
        boolean result = subject.isMatching(field);

        //assert
        assertFalse(result);

    }

    static class NoInjectField {

        private String field;
    }

    static class InjectField {

        @Inject
        private String field;
    }

    static class NoMatchingField {

        @Inject
        private Integer field;
    }

}
