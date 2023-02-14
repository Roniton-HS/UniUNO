package de.uniks.pmws2223.uno.service;

import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

public class BotServiceTest extends ApplicationTest {
    BotService botService = new BotService();

    Card red1 = new Card().setValue(1).setColor("red");
    Card yellow2 = new Card().setValue(2).setColor("yellow");
    Card blue3 = new Card().setValue(3).setColor("blue");
    Card wild = new Card().setValue(13).setColor("");

    Player bot = new Player()
            .setBot(true)
            .setName("Bot")
            .withCards(red1, yellow2, blue3);
    Game game = new Game()
            .withPlayers(bot)
            .setClockwise(true);

    @Test
    public void checkCard() {
        //test if card is being selected if it has the same color
        game.setCurrentCard(new Card().setColor("yellow").setValue(9));
        assertEquals(yellow2, botService.checkCard(game, bot));

        //test if card is being selected if it has the same value
        game.setCurrentCard(new Card().setColor("green").setValue(3));
        assertEquals(blue3, botService.checkCard(game, bot));

        //test if wild is being selected if it has no other choices
        game.setCurrentCard(new Card().setColor("green").setValue(9));
        bot.withCards(wild);
        assertEquals(wild, botService.checkCard(game, bot));
    }

    @Test
    public void playRoundPlay() {
        //test if the bot plays a fitting card
        game.setCurrentCard(blue3);
        botService.playRound(game, bot);
        sleep(2001);
        assertEquals(2, bot.getCards().size());
    }

    @Test
    public void playRoundDraw(){
        //test if the bot draws a card if it has no fitting cards
        game.setCurrentCard(new Card().setValue(9).setColor("green"));
        botService.playRound(game, bot);
        sleep(2000);
        assertEquals(4, bot.getCards().size());
    }
}
