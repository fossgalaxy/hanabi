package com.fossgalaxy.games.fireworks.state.hands;

import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.CardValue;

/**
 * Created by webpigeon on 18/10/16.
 */
public interface SimpleHand {

    int getSize();

    CardColour[] getColours(int slot);
    Integer[] getValues(int slot);

    boolean isKnownColour(int slot);
    boolean isKnownValue(int slot);

    boolean isPossible(int slot, CardColour colour);
    boolean isPossible(int slot, CardValue value);

    default boolean isPossible(int slot, CardColour cardColour, CardValue value){
        return isPossible(slot, cardColour) && isPossible(slot, value);
    }

}
