package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;
import de.uniks.pmws2223.uno.service.BotService;
import de.uniks.pmws2223.uno.service.GameService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IngameController implements Controller {
    private final App app;
    private final int botCount;
    private final String playerName;
    private final Game game = new Game();

    private PropertyChangeListener cardListener;
    private PropertyChangeListener currentCardListener;
    private PropertyChangeListener currentPlayerListener;

    private final GameService gameService = new GameService();
    private final BotService botService = new BotService();

    private final List<Controller> subControllers = new ArrayList<>();

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

        Player human = new Player().setName(playerName).setBot(false);
        gameService.initDraw(human);

        game.setClockwise(true);
        game.setName("UNO");
        game.withPlayers(human);
        game.setCurrentPlayer(human);
        game.setCurrentCard(gameService.randomCard());

        for (int i = 0; i < botCount; i++) {
            Player bot = new Player().setName("Bot " + i).setBot(true);
            gameService.initDraw(bot);
            game.withPlayers(bot);
        }
    }


    @Override
    public Parent render() throws IOException {
        Player human = game.getPlayers().get(0);

        //Load FXML
        final Parent parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/Ingame.fxml")));

        renderBots(parent);

        //quit button
        final Button quitButton = (Button) parent.lookup("#quitButton");
        quitButton.setOnAction(action -> app.show(new SetupController(app)));

        //draw button
        final Button drawButton = (Button) parent.lookup("#drawButton");
        drawButton.setOnAction(action -> {
            if (human.getGame() != null && !human.isDrewCard()) {
                gameService.drawCard(human);
                if (gameService.checkCards(game, human) == null) {
                    gameService.nextPlayer(game);
                }
            }
        });

        //first render cards
        final HBox humanCards = (HBox) parent.lookup("#humanCards");
        for (Card c : human.getCards()) {
            CardController cardController = new CardController(game, c, app);
            subControllers.add(cardController);
            humanCards.getChildren().add(cardController.render());
        }

        //card listener
        cardListener = cardListener -> {
            humanCards.getChildren().clear();
            for (Card c : human.getCards()) {
                try {
                    CardController cardController = new CardController(game, c, app);
                    subControllers.add(cardController);
                    humanCards.getChildren().add(cardController.render());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        human.listeners().addPropertyChangeListener(Player.PROPERTY_CARDS, cardListener);

        renderCurrentCards(parent);

        currentPlayerListener = currentPlayerListener -> {
            Player currPlayer = game.getCurrentPlayer();
            System.out.println("-> " + currPlayer.getName());
            if (currPlayer.isBot()) {
                botService.playRound(game, currPlayer);
            }
        };
        game.listeners().addPropertyChangeListener(Game.PROPERTY_CURRENT_PLAYER, currentPlayerListener);

        return parent;
    }

    public void renderCurrentCards(Parent parent) throws IOException {
        //first render current card
        final VBox discardPile = (VBox) parent.lookup("#discardPile");
        CardController cardController = new CardController(game, game.getCurrentCard(), app);
        subControllers.add(cardController);
        discardPile.getChildren().add(cardController.render());

        //current card listener
        currentCardListener = currentCardListener -> {
            discardPile.getChildren().clear();
            try {
                CardController cController = new CardController(game, game.getCurrentCard(), app);
                subControllers.add(cController);
                discardPile.getChildren().add(cController.render());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        game.listeners().addPropertyChangeListener(Game.PROPERTY_CURRENT_CARD, currentCardListener);
    }

    public void renderBots(Parent parent) throws IOException {

        //first render bots
        final VBox botSpace1 = (VBox) parent.lookup("#botSpace1");
        BotController botController1 = new BotController(app, game, game.getPlayers().get(1));
        subControllers.add(botController1);
        botSpace1.getChildren().add(botController1.render());

        if (botCount > 1) {
            final VBox botSpace2 = (VBox) parent.lookup("#botSpace2");
            BotController botController2 = new BotController(app, game, game.getPlayers().get(2));
            subControllers.add(botController2);
            botSpace2.getChildren().add(botController2.render());
        }

        if (botCount > 2) {
            final VBox botSpace3 = (VBox) parent.lookup("#botSpace3");
            BotController botController3 = new BotController(app, game, game.getPlayers().get(3));
            subControllers.add(botController3);
            botSpace3.getChildren().add(botController3.render());
        }
    }

    @Override
    public void destroy() {
        for (Controller c : subControllers) {
            c.destroy();
        }
        game.getPlayers().get(0).listeners().removePropertyChangeListener(Player.PROPERTY_CARDS, cardListener);
        game.listeners().removePropertyChangeListener(Game.PROPERTY_CURRENT_CARD, currentCardListener);
        game.listeners().removePropertyChangeListener(Game.PROPERTY_CURRENT_PLAYER, currentPlayerListener);
    }
}
