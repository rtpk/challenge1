package com.technical.cal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Iterator;

public class Calendar implements Iterable<LocalDate> {

    private LocalDate startDate;

    public Calendar(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return new Iterator<LocalDate>() {

            LocalDate state = startDate;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public LocalDate next() {
                if (state.getDayOfWeek().equals(DayOfWeek.TUESDAY) || state.getDayOfWeek().equals(DayOfWeek.WEDNESDAY) || state.getDayOfWeek().equals(DayOfWeek.THURSDAY))
                    state = state.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
                else
                    state = state.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
                return state;
            }
        };
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
