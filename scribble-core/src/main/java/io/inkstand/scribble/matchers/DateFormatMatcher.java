package io.inkstand.scribble.matchers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Created by Gerald Muecke on 03.12.2015.
 */
public class DateFormatMatcher extends BaseMatcher<String>{

    private final DateFormat dateFormat;
    private final String strDateFormat;

    public DateFormatMatcher(final String dateFormatPattern) {
        try {
            this.strDateFormat = dateFormatPattern;
            this.dateFormat = new SimpleDateFormat(dateFormatPattern);
        } catch (IllegalArgumentException e){
            throw new AssertionError("Invalid Date Format " + dateFormatPattern,e);
        }
    }

    public static DateFormatMatcher matchesDateFormat(String dateFormatPattern){
        return new DateFormatMatcher(dateFormatPattern);
    }

    @Override
    public boolean matches(final Object o) {
        if(o instanceof String){
            return matchesDateString((String)o);
        }
        return false;
    }

    private boolean matchesDateString(final String dateString) {

        try {
            dateFormat.parse(dateString);
            return true;
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("matches format " + strDateFormat.toString());
    }
}
