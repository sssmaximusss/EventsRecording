package ru.sssmaximusss.eventshandler.test;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sssmaximusss.eventshandler.Event;
import ru.sssmaximusss.eventshandler.EventHandler;
import ru.sssmaximusss.eventshandler.TimeSlot;

/**
 * Check a work read/write methods
 */
public class EventHandlerReadWriteTest {

    private EventHandler eventHandler;

    private static final Logger logger = Logger.getLogger(EventHandlerReadWriteTest.class);

    @Before
    public void setUp() {
        eventHandler = new EventHandler();
    }

    @Test
    public void testEventHandler() throws InterruptedException {

        Assert.assertNotNull(eventHandler);

        for (int i = 0; i < 20; i++) {
            eventHandler.addEvent(new Event("event without waiting", ""));
        }

        logger.info("Number of events per last second: " + eventHandler.getNumberEvents(TimeSlot.SECOND));

        for (int i = 0; i < 20; i++) {
            eventHandler.addEvent(new Event("event without waiting", ""));
            logger.info("Number of events per last second: " + eventHandler.getNumberEvents(TimeSlot.SECOND));
        }

        logger.debug("All events into system:");
        logger.debug(eventHandler);
    }
}
