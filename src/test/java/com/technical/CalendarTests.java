package com.technical;

import com.technical.cal.Calendar;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        assertThat(calendar.iterator().hasNext()).isTrue();
    }

    @Test
    public void testGetStartDateCalendar() {
        Calendar calendar = new Calendar(LocalDate.now());
        assertThat(calendar.getStartDate()).isNotNull();
    }

    @Test
    public void testCalendarCollections() {
        Calendar calendar = new Calendar(LocalDate.of(2016,9,19));

        Iterator it = calendar.iterator();
        List<LocalDate> temp = new ArrayList<>();
        temp.add((LocalDate) it.next());
        temp.add((LocalDate) it.next());
        temp.add((LocalDate) it.next());
        temp.add((LocalDate) it.next());
        assertThat(temp).contains(LocalDate.of(2016,9,20),LocalDate.of(2016,9,23),LocalDate.of(2016,9,27),LocalDate.of(2016,9,30));

    }

    @Test
    public void testCalendarMoreThanOneIterators() {
        Calendar calendar = new Calendar(LocalDate.now());

        Iterator firstIterator = calendar.iterator();
        Iterator secondIterator = calendar.iterator();

        assertThat(firstIterator.next()).isEqualTo(secondIterator.next());
    }

}
