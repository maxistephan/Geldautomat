package hsa.maxist.se.geldautomat.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;

public class DisplayStage extends Stage {

    private final Label currentStateMessage;

    public DisplayStage() {

        BorderPane root = new BorderPane();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setTitle("Geldautomat");
        setScene(new Scene(root));
        setWidth(screenSize.getWidth() / 3);
        setHeight(screenSize.getHeight() / 2);
        setY(screenSize.getHeight() / 2 - getHeight() / 2);

        currentStateMessage = new Label();
        currentStateMessage.setFont(new Font("Arial", 30));

        BorderPane.setAlignment(currentStateMessage, Pos.CENTER);
        root.setCenter(currentStateMessage);

    }

    public Label getCurrentStateMessage() {
        return currentStateMessage;
    }

}
