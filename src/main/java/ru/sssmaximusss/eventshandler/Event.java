package ru.sssmaximusss.eventshandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * We can make class Event abstract and create a lot of classes of event with different types
 */
public class Event {
    private String title;
    private String description;
    private Date timeAdding;

    public Event(String title, String description) {
        this(title, description, Calendar.getInstance().getTime());
    }

    public Event(String title, String description, Date timeAdding) {
        this.title = title;
        this.description = description;
        this.timeAdding = timeAdding;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getTimeAdding() {
        return timeAdding;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", timeAdding=" + dateFormat.format(timeAdding) +
                '}';
    }
}
