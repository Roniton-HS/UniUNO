package de.uniks.pmws2223.uno.service;

import de.uniks.pmws2223.uno.model.Card;

import java.util.Random;

public class GameService {

    public Card drawCard() {
        Random random = new Random();
        int randValue = random.nextInt((13 - 1) + 1) + 1;
        int randColor = random.nextInt(4);

        Card card = new Card();
        card.setValue(randValue);

        if(randValue != 13){
            switch (randColor) {
                case 0 -> card.setColor("red");
                case 1 -> card.setColor("yellow");
                case 2 -> card.setColor("green");
                case 3 -> card.setColor("blue");
            }
        }else {
            card.setColor("");
        }


        return card;
    }
}
