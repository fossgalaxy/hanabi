package com.fossgalaxy.games.fireworks.state;

/**
 * Created by webpigeon on 18/10/16.
 */
public enum CardValue {
    ONE(1,3),TWO(2,2),THREE(3,2),FOUR(4,2),FIVE(5,1);

    private final int ord;
    private final int numPerSuit;

    CardValue(int value, int numPerSuit) {
        this.ord = value;
        this.numPerSuit = numPerSuit;
    }

    public int getCount() {
        return numPerSuit;
    }

    public boolean isAfter(CardValue value) {
        return value.ord < this.ord;
    }

    public boolean isBefore(CardValue value) {
        return value.ord > this.ord;
    }

    public boolean isLastInChain() {
        return ord == 5;
    }

}
