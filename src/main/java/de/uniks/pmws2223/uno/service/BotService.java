package de.uniks.pmws2223.uno.service;

import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;

import java.util.List;

public class BotService {

    GameService gameService = new GameService();

    public Card checkCard(Game game, Player player) {
        Card currCard = game.getCurrentCard();
        List<Card> cards = player.getCards();

        for (Card c : cards) {
            if (c.getColor().equals(currCard.getColor())
                            || c.getValue() == currCard.getValue()
                            || c.getValue() == 13){
                return c;
            }
        }
        return null;
    }

    public void playRound(Game game, Player player) {
        Card card = checkCard(game, player);
        if (card != null) {
            gameService.playCard(game, card);
        } else {
            gameService.drawCard(player);

            Card newCard = checkCard(game, player);
            if (newCard != null) {
                gameService.playCard(game, newCard);
            }else {
                gameService.nextPlayer(game);
            }
        }
    }
}
