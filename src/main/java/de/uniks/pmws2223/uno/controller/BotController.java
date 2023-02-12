package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.model.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;

public class BotController implements Controller{
    private Player bot;
    private PropertyChangeListener cardListener;

    public BotController(Player bot) {

        this.bot = bot;
    }

    @Override
    public String getTitle() {
        return "bot";
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        //Load FXML
        final Parent parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/Bot.fxml")));

        final Label botName = (Label) parent.lookup("#botName");

        final Label cardCount = (Label) parent.lookup("#cardCount");

        cardCount.setText("Cards: " + bot.getCards().size());
        botName.setText(bot.getName());

        //card listener
        cardListener = cardListener -> cardCount.setText("Cards: " + bot.getCards().size());
        bot.listeners().addPropertyChangeListener(Player.PROPERTY_CARDS, cardListener);

        return parent;
    }

    @Override
    public void destroy() {
        bot.listeners().removePropertyChangeListener(Player.PROPERTY_CARDS, cardListener);
    }
}
