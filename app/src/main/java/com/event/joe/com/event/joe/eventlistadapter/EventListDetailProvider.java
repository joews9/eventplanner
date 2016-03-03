package com.event.joe.com.event.joe.eventlistadapter;

/**
 * Created by Joe Aimee and Baby on 16/02/2016.
 */
public class EventListDetailProvider {

    private String eventTitle;
    private String eventDate;

    public EventListDetailProvider(String eventTitle, String eventDate){
        this.setEventDate(eventDate);
        this.setEventTitle(eventTitle);
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
