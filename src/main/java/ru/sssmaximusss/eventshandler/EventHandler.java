package ru.sssmaximusss.eventshandler;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

public class EventHandler {

    /**
     * Data contains in treemap with key = Event, value = a number of events with similar key
     * and comparator, which compares a time of adding Event into system {@link EventsComparator#compare(Event, Event)}
     */
    private TreeMap<Event, Integer> events;

    private boolean isDebug = false;
    private static final Logger logger = Logger.getLogger(EventHandler.class);

    /**
     * Default constructor, based comparator = EventsComparator {@link EventsComparator}
     */
    public EventHandler() {
        events = new TreeMap<>(new EventsComparator());
    }

    /**
     * Add the event to TreeMap.
     * If TreeMap already contains the event, value will increment by 1
     *
     * @param event the input event
     */
    public void addEvent(final Event event) {
        Integer numberEvents = events.get(event);
        if (numberEvents == null) {
            numberEvents = 0;
        }

        events.put(event, numberEvents + 1);
    }

    /**
     * Get the number of events in system per time slot
     *
     * @param timeSlot the period of searching events {@link TimeSlot}
     * @return the number of events in system per time slot
     */
    public long getNumberEvents(TimeSlot timeSlot) {
        return getNumberEvents(timeSlot.getTimeOfBeginningTimeSlot());
    }

    /**
     * Get the number of events in system per time slot with a duration
     *
     * @param timeSlot the period (second, minute, hour, ...) of searching events {@link TimeSlot}
     * @param amount   the duration of a searching period
     * @return the number of events in system per time slot
     */
    public long getNumberEvents(TimeSlot timeSlot, int amount) {
        return getNumberEvents(timeSlot.getTimeOfBeginningTimeSlot(-amount));
    }

    /**
     * Get the number of events in system adding after startTimeSlot
     *
     * @param startTimeSlot the time is the beginning of time slot
     * @return the number of events in system per time slot
     */
    public long getNumberEvents(Date startTimeSlot) {

        if (events.isEmpty()) {
            return 0;
        }

        /**
         * get the key, after which all the events are interested by us
         */
        Event key = events.lowerKey(new Event(null, null, startTimeSlot));

        if (isDebug) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
            logger.debug("Starting time of time slot: " + dateFormat.format(startTimeSlot));
            logger.debug("Event with similar time in system: " + key);
        }

        /**
         * if time slot begins earlier than the earliest event in the system,
         * events.lowerKey() returns null. For this reason, I use the earliest event in the system
         * to get the number of all events
         */
        boolean inclusive = false;
        if (key == null) {
            key = events.firstKey();
            inclusive = true;
        }

        /**
         * get the submap to amount the number of needed events
         */
        SortedMap<Event, Integer> eventsPerTimeSlot = events.tailMap(key, inclusive);

        long numberEvents = 0;
        for (Integer numberEvent : eventsPerTimeSlot.values()) {
            numberEvents += numberEvent;
        }

        return numberEvents;
    }

    /**
     * For debugging
     *
     * @return all events in TreeMap
     */
    @Override
    public String toString() {
        StringBuilder handlerOutput = new StringBuilder();
        events.entrySet().forEach(eventIntegerEntry -> handlerOutput
                .append(eventIntegerEntry.getKey())
                .append(" : ")
                .append(eventIntegerEntry.getValue())
                .append("\n"));

        return handlerOutput.toString();
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    private class EventsComparator implements Comparator<Event> {

        @Override
        public int compare(Event event1, Event event2) {
            return event1.getTimeAdding().compareTo(event2.getTimeAdding());
        }
    }
}
