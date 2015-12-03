package io.inkstand.scribble.rules;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Gerald Muecke on 20.11.2015.
 */
public class SystemConsoleExample {


    @Rule
    public SystemConsole console = new SystemConsole();

    @Test
    public void outTest() {
        System.out.print("test");
        assertEquals("test", console.getOut());
    }

    @Test
    public void errTest() {
        System.err.print("test");
        assertEquals("test", console.getErr());
    }

}

