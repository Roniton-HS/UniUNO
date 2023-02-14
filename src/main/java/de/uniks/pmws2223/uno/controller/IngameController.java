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
    //final objects
    private final App app;
    private final int botCount;
    private final String playerName;
    private final Game game;

    //listeners
    private PropertyChangeListener cardListener;
    private PropertyChangeListener currentCardListener;
    private PropertyChangeListener currentPlayerListener;

    //services
    private final GameService gameService = new GameService();
    private final BotService botService = new BotService();

    //lists
    private final List<Controller> subControllers = new ArrayList<>();

    public IngameController(App app, int botCount, String playerName) {
        this.app = app;
        this.botCount = botCount;
        this.playerName = playerName;
        game = new Game();
    }

    @Override
    public String getTitle() {
        return "UNO - Ingame";
    }

    @Override
    public void init() {
        game.setClockwise(true);
        game.setCurrentCard(gameService.randomCard());

        //add human to the game
        Player human = new Player().setName(playerName).setBot(false);
        gameService.initDraw(human);
        game.withPlayers(human);
        game.setCurrentPlayer(human);

        //add bots to the game
        for (int i = 0; i < botCount; i++) {
            Player bot = new Player().setName("Bot " + i).setBot(true);
            gameService.initDraw(bot);
            game.withPlayers(bot);
        }
    }


    @Override
    public Parent render() throws IOException {
        //Load FXML
        final Parent parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/Ingame.fxml")));

        //quit button
        final Button quitButton = (Button) parent.lookup("#quitButton");
        quitButton.setOnAction(action -> app.show(new SetupController(app)));

        Player human = game.getPlayers().get(0);

        final HBox humanCards = (HBox) parent.lookup("#humanCards");
        humanCards.setStyle("-fx-border-color: #ff0000; -fx-background-color: #ffffff; -fx-border-width: 5");
        renderHumanCards(parent, human, humanCards);
        renderDrawButton(parent, human);
        renderBots(parent);
        renderCurrentCards(parent);

        //trigger bot logic
        currentPlayerListener = currentPlayerListener -> {
            Player currPlayer = game.getCurrentPlayer();
            if (currPlayer.isBot()) {
                botService.playRound(game, currPlayer);
                humanCards.setStyle("-fx-border-color: #000000; -fx-background-color: #ffffff; -fx-border-width: 5");
            } else {
                humanCards.setStyle("-fx-border-color: #ff0000; -fx-background-color: #ffffff; -fx-border-width: 5");
            }
        };
        game.listeners().addPropertyChangeListener(Game.PROPERTY_CURRENT_PLAYER, currentPlayerListener);

        return parent;
    }

    /**
     * renders the draw button that can be used to draw a card as the human
     *
     * @param parent JavaFX parent
     * @param human  player that plays the game
     */
    public void renderDrawButton(Parent parent, Player human) {
        final Button drawButton = (Button) parent.lookup("#drawButton");
        drawButton.setOnAction(action -> {
            if (human.getGame() != null && !human.isDrewCard()) {
                gameService.drawCard(human);
                if (gameService.checkCards(game, human) == null) {
                    gameService.nextPlayer(game);
                }
            }
        });
    }

    /**
     * renders the humans cards to the bottom of the screen
     *
     * @param parent JavaFX parent
     * @param human  player that plays the game
     */
    public void renderHumanCards(Parent parent, Player human, HBox humanCards) {
        //first render cards
        for (Card c : human.getCards()) {
            CardController cardController = new CardController(game, c);
            subControllers.add(cardController);
            try {
                humanCards.getChildren().add(cardController.render());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //card listener
        cardListener = cardListener -> {
            if (human.getCards().size() == 0) {
                app.show(new GameOverController(game, true, app));
            }

            humanCards.getChildren().clear();
            for (Card c : human.getCards()) {
                try {
                    CardController cardController = new CardController(game, c);
                    subControllers.add(cardController);
                    humanCards.getChildren().add(cardController.render());
                    System.out.println(human.getCards().size());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        human.listeners().addPropertyChangeListener(Player.PROPERTY_CARDS, cardListener);
    }

    /**
     * renders the top discard pile card
     *
     * @param parent JavaFX parent
     */
    public void renderCurrentCards(Parent parent) throws IOException {
        //first render current card
        final VBox discardPile = (VBox) parent.lookup("#discardPile");
        CardController cardController = new CardController(game, game.getCurrentCard());
        subControllers.add(cardController);
        discardPile.getChildren().add(cardController.render());

        //current card listener
        currentCardListener = currentCardListener -> {
            discardPile.getChildren().clear();
            try {
                CardController cController = new CardController(game, game.getCurrentCard());
                subControllers.add(cController);
                discardPile.getChildren().add(cController.render());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        game.listeners().addPropertyChangeListener(Game.PROPERTY_CURRENT_CARD, currentCardListener);
    }

    /**
     * creates a subcontroller for each bot
     *
     * @param parent JavaFX parent
     */
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
