package com.technical;

import com.technical.cal.Calendar;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Iterator;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by Robert Piotrowski on 03/10/2016.
 */
public class CalendarTests {

    @Test
    public void testIteratorExists() {
        Calendar calendar = new Calendar(LocalDate.now());
        assertThat(calendar.iterator()).isNotNull();
    }

    @Test
    public void testIteratorNext() {
        Calendar calendar = new Calendar(LocalDate.now());
        assertThat(calendar.iterator().hasNext()).isNotNull();
    }

    @Test
    public void testGetStartDateCalendar() {
        Calendar calendar = new Calendar(LocalDate.now());
        assertThat(calendar.getStartDate()).isNotNull();
    }

//
//    @Test
//    public void testCalendarCollections() {
//        Calendar calendar = new Calendar(LocalDate.now());
//
//        Iterator it = calendar.iterator();
//        List<LocalDate> temp = new ArrayList<LocalDate>();
//        temp.add((LocalDate) it.next());
//        temp.add((LocalDate) it.next());
//        assertThat(temp).
//
//    }


    @Test
    public void testCalendarMoreThanOneIterators() {
        Calendar calendar = new Calendar(LocalDate.now());

        Iterator firstIterator = calendar.iterator();
        Iterator secondIterator = calendar.iterator();

        assertThat(firstIterator.next()).isEqualTo(secondIterator.hasNext());
        assertThat(firstIterator.next()).isEqualTo(secondIterator.hasNext());
        assertThat(firstIterator.next()).isEqualTo(secondIterator.hasNext());
    }

}
