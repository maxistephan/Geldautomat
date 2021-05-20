package hsa.maxist.se.geldautomat.statemachine.eventhandling;

public enum StateEvent {

    START("start"),
    VALID("valid"),
    INVALID("invalid"),
    KARTE_EINGEBEN("Karte eingeben"),
    OK("Ok"),
    AFTER("After(15s)"),
    GELD_ENTNEHMEN("Geld entnehmen"),
    KARTE_ENTNEHMEN("Karte entnehmen");

    private final String action;

    StateEvent(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

}
