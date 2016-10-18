package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.CardValue;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;

/**
 * Created by webpigeon on 18/10/16.
 */
public class DiscardSafeCard extends AbstractDiscardRule {
    @Override
    public Action execute(int playerID, GameState state) {
        if (state.getInfomation() == state.getStartingInfomation()) {
            return null;
        }

        CardValue[] tableCurr = new CardValue[CardColour.values().length];
        CardValue min = CardValue.FIVE;

        for (CardColour c : CardColour.values()) {
            CardValue currValue = state.getTableValue(c);
            tableCurr[c.ordinal()] = currValue;

            if (currValue.isBefore(min)) {
                min = currValue;
            }
        }

        Hand myHand = state.getHand(playerID);
        for (int slot = 0; slot < state.getHandSize(); slot++) {
            CardColour c = myHand.getKnownColour(slot);
            CardValue value = myHand.getKnownValue(slot);

            if (c != null) {
                CardValue currentTable = tableCurr[c.ordinal()];

                //even if we don't know the value, sometimes we know it's safe to discard
                if (value == null) {
                    if (currentTable.isLastInChain()) {
                        return new DiscardCard(slot);
                    }
                } else if (value.isBefore(currentTable)) {
                        return new DiscardCard(slot);
                }
            } else if (value.isBefore(min)) {
                return new DiscardCard(slot);
            }
        }

        return null;
    }
}
