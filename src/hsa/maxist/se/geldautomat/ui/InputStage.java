package hsa.maxist.se.geldautomat.ui;

import hsa.maxist.se.geldautomat.data.EventLog;
import hsa.maxist.se.geldautomat.data.LogElement;
import hsa.maxist.se.geldautomat.statemachine.eventhandling.EventHandler;
import hsa.maxist.se.geldautomat.statemachine.eventhandling.StateEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class InputStage extends Stage {

    private final TextField inputField;

    public InputStage(EventLog eventLog, Stage parent) {
        AnchorPane root = new AnchorPane();
        setScene(new Scene(root, parent.getWidth() * 1.5, parent.getHeight() * 0.9));
        setX(parent.getX() + parent.getWidth());
        setY(parent.getY());
        setTitle("State Info & User Input");
        initModality(Modality.WINDOW_MODAL);
        initOwner(parent);

        TableView<LogElement> eventList = new TableView<>();
        eventList.setItems(eventLog.getEvents());

        TableColumn<LogElement, String> eventCol = new TableColumn<>("Event");
        eventCol.setCellValueFactory(new PropertyValueFactory<>("event"));
        eventCol.setPrefWidth(200d);
        eventCol.setSortable(false);

        TableColumn<LogElement, String> effectCol = new TableColumn<>("Effect");
        effectCol.setCellValueFactory(new PropertyValueFactory<>("effect"));
        effectCol.setPrefWidth(300d);
        effectCol.setSortable(false);

        eventList.getColumns().add(eventCol);
        eventList.getColumns().add(effectCol);

        Button[] buttons = new Button[]{
                new Button(StateEvent.KARTE_EINGEBEN.getAction()),
                new Button(StateEvent.VALID.getAction()),
                new Button(StateEvent.INVALID.getAction()),
                new Button(StateEvent.KARTE_ENTNEHMEN.getAction()),
                new Button(StateEvent.GELD_ENTNEHMEN.getAction()),
                new Button(StateEvent.OK.getAction())
        };

        for(Button button : buttons) {
            button.setOnAction(EventHandler.getHandler());
        }

        inputField = new TextField();

        FlowPane inputPane = new FlowPane();
        inputPane.getChildren().addAll(buttons);
        inputPane.getChildren().add(inputField);

        AnchorPane.setTopAnchor(inputPane, 10d);
        AnchorPane.setLeftAnchor(inputPane, 10d);
        AnchorPane.setRightAnchor(inputPane, 10d);

        AnchorPane.setTopAnchor(eventList, 50d);
        AnchorPane.setBottomAnchor(eventList, 10d);
        AnchorPane.setLeftAnchor(eventList, 10d);
        AnchorPane.setRightAnchor(eventList, 10d);
        root.getChildren().addAll(inputPane, eventList);
    }

    public TextField getText() {
        return inputField;
    }

}
