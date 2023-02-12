package de.uniks.pmws2223.uno.model;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class GenModel implements ClassModelDecorator {
    public class Game{

        String name;

        @Link("game")
        ArrayList<Player> players;

        @Link
        Player currentPlayer;

        @Link
        Card currentCard;
    }

    public class Player{

        String name;

        @Link("owner")
        ArrayList<Card> cards;

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
