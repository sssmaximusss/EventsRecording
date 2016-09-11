package ru.sssmaximusss.eventshandler.test;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sssmaximusss.eventshandler.Event;
import ru.sssmaximusss.eventshandler.EventHandler;
import ru.sssmaximusss.eventshandler.TimeSlot;

/**
 * Check a work system without events
 */
public class EventHandlerWithoutEventsTest {

    private EventHandler eventHandler;

    private static final Logger logger = Logger.getLogger(EventHandlerWithoutEventsTest.class);

    @Before
    public void setUp() {
        eventHandler = new EventHandler();
    }

    @Test
    public void testEventHandler() throws InterruptedException {

        Assert.assertNotNull(eventHandler);

        logger.debug("All events into system:");
        logger.debug(eventHandler);

        logger.info("Number of events per last second: " + eventHandler.getNumberEvents(TimeSlot.SECOND));
        logger.info("Number of events per last minute: " + eventHandler.getNumberEvents(TimeSlot.MINUTE));
        logger.info("Number of events per last hour: " + eventHandler.getNumberEvents(TimeSlot.HOUR));
        logger.info("Number of events per last day: " + eventHandler.getNumberEvents(TimeSlot.DAY));

        eventHandler.addEvent(new Event("", ""));

        logger.debug("All events into system:");
        logger.debug(eventHandler);

        logger.info("Number of events per last second: " + eventHandler.getNumberEvents(TimeSlot.SECOND));
        logger.info("Number of events per last minute: " + eventHandler.getNumberEvents(TimeSlot.MINUTE));
        logger.info("Number of events per last hour: " + eventHandler.getNumberEvents(TimeSlot.HOUR));
        logger.info("Number of events per last day: " + eventHandler.getNumberEvents(TimeSlot.DAY));
    }
}
