package com.example.somebody.eventmanager.entitiy;

import java.util.TimeZone;

public class Event {

    public static final long DEFAULT_CALENDAR_ID = 1L;
    public static final String DEFAULT_TIMEZONE = TimeZone.getDefault().getID();

    private Long id;
    private String title;
    private Long calendarId;
    private Long dateStart;
    private Long dateEnd;
    private String timeZone;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    public Long getDateStart() {
        return dateStart;
    }

    public void setDateStart(Long dateStart) {
        this.dateStart = dateStart;
    }

    public Long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Long dateEnd) {
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

        if (!id.equals(event.id)) return false;
        if (title != null ? !title.equals(event.title) : event.title != null) return false;
        if (!dateStart.equals(event.dateStart)) return false;
        if (!dateEnd.equals(event.dateEnd)) return false;
        return description != null ? description.equals(event.description) : event.description == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + dateStart.hashCode();
        result = 31 * result + dateEnd.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
