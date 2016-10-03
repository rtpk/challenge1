package com.technical.command;

import com.technical.cal.Calendar;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Iterator;

/**
 * Created by Robert Piotrowski on 03/10/2016.
 */
@Component
public class MyRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Calendar calendar = new Calendar(LocalDate.now());

        Iterator it = calendar.iterator();
        for(int i =0; i< 100 ;i ++)
        {
            System.out.println( it.next());
        }
        System.out.println("calendar.iterator().next() = " + it.next());
    }
}