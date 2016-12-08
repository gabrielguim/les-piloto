package com.example.semtempo;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        Calendar cal = new GregorianCalendar();

        System.out.println(cal.getTime().toString());


    }
}