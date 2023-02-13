package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.model.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Objects;

public class GameOverController implements Controller {
    private final Game game;
    private final boolean won;
    private final App app;

    public GameOverController(Game game, boolean won, App app) {
        this.game = game;
        this.won = won;
        this.app = app;
    }

    @Override
    public String getTitle() {
        return "UNO - Game Over";
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        //Load FXML
        final Parent parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/GameOver.fxml")));

        final Label statusLabel = (Label) parent.lookup("#statusLabel");
        if (won) {
            statusLabel.setText("You Won! :)");
        } else {
            statusLabel.setText("You Lost :(");
        }

        final Button restartButton = (Button) parent.lookup("#restartButton");
        restartButton.setOnAction(action -> app.show(new IngameController(app, game.getPlayers().size() - 1, game.getPlayers().get(0).getName())));

        final Button menuButton = (Button) parent.lookup("#menuButton");
        menuButton.setOnAction(action -> app.show(new SetupController(app)));

        return parent;
    }

    @Override
    public void destroy() {

    }
}
