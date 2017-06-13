package com.example.somebody.eventmanager.entitiy;

import java.util.TimeZone;

public class Event {

    public static final long DEFAULT_CALENDAR_ID = 1L;
    public static final String DEFAULT_TIMEZONE = TimeZone.getDefault().getID();
    private static final String EMPTY_FIELD = "(Не указано)";

    private long id;
    private String title;
    private long calendarId;
    private long dateStart;
    private long dateEnd;
    private String timeZone;
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty())
            this.title = EMPTY_FIELD;
        else
            this.title = title;
    }

    public long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (calendarId != event.calendarId) return false;
        if (dateStart != event.dateStart) return false;
        if (dateEnd != event.dateEnd) return false;
        if (title != null ? !title.equals(event.title) : event.title != null) return false;
        if (timeZone != null ? !timeZone.equals(event.timeZone) : event.timeZone != null)
            return false;
        return description != null ? description.equals(event.description) : event.description == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (int) (calendarId ^ (calendarId >>> 32));
        result = 31 * result + (int) (dateStart ^ (dateStart >>> 32));
        result = 31 * result + (int) (dateEnd ^ (dateEnd >>> 32));
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
