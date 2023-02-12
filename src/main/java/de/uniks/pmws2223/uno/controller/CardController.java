package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Objects;

public class CardController implements Controller {

    private Game game;
    private Card card;

    public CardController(Game game, Card card) {
        this.game = game;
        this.card = card;
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

        switch (card.getColor()) {
            case "red" -> parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/RedCard.fxml")));

            case "yellow" -> parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/YellowCard.fxml")));

            case "green" -> parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/GreenCard.fxml")));

            case "blue" -> parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/BlueCard.fxml")));

            default -> parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/Card.fxml")));

        }

        final Button button = (Button) parent.lookup("#cardButton");
        button.setOnAction(action -> {game.setCurrentCard(card);
            game.getPlayers().get(0).withoutCards(card);
        });


        String cardPrint;
        switch (card.getValue()){
            case 10 -> cardPrint = "x";
            case 11 -> cardPrint = "+2";
            case 12 -> cardPrint = "<->";
            case 13 -> cardPrint = "w";
            default -> cardPrint = String.valueOf(card.getValue());
        }
        button.setText(cardPrint);

        return parent;
    }

    @Override
    public void destroy() {

    }
}
