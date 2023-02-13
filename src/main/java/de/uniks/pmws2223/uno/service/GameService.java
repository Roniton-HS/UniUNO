package de.uniks.pmws2223.uno.service;

import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;

import java.util.List;
import java.util.Random;

public class GameService {

    public Card randomCard() {
        Random r = new Random();
        int randValue = r.nextInt((12 - 1) + 1) + 1;
        int randColor = r.nextInt(4);

        Card card = new Card();
        card.setValue(randValue);

        if (randValue != 13) {
            card.setColor(switch (randColor) {
                case 0 -> "red";
                case 1 -> "yellow";
                case 2 -> "green";
                case 3 -> "blue";
                default -> "";
            });
        } else {
            card.setColor("");
        }

        return card;
    }

    public void drawCard(Player player) {
        Card card = randomCard();
        player.withCards(card);
        player.setDrewCard(true);
    }

    public void initDraw(Player player) {
        for (int i = 0; i < 7; i++) {
            drawCard(player);
        }
        player.setDrewCard(false);
    }

    public void playCard(Game game, Card card) {
        Player owner = card.getOwner();
        if (card.getOwner() != null) {
            if (card.getColor().equals(game.getCurrentCard().getColor()) || card.getValue() == game.getCurrentCard().getValue() || card.getValue() == 13) {
                game.setCurrentCard(card);
                owner.withoutCards(card);
                System.out.println("(" + game.getCurrentPlayer() + ") " + card.getColor() + " " + card.getValue());

                if (card.getValue() == 10) {
                    skipPlayer(game);
                } else if (card.getValue() == 11) {
                    drawCard(getNextPlayer(game));
                    drawCard(getNextPlayer(game));
                    skipPlayer(game);
                } else if (card.getValue() == 12) {
                    game.setClockwise(!game.isClockwise());
                } else {
                    nextPlayer(game);
                }
            }
        }

    }

    public Player getNextPlayer(Game game) {
        Player currPlayer = game.getCurrentPlayer();
        List<Player> players = game.getPlayers();
        int playerIndex = players.indexOf(currPlayer);

        currPlayer.setDrewCard(false);

        if (game.isClockwise()) {
            if (playerIndex < players.size() - 1) {
                return players.get(playerIndex + 1);
            } else {
                return players.get(0);
            }
        } else {
            if (playerIndex > 0) {
                return players.get(playerIndex - 1);
            } else {
                return game.getPlayers().get(players.size() - 1);
            }
        }
    }

    public void nextPlayer(Game game) {
        game.setCurrentPlayer(getNextPlayer(game));
    }

    public void skipPlayer(Game game) {
        Player currPlayer = game.getCurrentPlayer();
        List<Player> players = game.getPlayers();
        int playerIndex = players.indexOf(currPlayer);

        currPlayer.setDrewCard(false);

        if (game.isClockwise()) {
            if (playerIndex < players.size() - 2) {
                game.setCurrentPlayer(players.get(playerIndex + 2));
            } else if (playerIndex < players.size() - 1) {
                game.setCurrentPlayer(players.get(0));
            } else {
                game.setCurrentPlayer(players.get(1));
            }
        } else {
            if (playerIndex > 1) {
                game.setCurrentPlayer(players.get(playerIndex - 2));
            } else if (playerIndex > 0) {
                game.setCurrentPlayer(game.getPlayers().get(players.size() - 1));
            } else {
                game.setCurrentPlayer(game.getPlayers().get(players.size() - 2));
            }
        }
    }
}
