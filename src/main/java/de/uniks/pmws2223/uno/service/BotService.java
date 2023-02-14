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

    //final objects
    private final GameService gameService = new GameService();

    /**
     * checks for a fitting card in the bots hand
     * prioritizes non-wild cards
     *
     * @param game   game object
     * @param player bot that is being checked
     * @return fitting card
     */
    public Card checkCard(Game game, Player player) {
        Card currCard = game.getCurrentCard();
        List<Card> cards = player.getCards();

        //check for non-wild cards
        for (Card c : cards) {
            if (c.getColor().equals(currCard.getColor())
                    || c.getValue() != 13 && c.getValue() == currCard.getValue()) {
                return c;
            }
        }

        //check for wild cards
        for (Card c : cards) {
            if (c.getValue() == 13) {
                return c;
            }
        }

        return null;
    }

    /**
     * plays one bot round
     *
     * play first fitting card in deck
     * else draw a card and end turn
     *
     * @param game game object
     * @param player bot that is playing
     */
    public void playRound(Game game, Player player) {
        Card card = checkCard(game, player);
        if (card != null) {
            playCardWithDelay(game, card);
        } else {
            drawCardWithDelay(game, player);
        }
    }

    /**
     * plays a bots card
     * random wild card color
     * wait 2 seconds before play
     *
     * @param game game the card is being played to
     * @param card card that is being played
     */
    public void playCardWithDelay(Game game, Card card) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (card.getValue() == 13) {
                        Random r = new Random();
                        int randColor = r.nextInt(4);
                        card.setColor(switch (randColor) {
                            case 0 -> "red";
                            case 1 -> "blue";
                            case 2 -> "yellow";
                            case 3 -> "green";
                            default -> "";
                        });
                    }
                    gameService.playCard(game, card);
                    timer.purge();
                    timer.cancel();
                });
            }
        }, 2000);
    }

    /**
     * draws a card and ends turn
     * wait 1 second
     *
     * @param game game object
     * @param player bot that draws a card
     */
    public void drawCardWithDelay(Game game, Player player) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    gameService.drawCard(player);
                    gameService.nextPlayer(game);

                    timer.purge();
                    timer.cancel();
                });
            }
        }, 1000);
    }
}
