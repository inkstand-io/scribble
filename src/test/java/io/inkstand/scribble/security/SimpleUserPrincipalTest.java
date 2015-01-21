package io.inkstand.scribble.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleUserPrincipalTest {

    private SimpleUserPrincipal subject;

    @Before
    public void setUp() throws Exception {
        subject = new SimpleUserPrincipal("testUser");
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("testUser", subject.getName());
    }

}
