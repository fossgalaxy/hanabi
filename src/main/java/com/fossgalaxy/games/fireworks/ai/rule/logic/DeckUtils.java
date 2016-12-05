package com.fossgalaxy.games.fireworks.ai.rule.logic;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DeckUtils {

    public static Map<Integer, List<Card>> bindCard(int player, Hand hand, List<Card> deck) {

        Map<Integer, List<Card>> possible = new HashMap<>();

        for (int slot = 0; slot < hand.getSize(); slot++) {
            final int slotF = slot;
            List<Card> possibleCards = Collections.unmodifiableList(deck.stream().filter((Card c) -> hand.isCompletePossible(slotF, c)).collect(Collectors.toList()));
            possible.put(slot, possibleCards);
        }

        return possible;
    }

    /**
     * Bind cards, but don't use real values if present.
     * <p>
     * This should be used if the player has a perfect infomation copy of the state but should act like they don't.
     * (mostly important for rule based AIs which can be used as predictors).
     *
     * @param player
     * @param hand
     * @param deck
     * @return
     */
    public static Map<Integer, List<Card>> bindBlindCard(int player, Hand hand, List<Card> deck) {

        Map<Integer, List<Card>> possible = new HashMap<>();

        for (int slot = 0; slot < hand.getSize(); slot++) {
            final int slotF = slot;
            List<Card> possibleCards = Collections.unmodifiableList(deck.stream().filter((Card c) -> hand.isPossible(slotF, c)).collect(Collectors.toList()));
            possible.put(slot, possibleCards);
        }

        return possible;
    }

    public static boolean isDiscardable(List<Card> cards, GameState state) {
        return cards.stream().allMatch((Card c) -> isDiscardable(c, state));
    }

    public static boolean isDiscardable(Card card, GameState state) {
        int tableValue = state.getTableValue(card.colour);
        if (tableValue >= card.value) {
            return true;
        }

        //TODO factor in duplicate cards and wrecked decks
        return false;
    }

    public static double getProbablity(List<Card> cards, Card target) {
        return getProbablity(cards, target::equals);
    }

    public static double getProbablity(List<Card> cards, CardColour target) {
        return getProbablity(cards, (Card c) -> target.equals(c.colour));
    }

    public static double getProbablity(List<Card> cards, Integer target) {
        return getProbablity(cards, (Card c) -> target.equals(c.value));
    }

    public static double getProbablity(List<Card> cards, Predicate<Card> rule) {
        double matchingCards = cards.stream().filter(rule).count() * 1.0;
        return matchingCards / cards.size();
    }

    public static List<Integer> bindOrder(Map<Integer, List<Card>> possibleCards) {

        List<Integer> ordering = new ArrayList<>(possibleCards.keySet());
        Collections.sort(ordering, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(possibleCards.get(o1).size(), possibleCards.get(o2).size());
            }
        });

        return ordering;
    }

    public static Map<Integer, Card> bindCards(List<Integer> order, Map<Integer, List<Card>> possibleCards) {

        Random r = new Random();
        List<Card> removed = new ArrayList<Card>();

        Map<Integer, Card> hand = new HashMap<Integer, Card>();
        for (Integer slot : order) {
            List<Card> possible = new ArrayList<>(possibleCards.get(slot));
            for (Card card : removed) {
                possible.remove(card);
            }

            Card selected = possible.get(r.nextInt(possible.size()));
            hand.put(slot, selected);
            removed.add(selected);
        }

        return hand;
    }
}
