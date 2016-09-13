package ru.sssmaximusss.eventshandler.test;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ru.sssmaximusss.eventshandler.Event;
import ru.sssmaximusss.eventshandler.EventHandler;
import ru.sssmaximusss.eventshandler.TimeSlot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Check a work read/write methods
 */
public class EventHandlerReadWriteLockingTest {

    private EventHandler eventHandler;

    private static final int numberThreads = 100;

    private static final Logger logger = Logger.getLogger(EventHandlerReadWriteLockingTest.class);

    @Before
    public void setUp() {
        eventHandler = new EventHandler();
        eventHandler.setDebug(true);
    }

    @Test
    public void testEventHandler() throws InterruptedException {

        /**
         * Create threads to write
         */
        List<Thread> writeThreads = new ArrayList<>();
        for (int i = 0; i < numberThreads; i++) {
            Calendar generatorDate = new GregorianCalendar();
            generatorDate.add(Calendar.SECOND, -2);
            writeThreads.add(new Thread(() -> eventHandler.addEvent(new Event("Writer", "", generatorDate.getTime())), "Writer" + i));
        }

        /**
         * Create threads to read
         */
        List<Thread> readThreads = new ArrayList<>(numberThreads);
        for (int i = 0; i < numberThreads; i++) {
            final TimeSlot timeSlot = generateTimeSlot(i);

            readThreads.add(new Thread(() -> logger.info("Number of events per last " + timeSlot + " : " + eventHandler.getNumberEvents(timeSlot))
                    , "Reader" + timeSlot + i));
        }

        /**
         * Stare threads to read and write. The writing threads finish firstly.
         */
        writeThreads.forEach(Thread::start);
        readThreads.forEach(Thread::start);


        /**
         * Create and start new writing threads
         */
        for (int i = 0; i < numberThreads / 2; i++) {
            Calendar generatorDate = new GregorianCalendar();
            generatorDate.add(Calendar.SECOND, -2);
            new Thread(() -> eventHandler.addEvent(new Event("Writer", "", generatorDate.getTime())), "Writer" + (numberThreads + i)).start();
        }

        /**
         * Create and start new reading threads
         */
        for (int i = 0; i < numberThreads / 4; i++) {
            final TimeSlot timeSlot = generateTimeSlot(i);
            new Thread(() -> logger.info("Number of events per last " + timeSlot + " : " + eventHandler.getNumberEvents(timeSlot))
                    , "Reader" + timeSlot + (numberThreads + i)).start();

        }

        Thread.sleep(5000);
        logger.debug("All events into system:");
        logger.debug(eventHandler);
    }

    private TimeSlot generateTimeSlot(int i) {
        switch (i % 4) {
            case 0:
                return TimeSlot.SECOND;
            case 1:
                return TimeSlot.MINUTE;
            case 2:
                return TimeSlot.HOUR;
            case 3:
                return TimeSlot.DAY;
        }
        return TimeSlot.SECOND;
    }
}
