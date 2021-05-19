package hsa.maxist.se.geldautomat.data;


public class LogElement {

    private String effect;
    private String event;

    public LogElement(String event, String effect) {
        this.event = event;
        this.effect = effect;
    }

    public String getEffect() {
        return effect;
    }

    public String getEvent() {
        return event;
    }

}
