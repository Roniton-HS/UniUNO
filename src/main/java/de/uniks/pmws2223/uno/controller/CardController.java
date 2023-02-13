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

    private final Game game;
    private final Card card;

    private final GameService gameService = new GameService();

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

        //display right card color
        final Parent parent = switch (card.getColor()) {
            case "red" -> FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/RedCard.fxml")));

            case "yellow" -> FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/YellowCard.fxml")));

            case "green" -> FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/GreenCard.fxml")));

            case "blue" -> FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/BlueCard.fxml")));

            default -> FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/Card.fxml")));
        };

        //action on card click
        final Button button = (Button) parent.lookup("#cardButton");
        button.setOnAction(action -> gameService.playCard(game, card));

        //display card type
        button.setText(switch (card.getValue()) {
            case 10 -> "x";
            case 11 -> "+2";
            case 12 -> "<->";
            case 13 -> "w";
            default -> String.valueOf(card.getValue());
        });

        return parent;
    }

    @Override
    public void destroy() {

    }
}
