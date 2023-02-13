package de.uniks.pmws2223.uno.service;

import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;

import java.util.List;
import java.util.Random;

public class GameService {

    public Card randomCard() {
        Random r = new Random();
        int randValue = r.nextInt((13 - 1) + 1) + 1;
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
        System.out.println(player.getName() + " draws a card");
    }

    public void initDraw(Player player) {
        for (int i = 0; i < 7; i++) {
            drawCard(player);
        }
        player.setDrewCard(false);
    }

    public boolean playCard(Game game, Card card) {
        Player owner = card.getOwner();
        if (card.getOwner() != null) {
            if (card.getColor().equals(game.getCurrentCard().getColor()) || card.getValue() == game.getCurrentCard().getValue() || card.getValue() == 13) {
                game.setCurrentCard(card);
                owner.withoutCards(card);
                System.out.println("(" + game.getCurrentPlayer() + ") " + getCardName(card));

                if (owner.getCards().size() == 0) {
                    return true;
                }

                if (card.getValue() == 10) {
                    skipPlayer(game);
                } else if (card.getValue() == 11) {
                    drawCard(getNextPlayer(game));
                    drawCard(getNextPlayer(game));
                    skipPlayer(game);
                } else if (card.getValue() == 12) {
                    if (game.getPlayers().size() == 2) {
                        skipPlayer(game);
                    } else {
                        game.setClockwise(!game.isClockwise());
                        nextPlayer(game);
                    }

                } else {
                    nextPlayer(game);
                }
            }
            if(game.getCurrentPlayer() == owner && owner.isBot()){
                BotService botService = new BotService();
                botService.playRound(game, owner);
            }
        }
        return false;
    }

    public boolean playWildAs(Game game, Card card, String color) {
        card.setColor(color);
        return playCard(game, card);
    }

    public Player getNextPlayer(Game game) {
        Player currPlayer = game.getCurrentPlayer();
        List<Player> players = game.getPlayers();
        int playerIndex = players.indexOf(currPlayer);

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
        game.getCurrentPlayer().setDrewCard(false);
    }

    public void skipPlayer(Game game) {
        Player currPlayer = game.getCurrentPlayer();
        List<Player> players = game.getPlayers();
        int playerIndex = players.indexOf(currPlayer);

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

    public String getCardName(Card card) {
        int val = card.getValue();
        String out = card.getColor() + " ";
        if (val == 10) {
            out += "skip";
        } else if (val == 11) {
            out += "draw 2";
        } else if (val == 12) {
            out += "reverse";
        } else {
            out += val;
        }
        return out;
    }

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
