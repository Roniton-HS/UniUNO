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
    private int id;
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

        //display right card color
        final Parent parent = switch (card.getColor()) {
            case "red" -> FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/RedCard.fxml")));

            case "yellow" -> FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/YellowCard.fxml")));

            case "green" -> FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/GreenCard.fxml")));

            case "blue" -> FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/BlueCard.fxml")));

            default -> FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/wildCard.fxml")));
        };

        //button to play the card
        Button button = new Button();

        //play cards on click
        if (card.getColor().equals("")) {
            //wild card
            Button redButton = (Button) parent.lookup("#redButton");
            Button blueButton = (Button) parent.lookup("#blueButton");
            Button yellowButton = (Button) parent.lookup("#yellowButton");
            Button greenButton = (Button) parent.lookup("#greenButton");

            redButton.setOnAction(action -> wildCard("red"));
            blueButton.setOnAction(action -> wildCard("blue"));
            yellowButton.setOnAction(action -> wildCard("yellow"));
            greenButton.setOnAction(action -> wildCard("green"));

        } else {
            //normal card
            button = (Button) parent.lookup("#cardButton");
            button.setId("cardButton" + id);
            button.setOnAction(action -> {
                //players turn and card fits
                if (game.getPlayers().get(0).getGame() != null && checkCard(card)) {
                    gameService.playCard(game, card);
                }
            });
        }

        //display card value
        button.setText(switch (card.getValue()) {
            case 10 -> "x";
            case 11 -> "+2";
            case 12 -> "<->";
            case 13 -> "";
            default -> String.valueOf(card.getValue());
        });

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
