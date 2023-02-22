package de.uniks.pmws2223.uno.service;

import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;
import org.testfx.framework.junit.ApplicationTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameServiceTest extends ApplicationTest {

    GameService gameService = new GameService();

    Card red12 = new Card().setValue(12).setColor("red");
    Card yellow10 = new Card().setValue(10).setColor("yellow");
    Card blue11 = new Card().setValue(11).setColor("blue");
    Card wild = new Card().setValue(13).setColor("");

    Player player1 = new Player()
            .setBot(true)
            .setName("Player1")
            .withCards(red12, yellow10, blue11, wild);

    Player player2 = new Player()
            .setBot(true)
            .setName("Player2");
    Player player3 = new Player()
            .setBot(true)
            .setName("Player3");

    Game game = new Game()
            .withPlayers(player1, player2, player3);


    @Test
    public void playCard() {
        //test if a card can be played to the discard pile
        gameService.playCard(game, red12);

        assertEquals(red12, game.getCurrentCard());
        assertEquals(3, player1.getCards().size());
    }

    @Test
    public void getNextPlayer() {
        //test if the next player is being selected clockwise
        game.setClockwise(true);
        game.setCurrentPlayer(player1);

        gameService.nextPlayer(game);
        assertEquals(player2, game.getCurrentPlayer());

        gameService.nextPlayer(game);
        assertEquals(player3, game.getCurrentPlayer());

        //test if the next player is being selected counterclockwise
        game.setClockwise(false);

        gameService.nextPlayer(game);
        assertEquals(player2, game.getCurrentPlayer());
    }

    @Test
    public void skip() {
        //tests if a player is being skipped if a skip card is played
        game.setClockwise(true);
        game.setCurrentPlayer(player1);

        gameService.playCard(game, yellow10);
        assertEquals(player3, game.getCurrentPlayer());
    }

    @Test
    public void draw2() {
        //tests if a player is being skipped and drawing 2 card if a +2 card is played
        game.setClockwise(true);
        game.setCurrentPlayer(player1);

        gameService.playCard(game, blue11);
        assertEquals(player3, game.getCurrentPlayer());
        assertEquals(2, player2.getCards().size());
    }

    @Test
    public void switchDirections() {
        //tests if the direction of the game is being changed if a switch card is played
        game.setClockwise(true);
        game.setCurrentPlayer(player1);

        gameService.playCard(game ,red12);
        assertEquals(player3, game.getCurrentPlayer());
    }

    @Test
    public void wild() {
        //test if a wild card can be played
        game.setClockwise(true);
        game.setCurrentPlayer(player1);

        gameService.playCard(game, wild);

        assertEquals(game.getCurrentCard(), wild);
        assertEquals("", game.getCurrentCard().getColor());

        /*
        wild cards get their color by clicking on them, therefore this logic is handled
        by the card controller. You can still give them a color by hand though
         */
        wild.setColor("green");
        assertEquals("green", game.getCurrentCard().getColor());
    }
}
