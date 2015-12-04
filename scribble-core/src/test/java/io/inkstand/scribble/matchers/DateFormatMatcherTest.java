package io.inkstand.scribble.matchers;

import static io.inkstand.scribble.matchers.DateFormatMatcher.matchesDateFormat;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Gerald Muecke on 03.12.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class DateFormatMatcherTest {

    @Mock
    private Description description;

    @Test
    public void testMatches_nonString_false() throws Exception {

        //prepare
        DateFormatMatcher subject = new DateFormatMatcher("YYYYMMDD");
        //act
        boolean result = subject.matches(new Object());

        //assert
        assertFalse(result);
    }

    @Test
    public void testMatches_nonDateString_false() throws Exception {

        //prepare
        DateFormatMatcher subject = new DateFormatMatcher("YYYYMMDD");
        //act
        boolean result = subject.matches("nonDateString");

        //assert
        assertFalse(result);
    }

    @Test
    public void testMatches_dateString_true() throws Exception {

        //prepare
        DateFormatMatcher subject = new DateFormatMatcher("YYYYMMDD");
        //act
        boolean result = subject.matches("20151231");

        //assert
        assertTrue(result);
    }

    @Test(expected = AssertionError.class)
    public void testCreate_invalidDateString() throws Exception {

        //act
        matchesDateFormat("TTTT");
    }

    @Test
    public void testCreate_validDateString() throws Exception {

        //act
        DateFormatMatcher subject = matchesDateFormat("YYYY-MM-DD");

        //assert
        assertNotNull(subject);
        assertTrue(subject.matches("2015-12-31"));

    }

    @Test
    public void testDescribeTo() throws Exception {

        //prepare
        DateFormatMatcher subject = new DateFormatMatcher("YYYYMMDD");

        //act
        subject.describeTo(description);

        //assert
        verify(description).appendText("matches format YYYYMMDD");
    }

    @Test
    public void dateMatcherExample_match() throws Exception {
        //prepare
        String format = "YYYY-MM-DD hh:mm:ss.SSS";
        String date = "2015-12-31 13:15:10.123";
        //act
        assertThat(date, matchesDateFormat(format));

    }
    @Test
    public void dateMatcherExample_noMatch() throws Exception {
        //prepare
        String format = "YYYY-MM-DD hh:mm:ss.SSS";
        String date = "2015-12-31, 13:15:10.123";
        //act
        assertThat(date, not(matchesDateFormat(format)));

    }
}
