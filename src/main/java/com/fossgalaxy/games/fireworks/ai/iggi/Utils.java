package com.fossgalaxy.games.fireworks.ai.iggi;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.*;

import java.util.Collection;
import java.util.HashSet;

public class Utils {
    private static final int[] HAND_SIZE = {-1, -1, 5, 5, 4, 4};

    private Utils() {

    }

    public static Collection<Action> generateAllActions(int playerID, int numPlayers) {

        HashSet<Action> list = new HashSet<>();

        for (int i = 0; i < HAND_SIZE[numPlayers]; i++) {
            list.add(new DiscardCard(i));
            list.add(new PlayCard(i));
        }

        //Legal Information Actions
        for (int player = 0; player < numPlayers; player++) {
            //can't tell self about hand
            if (player == playerID) {
                continue;
            }


            for (CardColour colour : CardColour.values()) {
                list.add(new TellColour(player, colour));
            }
            for (int i = 0; i < 5; i++) {
                list.add(new TellValue(player, i));
            }
        }

        return list;
    }

    public static Collection<Action> generateActions(int playerID, GameState state) {
        HashSet<Action> list = new HashSet<>();

        Hand myHand = state.getHand(playerID);
        for (int slot = 0; slot < myHand.getSize(); slot++) {
            if (myHand.hasCard(slot)) {
                list.add(new PlayCard(slot));
                if (state.getInfomation() != state.getStartingInfomation()) {
                    list.add(new DiscardCard(slot));
                }
            }
        }

        //if we have no information, abort
        if (state.getInfomation() == 0) {
            return list;
        }

        //Legal Information Actions
        for (int player = 0; player < state.getPlayerCount(); player++) {
            //can't tell self about hand
            if (player == playerID) {
                continue;
            }

            Hand hand = state.getHand(player);
            for (int slot = 0; slot < hand.getSize(); slot++) {
                Card card = hand.getCard(slot);
                if (card != null) {
                    list.add(new TellColour(player, card.colour));
                    list.add(new TellValue(player, card.value));
                }
            }
        }

        return list;
    }

    public static Collection<Action> generateSuitableActions(int playerID, GameState state) {
        HashSet<Action> list = new HashSet<>();

        Hand myHand = state.getHand(playerID);
        for (int slot = 0; slot < myHand.getSize(); slot++) {
            if (myHand.hasCard(slot)) {
                list.add(new PlayCard(slot));
                if (state.getInfomation() != state.getStartingInfomation()) {
                    list.add(new DiscardCard(slot));
                }
            }
        }

        //if we have no information, abort
        if (state.getInfomation() == 0) {
            return list;
        }

        //Legal Information Actions
        for (int player = 0; player < state.getPlayerCount(); player++) {
            //can't tell self about hand
            if (player == playerID) {
                continue;
            }

            Hand hand = state.getHand(player);
            for (int slot = 0; slot < hand.getSize(); slot++) {
                Card card = hand.getCard(slot);
                if (card != null) {
                    if (hand.getKnownColour(slot) == null) {
                        list.add(new TellColour(player, card.colour));
                    }

                    if (hand.getKnownValue(slot) == null) {
                        list.add(new TellValue(player, card.value));
                    }
                }
            }
        }

        return list;
    }

}
