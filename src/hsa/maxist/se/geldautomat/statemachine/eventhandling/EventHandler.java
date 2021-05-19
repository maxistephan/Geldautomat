package hsa.maxist.se.geldautomat.statemachine.eventhandling;

import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

public class EventHandler implements javafx.event.EventHandler<ActionEvent> {

    private static List<Receiver> receivers;
    private static final EventHandler instance = new EventHandler();

    private EventHandler() {
        receivers = new ArrayList<>(1);
    }

    public static void addReceiver(Receiver receiver) {
        receivers.add(receiver);
    }

    public static javafx.event.EventHandler<ActionEvent> getHandler() {
        return instance;
    }

    @Override
    public void handle(ActionEvent event) {
        for(Receiver receiver : receivers) {
            receiver.receive(event);
        }
    }
}
