package ru.sssmaximusss.eventshandler.test;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sssmaximusss.eventshandler.Event;
import ru.sssmaximusss.eventshandler.EventHandler;
import ru.sssmaximusss.eventshandler.TimeSlot;

/**
 * Check a work with BigData (over 1M operation per second/some second)
 */
public class EventHandlerReadWriteBigDataTest {

    private EventHandler eventHandler;

    private static final Logger logger = Logger.getLogger(EventHandlerReadWriteBigDataTest.class);
    private static final long numberOfIterations = (long) 1e6;

    @Before
    public void setUp() {
        eventHandler = new EventHandler();
    }

    @Test
    public void testEventHandler() throws InterruptedException {

        Assert.assertNotNull(eventHandler);

        long startTime = System.nanoTime();
        for (int i = 0; i < numberOfIterations; i++) {
            eventHandler.addEvent(new Event("event without waiting", ""));
        }
        long endTime = System.nanoTime();
        logger.debug("Time of adding " + numberOfIterations + " events: " + ((endTime - startTime) / 1e6) + " ms.");

        logger.info("Number of events per last second: " + eventHandler.getNumberEvents(TimeSlot.SECOND));
        logger.info("Number of events per last minute: " + eventHandler.getNumberEvents(TimeSlot.MINUTE));

        startTime = System.nanoTime();
        for (int i = 0; i < numberOfIterations; i++) {
            if (i % 2 == 0) {
                eventHandler.getNumberEvents(TimeSlot.SECOND);
            } else {
                eventHandler.getNumberEvents(TimeSlot.MINUTE);
            }
        }
        endTime = System.nanoTime();
        logger.debug("Time of " + numberOfIterations + " readings: " + ((endTime - startTime) / 1e6) + " ms.");
    }
}
