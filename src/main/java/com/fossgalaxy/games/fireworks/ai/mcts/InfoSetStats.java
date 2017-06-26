package com.fossgalaxy.games.fireworks.ai.mcts;

import java.util.Arrays;

/**
 * Created by webpigeon on 19/06/17.
 */
public class InfoSetStats {
    public int[] cardPossibilities = new int[5];
    public int[] cardUniques = new int[5];
    public int cardsInDeck = 0;
    public int uniqueCardsInDeck = 0;
    public int totalPossibleCards = 0;

    public String toString() {
        return String.format("%s, %d, %d", Arrays.toString(cardPossibilities), cardsInDeck, uniqueCardsInDeck);
    }

}
