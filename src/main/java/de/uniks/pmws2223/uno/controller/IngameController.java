package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class IngameController implements Controller{
    private App app;
    private int botCount;
    private String playerName;

    public IngameController(App app, int bots, String text) {

        this.app = app;
        botCount = bots;
        playerName = text;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        //Load FXML
        final Parent parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/Ingame.fxml")));

        return parent;
    }

    @Override
    public void destroy() {

    }
}
