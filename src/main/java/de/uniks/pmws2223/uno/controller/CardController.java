package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.service.GameService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Objects;

public class CardController implements Controller {

    //final objects
    private final Game game;
    private final Card card;
    private final int id;
    private final GameService gameService;

    public CardController(Game game, Card card, int id) {
        this.game = game;
        this.card = card;
        this.id = id;
        gameService = new GameService(false);
    }

    @Override
    public String getTitle() {
        return "card";
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {

        final Parent parent;
        if (card.getColor().equals("")) {
            /*
            WILD CARD
             */

            //load fxml
            parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/WildCard.fxml")));

            //buttons to play the card
            Button redButton = (Button) parent.lookup("#redButton");
            Button blueButton = (Button) parent.lookup("#blueButton");
            Button yellowButton = (Button) parent.lookup("#yellowButton");
            Button greenButton = (Button) parent.lookup("#greenButton");

            redButton.setOnAction(action -> wildCard("red"));
            blueButton.setOnAction(action -> wildCard("blue"));
            yellowButton.setOnAction(action -> wildCard("yellow"));
            greenButton.setOnAction(action -> wildCard("green"));

        } else {
            /*
            NORMAL CARD
             */

            //load fxml
            parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/Card.fxml")));

            //button to play the card
            Button button;
            button = (Button) parent.lookup("#cardButton");
            button.setId("cardButton" + id);
            button.setOnAction(action -> {
                //players turn and card fits
                if (card.getOwner() == game.getCurrentPlayer() && checkCard(card)) {
                    gameService.playCard(game, card);
                }
            });

            //set card text
            button.setText(switch (card.getValue()) {
                case 10 -> "x";
                case 11 -> "+2";
                case 12 -> "<->";
                case 13 -> "";
                default -> String.valueOf(card.getValue());
            });

            String baseStyle = "-fx-border-color: #000000; -fx-border-width: 5;";
            switch (card.getColor()) {
                case "red" -> button.setStyle(baseStyle + "-fx-background-color: #9e0808");

                case "yellow" -> button.setStyle(baseStyle + "-fx-background-color: #f5d520");

                case "green" -> button.setStyle(baseStyle + "-fx-background-color: #088214");

                case "blue" -> button.setStyle(baseStyle + "-fx-background-color: #1949b0");
            }
        }
        return parent;
    }

    /**
     * checks if a card fits to the discard pile
     * checks if a card belongs to the player
     *
     * @param card card that is being checked
     */
    private boolean checkCard(Card card) {
        Card c = game.getCurrentCard();
        return c.getColor().equals(card.getColor()) || c.getValue() == card.getValue() || card.getValue() == 13 && card.getOwner() != null || c.getColor().equals("");
    }

    /**
     * set the wild cards color and play it
     * if the player is active
     *
     * @param color color the wild card gets after being played
     */
    private void wildCard(String color) {
        card.setColor(color);
        if (game.getPlayers().get(0).getGame() != null) {
            gameService.playCard(game, card);
        }
    }

    @Override
    public void destroy() {

    }
}
