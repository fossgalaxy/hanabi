package com.fossgalaxy.games.fireworks.state.actions;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.RulesViolation;
import com.fossgalaxy.games.fireworks.state.events.CardDiscarded;
import com.fossgalaxy.games.fireworks.state.events.CardDrawn;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class DiscardCard implements Action {
    public final int slot;

    public DiscardCard(int slot) {
        this.slot = slot;
    }

    @Override
    public List<GameEvent> apply(int playerID, GameState game) {
        if (!isLegal(playerID, game)) {
            throw new RulesViolation("this is a violation of the game rules!", this);
        }

        int currentInfo = game.getInfomation();

        // deal with the old card first
        Card oldCard = game.getCardAt(playerID, slot);
        game.addToDiscard(oldCard);

        // the players gain one information back
        game.setInformation(currentInfo + 1);

        ArrayList<GameEvent> events = new ArrayList<>();
        events.add(new CardDiscarded(playerID, slot, oldCard.colour, oldCard.value));

        // deal with the new card
        // XXX null pointer exception if next card was null.
        if (game.getDeck().hasCardsLeft()) {
            Card newCard = game.drawFromDeck();
            game.setCardAt(playerID, slot, newCard);
            events.add(new CardDrawn(playerID, slot, newCard.colour, newCard.value));
        } else {
            game.setCardAt(playerID, slot, null);
        }

        return events;
    }

    @Override
    public boolean isLegal(int playerID, GameState state) {
        return state.getInfomation() != state.getStartingInfomation();
    }

    @Override
    public String toProtocol() {
        return String.format("%s %d", TextProtocol.ACTION_DISCARD, slot);
    }

    public String toString() {
        return String.format("Discard %d", slot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscardCard that = (DiscardCard) o;

        return slot == that.slot;

    }

    @Override
    public int hashCode() {
        return slot;
    }
}
