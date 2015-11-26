package io.inkstand.scribble.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Created by Gerald Muecke on 26.11.2015.
 */
public class CallStackTest {

    @Test
    public void testGetCallerClass() throws Exception {

        //prepare

        //act
        Class<?> cls = CallStack.getCallerClass();

        //assert
        assertEquals(this.getClass(), cls );
    }
}
