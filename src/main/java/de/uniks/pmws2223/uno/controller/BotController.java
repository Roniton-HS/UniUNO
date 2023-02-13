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
    //final objects
    private final App app;
    private final Game game;
    private final Player bot;

    //listener
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

        //label for the bots name
        final Label botName = (Label) parent.lookup("#botName");
        botName.setTextFill(Color.color(0, 0, 0));
        botName.setText(bot.getName());

        //changes the color of the bots name to red if it's his turn
        gameListener = gameListener -> {
            if(bot.getGame() != null){
                System.out.println(bot.getName());
                botName.setTextFill(Color.color(1, 0, 0));
            }else {
                botName.setTextFill(Color.color(0, 0, 0));
            }
        };
        bot.listeners().addPropertyChangeListener(Player.PROPERTY_GAME, gameListener);

        //label for how many cards the bot owns
        final Label cardCount = (Label) parent.lookup("#cardCount");
        cardCount.setText("Cards: " + bot.getCards().size());

        //card listener
        cardListener = cardListener ->{

            //update card count
            cardCount.setText("Cards: " + bot.getCards().size());

            //check if bot won
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
