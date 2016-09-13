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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Check a work read/write methods
 */
public class EventHandlerBigDataMultithreadsTest {

    private EventHandler eventHandler;

    private static final int numberThreads = 100;
    private static final int largeNumberEvents = (int) 1e6;
    private static final int smallNumberEvents = 10;
    private static final int numberReading = 150;


    private static final Logger logger = Logger.getLogger(EventHandlerBigDataMultithreadsTest.class);

    @Before
    public void setUp() {
        eventHandler = new EventHandler();
        //eventHandler.setDebug(true);
    }

    @Test
    public void testEventHandler() throws InterruptedException {

        /**
         * Create the writing Executor Service with a number threads = {@link numberThreads}
         */
        ExecutorService writeExecutor = Executors.newFixedThreadPool(numberThreads);

        /**
         * Create a large list of events. Number events = {@link largeNumberEvents}
         */
        List<Event> largeEventList = new ArrayList<>(largeNumberEvents);
        for (int i = 0; i < largeNumberEvents; i++) {
            largeEventList.add(new Event("LargeListEvents" + i, ""));
        }

        /**
         * Create a small list of events. Number events = {@link smallNumberEvents}
         */
        Calendar generatorDate = new GregorianCalendar();
        List<Event> smallEventList = new ArrayList<>(smallNumberEvents);
        for (int i = 0; i < smallNumberEvents; i++) {
            generatorDate.add(Calendar.HOUR, -1);
            smallEventList.add(new Event("SmallListEvents" + i, "", generatorDate.getTime()));
        }

        /**
         * Execute 5 threads with large number events and 5 threads with small number events
         */
        for (int i = 0; i < numberThreads; i = i + 2) {
            writeExecutor.submit((Runnable) () -> largeEventList.forEach(event -> eventHandler.addEvent(event)));
            writeExecutor.submit((Runnable) () -> smallEventList.forEach(event -> eventHandler.addEvent(event)));
        }



        /**
         * Create the reading Executor Service with a single thread
         */
        ExecutorService readExecutor = Executors.newSingleThreadExecutor();

        List<TimeSlot> readList = new ArrayList<>();
        for (int i = 0; i < numberReading; i++) {
            readList.add(generateTimeSlot(i));
        }

        /**
         * Execute a thread with several number of reading {@link numberReading}
         */
        readExecutor.submit((Runnable) () -> readList.forEach(timeSlot -> logger.info("Number of events per last " + timeSlot + " : " + eventHandler.getNumberEvents(timeSlot))));

        /**
         * Shutdown writing ExecutorService
         */
        writeExecutor.shutdown();
        boolean done = writeExecutor.awaitTermination(20, TimeUnit.SECONDS);
        logger.info("All events were added. " + done);
        
        /**
         * Shutdown reading ExecutorService
         */
        readExecutor.shutdown();
        done = readExecutor.awaitTermination(10, TimeUnit.SECONDS);
        logger.info("All request were executed. " + done);


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
