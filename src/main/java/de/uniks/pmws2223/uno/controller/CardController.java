package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.App;
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
    private App app;

    private final GameService gameService = new GameService();

    public CardController(Game game, Card card, App app) {
        this.game = game;
        this.card = card;
        this.app = app;
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

        Button button = new Button();
        if (card.getColor().equals("")) {
            Button redButton = (Button) parent.lookup("#redButton");
            redButton.setOnAction(action -> {
                card.setColor("red");
                if (gameService.playWildAs(game, card, "red")) {
                    app.show(new GameOverController(game, true, app));
                }
                game.getCurrentPlayer().setDrewCard(false);
            });

            Button blueButton = (Button) parent.lookup("#blueButton");
            blueButton.setOnAction(action -> {
                card.setColor("blue");
                if (gameService.playWildAs(game, card, "blue")) {
                    app.show(new GameOverController(game, true, app));
                }
                game.getCurrentPlayer().setDrewCard(false);
            });

            Button yellowButton = (Button) parent.lookup("#yellowButton");
            yellowButton.setOnAction(action -> {
                card.setColor("yellow");
                if (gameService.playWildAs(game, card, "yellow")) {
                    app.show(new GameOverController(game, true, app));
                }
                game.getCurrentPlayer().setDrewCard(false);
            });

            Button greenButton = (Button) parent.lookup("#greenButton");
            greenButton.setOnAction(action -> {
                card.setColor("yellow");
                if (gameService.playWildAs(game, card, "green")) {
                    app.show(new GameOverController(game, true, app));
                }
                game.getCurrentPlayer().setDrewCard(false);
            });

        } else {
            //action on card click
            button = (Button) parent.lookup("#cardButton");
            button.setOnAction(action -> {
                game.getCurrentPlayer().setDrewCard(false);
                if (game.getPlayers().get(0).getGame() != null) {
                    if (gameService.playCard(game, card)) {
                        app.show(new GameOverController(game, true, app));
                    }
                }
            });
        }

        //display card type
        button.setText(switch (card.getValue()) {
            case 10 -> "x";
            case 11 -> "+2";
            case 12 -> "<->";
            case 13 -> "";
            default -> String.valueOf(card.getValue());
        });

        return parent;
    }

    @Override
    public void destroy() {

    }
}
