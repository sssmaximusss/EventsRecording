package ru.sssmaximusss.eventshandler.test;

import org.apache.log4j.Logger;
import org.junit.Assert;
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
 * Basic test. A validation of all methods
 */
public class EventHandlerBasicTest {

    private EventHandler eventHandler;

    private static final Logger logger = Logger.getLogger(EventHandlerBasicTest.class);

    @Before
    public void setUp() {
        eventHandler = new EventHandler();
    }

    @Test
    public void testEventHandler() throws InterruptedException {

        Calendar generatorOfDate = new GregorianCalendar();
        List<Event> eventList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            eventList.add(new Event("event without waiting", ""));
        }

        for (int i = 0; i < 10; i++) {
            generatorOfDate.add(Calendar.SECOND, -1);
            eventList.add(new Event("event with waiting 1s", "", generatorOfDate.getTime()));
        }

        generatorOfDate = Calendar.getInstance();
        for (int i = 0; i < 30; i++) {
            generatorOfDate.add(Calendar.SECOND, -5);
            eventList.add(new Event("event with waiting 5s", "", generatorOfDate.getTime()));
        }

        for (int i = 0; i < 30; i++) {
            eventList.add(new Event("event without waiting", ""));
        }

        logger.debug("Size of events' list: " + eventList.size());

        Assert.assertNotNull(eventHandler);

        for (Event event : eventList) {
            eventHandler.addEvent(event);
        }

        logger.debug("All events into system:");
        logger.debug(eventHandler);

        // one sec delay
        Thread.sleep(1000);

        logger.info("Number of events per last second: " + eventHandler.getNumberEvents(TimeSlot.SECOND));
        logger.info("Number of events per last 2 second: " + eventHandler.getNumberEvents(TimeSlot.SECOND, 2));
        logger.info("Number of events per last minute: " + eventHandler.getNumberEvents(TimeSlot.MINUTE));
        logger.info("Number of events per last 2 minute: " + eventHandler.getNumberEvents(TimeSlot.MINUTE, 2));
        logger.info("Number of events per last hour: " + eventHandler.getNumberEvents(TimeSlot.HOUR));
        logger.info("Number of events per last day: " + eventHandler.getNumberEvents(TimeSlot.DAY));
    }
}
