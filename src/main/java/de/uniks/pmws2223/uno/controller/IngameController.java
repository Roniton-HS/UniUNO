package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;
import de.uniks.pmws2223.uno.service.GameService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;

public class IngameController implements Controller {
    private App app;
    private int botCount;
    private String playerName;
    private Game game = new Game();

    private PropertyChangeListener cardListener;
    private PropertyChangeListener currentCardListener;

    private final GameService gameService = new GameService();

    public IngameController(App app, int bots, String text) {

        this.app = app;
        botCount = bots;
        playerName = text;
    }

    @Override
    public String getTitle() {
        return "UNO - Ingame";
    }

    @Override
    public void init() {

        game.withPlayers(new Player().setName(playerName).setBot(false));

        for (int i = 0; i < botCount; i++) {
            game.withPlayers(new Player().setName("Bot " + i).setBot(true).withCards(new Card()));
        }

        game.setCurrentCard(gameService.drawCard());
    }

    @Override
    public Parent render() throws IOException {
        //Load FXML
        final Parent parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/Ingame.fxml")));

        renderBots(parent);

        //quit button
        final Button quitButton = (Button) parent.lookup("#quitButton");
        quitButton.setOnAction(action -> app.show(new SetupController(app)));

        //draw button
        final Button drawButton = (Button) parent.lookup("#drawButton");
        drawButton.setOnAction(action -> game.getPlayers().get(0).withCards(gameService.drawCard()));

        final HBox humanCards = (HBox) parent.lookup("#humanCards");

        for (Card c : game.getPlayers().get(0).getCards()) {
            humanCards.getChildren().add(new CardController(game, c).render());
        }

        //card listener
        cardListener = cardListener -> {
            humanCards.getChildren().clear();
            for (Card c : game.getPlayers().get(0).getCards()) {
                try {
                    humanCards.getChildren().add(new CardController(game, c).render());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        game.getPlayers().get(0).listeners().addPropertyChangeListener(Player.PROPERTY_CARDS, cardListener);

        //first render current card
        final VBox discardPile = (VBox) parent.lookup("#discardPile");
        discardPile.getChildren().add(new CardController(game, game.getCurrentCard()).render());

        //current card listener
        currentCardListener = currentCardListener ->{
            discardPile.getChildren().clear();
            try {
                discardPile.getChildren().add(new CardController(game, game.getCurrentCard()).render());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        game.listeners().addPropertyChangeListener(Game.PROPERTY_CURRENT_CARD, currentCardListener);

        return parent;
    }

    public void renderBots(Parent parent) throws IOException {

        //first render bots
        final VBox botSpace1 = (VBox) parent.lookup("#botSpace1");
        botSpace1.getChildren().add(new BotController(game.getPlayers().get(1)).render());

        if (botCount > 1) {
            final VBox botSpace2 = (VBox) parent.lookup("#botSpace2");
            botSpace2.getChildren().add(new BotController(game.getPlayers().get(2)).render());
        }

        if (botCount > 2) {
            final VBox botSpace3 = (VBox) parent.lookup("#botSpace3");
            botSpace3.getChildren().add(new BotController(game.getPlayers().get(2)).render());
        }
    }

    @Override
    public void destroy() {
        game.getPlayers().get(0).listeners().removePropertyChangeListener(Player.PROPERTY_CARDS, cardListener);
        game.listeners().removePropertyChangeListener(Game.PROPERTY_CURRENT_CARD, currentCardListener);
    }
}
