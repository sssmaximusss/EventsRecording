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
 * Check a work with the artificially generated data
 */
public class EventWithGeneratedDateTest {

    private EventHandler eventHandler;

    private static final int numberIterationSecMin = 59;
    private static final int numberIterationHour = 23;

    private static final Logger logger = Logger.getLogger(EventWithGeneratedDateTest.class);

    @Before
    public void setUp() {
        eventHandler = new EventHandler();
    }

    @Test
    public void testEventHandler() throws InterruptedException {

        Calendar generatorOfDate = new GregorianCalendar();

        List<Event> eventList = new ArrayList<>();
        for (int i = 0; i < numberIterationSecMin; i++) {
            generatorOfDate.add(Calendar.MILLISECOND, -1);
            eventList.add(new Event("msec", "", generatorOfDate.getTime()));
        }

        generatorOfDate = Calendar.getInstance();
        for (int i = 0; i < numberIterationSecMin; i++) {
            generatorOfDate.add(Calendar.SECOND, -1);
            eventList.add(new Event("sec", "", generatorOfDate.getTime()));
        }

        generatorOfDate = Calendar.getInstance();
        for (int i = 0; i < numberIterationSecMin; i++) {
            generatorOfDate.add(Calendar.MINUTE, -1);
            eventList.add(new Event("min", "", generatorOfDate.getTime()));
        }

        generatorOfDate = Calendar.getInstance();
        for (int i = 0; i < numberIterationHour; i++) {
            generatorOfDate.add(Calendar.HOUR, -1);
            eventList.add(new Event("h", "", generatorOfDate.getTime()));
        }

        logger.debug("Size of events' list: " + eventList.size());
        Assert.assertNotNull(eventHandler);

        for (Event event : eventList) {
            eventHandler.addEvent(event);
        }

        logger.debug("All events into system:");
        logger.debug(eventHandler);

        logger.info("Number of events per last second: " + eventHandler.getNumberEvents(TimeSlot.SECOND));
        logger.info("Number of events per last 2 second: " + eventHandler.getNumberEvents(TimeSlot.SECOND, 2));
        logger.info("Number of events per last minute: " + eventHandler.getNumberEvents(TimeSlot.MINUTE));
        logger.info("Number of events per last 2 minute: " + eventHandler.getNumberEvents(TimeSlot.MINUTE, 2));
        logger.info("Number of events per last hour: " + eventHandler.getNumberEvents(TimeSlot.HOUR));
        logger.info("Number of events per last day: " + eventHandler.getNumberEvents(TimeSlot.DAY));

        Assert.assertEquals("Number of events per last second: ", eventHandler.getNumberEvents(TimeSlot.SECOND), numberIterationSecMin);
        Assert.assertEquals("Number of events per last 2 second: ", eventHandler.getNumberEvents(TimeSlot.SECOND, 2), numberIterationSecMin + 1);
        Assert.assertEquals("Number of events per last minute: ", eventHandler.getNumberEvents(TimeSlot.MINUTE), 2 * numberIterationSecMin);
        Assert.assertEquals("Number of events per last 2 minute: ", eventHandler.getNumberEvents(TimeSlot.MINUTE, 2), 2 * numberIterationSecMin + 1);
        Assert.assertEquals("Number of events per last hour: ", eventHandler.getNumberEvents(TimeSlot.HOUR), 3 * numberIterationSecMin);
        Assert.assertEquals("Number of events per last day: ", eventHandler.getNumberEvents(TimeSlot.DAY), 3 * numberIterationSecMin + numberIterationHour);
    }
}
