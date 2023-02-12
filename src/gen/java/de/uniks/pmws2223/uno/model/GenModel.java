package de.uniks.pmws2223.uno.model;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.List;

@SuppressWarnings("unused")
public class GenModel implements ClassModelDecorator {
    public class Game{

        String name;

        @Link("game")
        List<Player> players;

        @Link
        Player currentPlayer;

        @Link
        Card currentCard;
    }

    public class Player{

        String name;

        boolean bot;

        @Link("owner")
        List<Card> cards;

        @Link("players")
        Game game;
    }

    public class Card{

        int value;
        String color;

        @Link("cards")
        Player owner;
    }




    @Override
    public void decorate(ClassModelManager mm) {
        mm.haveNestedClasses(GenModel.class);
    }
}
