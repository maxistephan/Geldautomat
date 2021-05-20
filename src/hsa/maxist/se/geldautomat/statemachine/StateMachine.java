package hsa.maxist.se.geldautomat.statemachine;

import hsa.maxist.se.geldautomat.data.EventLog;
import hsa.maxist.se.geldautomat.data.LogElement;
import hsa.maxist.se.geldautomat.statemachine.eventhandling.Receiver;
import hsa.maxist.se.geldautomat.statemachine.eventhandling.StateEvent;
import static hsa.maxist.se.geldautomat.statemachine.eventhandling.StateEvent.*;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StateMachine implements Receiver {

    private static EventLog eventLog;
    private static int tries = 0;
    private static Label display;
    private static List<String> currentEffects;
    private static StringProperty numInput;
    private static String betrag;

    private static State currentState;

    private StateMachine() {
        currentState = State.READY;
        currentState.entry.invoke();
        currentState.doWhile.invoke();
    }

    public static StateMachine createInstance(EventLog logs, Label displayLabel, StringProperty textProperty) {
        eventLog = logs;
        numInput = textProperty;
        display = displayLabel;
        display.setText("Bereit");
        return new StateMachine();
    }

    public void receive(ActionEvent e) {
        Object source = e.getSource();
        String eventName = source instanceof Button
                ? ((Button)source).getText()
                : source.toString();

        apply(Arrays.stream(StateEvent.values())
                .filter(t -> t.getAction().equals(eventName))
                .findAny()
                .orElse(null)
        );

        eventLog.log(new LogElement(eventName, currentEffects.toString()));
        e.consume();
    }

    public void apply(StateEvent event) {

        currentEffects = new ArrayList<>();

        switch(currentState) {
            case READY -> {
                if(event.equals(KARTE_EINGEBEN)) {
                    prKarte();
                    currentState = State.CARD_VALIDITY;
                    currentState.entry.invoke();
                }
            }

            case CARD_VALIDITY -> {
                if(event.equals(VALID)) {
                    tries = 0;
                    currentState = State.PIN_INPUT;
                    currentState.entry.invoke();
                } else if(event.equals(INVALID)) {
                    karteAusgeben();
                    currentState = State.READY;
                    currentState.entry.invoke();
                }
            }

            case PIN_INPUT -> {
                if(event.equals(OK)) {
                    prPIN();
                    currentState = State.PIN_VALIDITY;
                    currentState.entry.invoke();
                }
            }

            case PIN_VALIDITY -> {
                if(event.equals(VALID)) {
                    currentState = State.VALUE_INPUT;
                    currentState.entry.invoke();
                } else if(event.equals(INVALID)) {
                    if(tries < 3)
                        currentState = State.PIN_INPUT;
                    else {
                        karteAusgeben();
                        currentState = State.READY;
                    }
                    currentState.entry.invoke();
                }
            }

            case VALUE_INPUT -> {
                if(event.equals(OK)) {
                    prBetrag();
                    currentState = State.VALUE_VALIDITY;
                    currentState.entry.invoke();
                }
            }

            case VALUE_VALIDITY -> {
                if(event.equals(VALID)) {
                    kontoBelasten();
                    currentState = State.CARD_OUTPUT;
                    currentState.entry.invoke();
                } else if(event.equals(INVALID)) {
                    currentState = State.VALUE_INPUT;
                    currentState.entry.invoke();
                }
            }

            case CARD_OUTPUT -> {
                if(event.equals(KARTE_ENTNEHMEN)) {
                    currentState = State.MONEY_OUTPUT;
                    currentState.entry.invoke();
                } else if(event.equals(AFTER)) {
                    karteEinziehen();
                    buchungStornieren();
                    currentState = State.READY;
                    currentState.entry.invoke();
                }
            }

            case MONEY_OUTPUT -> {
                if(event.equals(GELD_ENTNEHMEN)) {
                    currentState = State.READY;
                    currentState.entry.invoke();
                }
            }
        }
        currentState.doWhile.invoke();
    }

    private static void prKarte() {
        currentEffects.add("Pr\u00fcfe Karte");
    }

    private static void prPIN() {
        currentEffects.add("Pr\u00fcfe PIN: " + numInput.get());
        numInput.setValue("");
    }

    private static void karteAusgeben() {
        currentEffects.add("Karte ausgeben");
        display.setText("Bitte entnehmen Sie Ihre Karte");
    }

    private static void geldBereitstellen() {
        currentEffects.add("Geld bereitstellen");
    }

    private static void prBetrag() {
        betrag = numInput.get();
        numInput.setValue("");
        currentEffects.add("Pr\u00fcfe Betrag: " + betrag);
    }

    private static void kontoBelasten() {
        currentEffects.add("Konto belastet mit " + betrag);
    }

    private static void karteEinziehen() {
        currentEffects.add("Karte Einziehen");
    }

    private static void buchungStornieren() {
        currentEffects.add("Buchung wird storniert. Betrag " + betrag + " wird nicht abgebucht");
    }

    // --State config
    private enum State {
        READY(null, () -> display.setText("Bitte Karte Eingeben")),
        CARD_VALIDITY(null, null),
        PIN_INPUT(null, () -> display.setText("Bitte geben Sie Ihren PIN ein")),
        PIN_VALIDITY(() -> tries++, () -> display.setText("Bitte warten")),
        VALUE_INPUT(null, () -> display.setText("Bitte geben Sie einen Betrag ein")),
        VALUE_VALIDITY(null, () -> display.setText("Bitte warten")),
        CARD_OUTPUT(StateMachine::karteAusgeben, () -> display.setText("Bitte entnehmen Sie Ihre Karte")),
        MONEY_OUTPUT(StateMachine::geldBereitstellen, () -> display.setText("Bitte entnehmen Sie Ihr Geld"));

        public final ActionPerformer entry;
        public final ActionPerformer doWhile;

        State(ActionPerformer entry, ActionPerformer doWhile) {
            ActionPerformer NOTHING = () -> {
                // --Do Nothing
            };
            this.entry = entry == null ? NOTHING : entry;
            this.doWhile = doWhile == null ? NOTHING : doWhile;
        }
    }

}
