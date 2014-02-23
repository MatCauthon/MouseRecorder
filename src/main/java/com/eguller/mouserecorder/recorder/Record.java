package com.eguller.mouserecorder.recorder;

import com.eguller.mouserecorder.recorder.event.DelayEvent;
import com.eguller.mouserecorder.recorder.event.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * User: eguller
 * Date: 2/22/14
 * Time: 12:06 AM
 */
public class Record {
    private List<Event> eventList = new ArrayList<Event>();
    long lastEvent;
    public Record(){
        this.lastEvent = System.currentTimeMillis();
    }

    public void post(Event event){
        addDelayEvent();
        eventList.add(event);
    }

    public void add(Event event){
        eventList.add(event);
    }

    private void addDelayEvent() {
        long elapsed = System.currentTimeMillis() - lastEvent;
        lastEvent = System.currentTimeMillis();
        if(elapsed > 0){
            eventList.add(new DelayEvent(elapsed));
        }
    }

    public List<Event> getEventList(){
         return eventList;
    }
}
