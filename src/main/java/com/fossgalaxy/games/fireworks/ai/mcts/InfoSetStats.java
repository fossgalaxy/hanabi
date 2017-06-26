package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.Hand;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by webpigeon on 19/06/17.
 */
public class InfoSetStats {
    public int[] cardPossibilities = new int[5];
    public int[] cardUniques = new int[5];

    public Hand myHand;
    public List<Card> deck;
    public Map<Integer, List<Card>> possibleCards;

    public int cardsInDeck = 0;
    public int uniqueCardsInDeck = 0;
    public int totalPossibleCards = 0;

    public String toString() {
        return String.format("%s, %d, %d", Arrays.toString(cardPossibilities), cardsInDeck, uniqueCardsInDeck);
    }

}
