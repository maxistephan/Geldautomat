package hsa.maxist.se.geldautomat.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Iterator;

public class EventLog implements Iterable<LogElement> {

    private final ObservableList<LogElement> events;

    public EventLog() {
        events = FXCollections.observableArrayList();
    }

    public void log(LogElement eventLog) {
        events.add(eventLog);
    }

    public ObservableList<LogElement> getEvents() {
        return events;
    }

    @Override
    public Iterator<LogElement> iterator() {
        return events.iterator();
    }
}
