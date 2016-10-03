package com.technical.cal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Iterator;

/**
 * Created by Robert Piotrowski on 03/10/2016.
 */

public class Calendar implements Iterable<LocalDate> {

    private LocalDate startDate;

    public Calendar(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public Iterator<LocalDate> iterator() {
        Iterator<LocalDate> it = new Iterator<LocalDate>() {

            LocalDate state = startDate;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public LocalDate next() {
                if (state.getDayOfWeek().equals(DayOfWeek.THURSDAY))
                    state = state.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
                else
                    state = state.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
                return state;
            }
        };
        return it;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
