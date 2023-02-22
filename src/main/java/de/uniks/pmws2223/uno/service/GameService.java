package de.uniks.pmws2223.uno.service;

import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;

import java.util.List;
import java.util.Random;

public class GameService {

    Random r;

    public GameService(boolean seeded) {
        if (seeded) {
            r = new Random(100);
        }else {
            r = new Random();
        }
    }

    public GameService() {
        r = new Random();
    }

    /**
     * creates a new card
     *
     * @return randomized Card
     */
    public Card randomCard() {
        int randValue = r.nextInt((13 - 1) + 1) + 1;

        Card card = new Card();
        card.setValue(randValue);

        if (randValue != 13) {
            card.setColor(randomColor());
        } else {
            card.setColor("");
        }

        return card;
    }

    public String randomColor() {
        int randColor = r.nextInt(4);
        return switch (randColor) {
            case 0 -> "red";
            case 1 -> "yellow";
            case 2 -> "green";
            case 3 -> "blue";
            default -> "";
        };
    }

    /**
     * gives a player a random new card
     *
     * @param player who draws a card
     */
    public void drawCard(Player player) {
        Card card = randomCard();
        player.withCards(card);
        player.setDrewCard(true);
    }

    /**
     * gives a player 7 random cards
     *
     * @param player player who draws cards
     */
    public void initDraw(Player player) {
        for (int i = 0; i < 7; i++) {
            drawCard(player);
        }
        player.setDrewCard(false);
    }

    /**
     * plays a card to the discard pile
     * resets player draws
     * <p>
     * 10 -> skip player
     * 11 -> draw 2 card + skip
     * 12 -> change direction
     *
     * @param game game the card will be played in
     * @param card card that is being played
     */
    public void playCard(Game game, Card card) {

        game.setCurrentCard(card);

        Player owner = card.getOwner();
        owner.withoutCards(card);
        owner.setDrewCard(false);

        //bonus card effects
        switch (card.getValue()) {
            case 10 -> skipPlayer(game);
            case 11 -> {
                drawCard(getNextPlayer(game));
                drawCard(getNextPlayer(game));
                skipPlayer(game);
            }
            case 12 -> {
                if (game.getPlayers().size() == 2) {
                    skipPlayer(game);
                } else {
                    game.setClockwise(!game.isClockwise());
                    nextPlayer(game);
                }
            }
            default -> nextPlayer(game);
        }

        //reactivate bot if 2 player and bot skips
        if (game.getCurrentPlayer() == owner && owner.isBot()) {
            BotService botService = new BotService();
            botService.playRound(game, owner);
        }

    }

    /**
     * @param game game object
     * @return next player to play
     * <p>
     * can go 2 directions
     */
    public Player getNextPlayer(Game game) {
        Player currPlayer = game.getCurrentPlayer();
        List<Player> players = game.getPlayers();
        int playerIndex = players.indexOf(currPlayer);

        if (game.isClockwise()) {
            //clockwise
            if (playerIndex < players.size() - 1) {
                return players.get(playerIndex + 1);
            } else {
                return players.get(0);
            }
        } else {
            //counterclockwise
            if (playerIndex > 0) {
                return players.get(playerIndex - 1);
            } else {
                return game.getPlayers().get(players.size() - 1);
            }
        }
    }

    /**
     * sets the next player
     *
     * @param game game object
     */
    public void nextPlayer(Game game) {
        game.setCurrentPlayer(getNextPlayer(game));
        game.getCurrentPlayer().setDrewCard(false);
    }

    /**
     * skips a player and sets the next player
     * can go in 2 directions
     *
     * @param game game object
     */
    public void skipPlayer(Game game) {
        Player currPlayer = game.getCurrentPlayer();
        List<Player> players = game.getPlayers();
        int playerIndex = players.indexOf(currPlayer);


        if (game.isClockwise()) {
            //clockwise
            if (playerIndex < players.size() - 2) {
                game.setCurrentPlayer(players.get(playerIndex + 2));
            } else if (playerIndex < players.size() - 1) {
                game.setCurrentPlayer(players.get(0));
            } else {
                game.setCurrentPlayer(players.get(1));
            }
        } else {
            //counterclockwise
            if (playerIndex > 1) {
                game.setCurrentPlayer(players.get(playerIndex - 2));
            } else if (playerIndex > 0) {
                game.setCurrentPlayer(game.getPlayers().get(players.size() - 1));
            } else {
                game.setCurrentPlayer(game.getPlayers().get(players.size() - 2));
            }
        }
    }

    /**
     * checks if a player has a fitting card to the discard pile
     *
     * @param game   game object
     * @param player player whose cards are being checked
     * @return return the fitting card
     */
    public Card checkCards(Game game, Player player) {
        Card currCard = game.getCurrentCard();
        List<Card> cards = player.getCards();

        for (Card c : cards) {
            if (c.getColor().equals(currCard.getColor())
                    || c.getValue() == currCard.getValue()
                    || c.getValue() == 13) {
                return c;
            }
        }
        return null;
    }
}
