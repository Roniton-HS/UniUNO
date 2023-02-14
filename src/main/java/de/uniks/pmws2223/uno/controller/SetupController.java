package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class SetupController implements Controller {

    //final objects
    private final App app;

    public SetupController(App app) {
        this.app = app;
    }

    @Override
    public String getTitle() {
        return "UNO - Setup";
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        //Load FXML
        final Parent parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/Setup.fxml")));

        //bot count display
        final Label botCount = (Label) parent.lookup("#botCount");

        // plus-minus buttons
        final Button plusButton = (Button) parent.lookup("#plusButton");
        plusButton.setOnAction(action -> {
            if (botCount.getText().equals("1")) {
                botCount.setText("2");
            } else if (botCount.getText().equals("2")) {
                botCount.setText("3");
            }
        });

        final Button minusButton = (Button) parent.lookup("#minusButton");
        minusButton.setOnAction(action -> {
            if (botCount.getText().equals("2")) {
                botCount.setText("1");
            } else if (botCount.getText().equals("3")) {
                botCount.setText("2");
            }
        });

        //seeded checkbox
        final CheckBox seedBox = (CheckBox) parent.lookup("#seedBox");

        //text field for player
        final TextField nameField = (TextField) parent.lookup("#nameField");

        //start button
        final Button startButton = (Button) parent.lookup("#startButton");
        startButton.setOnAction(action ->{
            if(!nameField.getText().equals("")){
                int bots = Integer.parseInt(botCount.getText());
                    app.show(new IngameController(app, bots, nameField.getText(), seedBox.isSelected()));
            }
        });

        return parent;
    }

    @Override
    public void destroy() {

    }
}
