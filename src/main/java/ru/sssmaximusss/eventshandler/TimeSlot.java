package ru.sssmaximusss.eventshandler;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This enum can be updated with a new time slot easily
 */
public enum TimeSlot {
    SECOND {
        @Override
        public Date getTimeOfBeginningTimeSlot() {
            return getTimeOfBeginningTimeSlot(-1);
        }

        @Override
        public Date getTimeOfBeginningTimeSlot(int amount) {
            return getUpdateDate(Calendar.SECOND, amount);
        }
    },
    MINUTE {
        @Override
        public Date getTimeOfBeginningTimeSlot() {
            return getTimeOfBeginningTimeSlot(-1);
        }

        @Override
        public Date getTimeOfBeginningTimeSlot(int amount) {
            return getUpdateDate(Calendar.MINUTE, amount);
        }
    },
    HOUR {
        @Override
        public Date getTimeOfBeginningTimeSlot() {
            return getTimeOfBeginningTimeSlot(-1);
        }

        @Override
        public Date getTimeOfBeginningTimeSlot(int amount) {
            return getUpdateDate(Calendar.HOUR, amount);
        }
    },
    DAY {
        @Override
        public Date getTimeOfBeginningTimeSlot() {
            return getTimeOfBeginningTimeSlot(-1);
        }

        @Override
        public Date getTimeOfBeginningTimeSlot(int amount) {
            return getUpdateDate(Calendar.DAY_OF_YEAR, amount);
        }
    };

    private static Date getUpdateDate(int timeSlot, int amount) {
        Calendar calendarForTimeSlot = new GregorianCalendar();
        calendarForTimeSlot.add(timeSlot, amount);
        return calendarForTimeSlot.getTime();
    }

    public abstract Date getTimeOfBeginningTimeSlot();

    public abstract Date getTimeOfBeginningTimeSlot(int amount);

}
