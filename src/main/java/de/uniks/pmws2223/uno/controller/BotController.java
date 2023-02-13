package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;

public class BotController implements Controller {
    private final App app;
    private final Game game;
    private final Player bot;
    private PropertyChangeListener cardListener;
    private PropertyChangeListener gameListener;

    public BotController(App app, Game game, Player bot) {
        this.app = app;
        this.game = game;

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

        botName.setTextFill(Color.color(0, 0, 0));
        botName.setText(bot.getName());

        gameListener = gameListener -> {
            if(bot.getGame() != null){
                System.out.println(bot.getName());
                botName.setTextFill(Color.color(1, 0, 0));
            }else {
                botName.setTextFill(Color.color(0, 0, 0));
            }

        };
        bot.listeners().addPropertyChangeListener(Player.PROPERTY_GAME, gameListener);


        //card listener
        cardListener = cardListener ->{
            cardCount.setText("Cards: " + bot.getCards().size());
            if(bot.getCards().size() == 0){
                app.show(new GameOverController(game, false, app));
            }
        };
        bot.listeners().addPropertyChangeListener(Player.PROPERTY_CARDS, cardListener);

        return parent;
    }

    @Override
    public void destroy() {
        bot.listeners().removePropertyChangeListener(Player.PROPERTY_CARDS, cardListener);
        bot.listeners().removePropertyChangeListener(Player.PROPERTY_GAME, gameListener);
    }
}
