package hsa.maxist.se.geldautomat;

import hsa.maxist.se.geldautomat.data.EventLog;
import hsa.maxist.se.geldautomat.statemachine.StateMachine;
import hsa.maxist.se.geldautomat.statemachine.eventhandling.EventHandler;
import hsa.maxist.se.geldautomat.ui.DisplayStage;
import hsa.maxist.se.geldautomat.ui.InputStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // --Instantiate Logs
        EventLog eventLog = new EventLog();

        // --Display
        DisplayStage display = new DisplayStage();
        display.show();
        // --Input
        InputStage input = new InputStage(eventLog, display);
        input.show();

        // --StateMachine
        StateMachine stateMachine = StateMachine.createInstance(eventLog, display.getCurrentStateMessage(), input.getText().textProperty());

        // -Add StateMachine to Receivers
        EventHandler.addReceiver(stateMachine);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
