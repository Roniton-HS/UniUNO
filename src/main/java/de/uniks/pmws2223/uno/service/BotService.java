package de.uniks.pmws2223.uno.service;

import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;
import javafx.application.Platform;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BotService {

    GameService gameService = new GameService();

    public Card checkCard(Game game, Player player) {
        Card currCard = game.getCurrentCard();
        List<Card> cards = player.getCards();

        for (Card c : cards) {
            if (c.getColor().equals(currCard.getColor())
                    || c.getValue() != 13 && c.getValue() == currCard.getValue()) {
                return c;
            }
        }
        for (Card c : cards) {
            if (c.getValue() == 13) {
                return c;
            }
        }
        return null;
    }

    public void playRound(Game game, Player player) {
        Card card = checkCard(game, player);
        if (card != null) {
            playCardWithDelay(game, card);
        } else {
            drawCardWithDelay(game, player);
        }
    }

    public void playCardWithDelay(Game game, Card card) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (card.getValue() == 13) {
                        Random r = new Random();
                        int randColor = r.nextInt(4);
                        gameService.playWildAs(game, card, switch (randColor) {
                            case 0 -> "red";
                            case 1 -> "blue";
                            case 2 -> "yellow";
                            case 3 -> "green";
                            default -> "";
                        });

                    } else {
                        gameService.playCard(game, card);
                    }
                });
            }
        }, 2000);
    }

    public void drawCardWithDelay(Game game, Player player) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    gameService.drawCard(player);
                    gameService.nextPlayer(game);
                });
            }
        }, 2000);
    }
}
