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
