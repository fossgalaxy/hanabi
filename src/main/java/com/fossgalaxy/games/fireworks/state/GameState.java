package com.fossgalaxy.games.fireworks.state;

import java.util.Collection;

public interface GameState {

    void init();

    void init(Long seed);

    void addToDiscard(Card card);

    Card drawFromDeck();

    // query the state (primatives)
    Card getCardAt(int player, int slot);

    // injector
    Deck getDeck();

    GameState getCopy();

    void deal(int playerID);

    Collection<Card> getDiscards();

    Hand getHand(int player);

    int getHandSize();

    int getInfomation();

    int getLives();

    // meta data
    int getPlayerCount();

    int getScore();

    int getStartingInfomation();

    int getStartingLives();

    int getTableValue(CardColour colour);

    boolean isGameOver();

    void setCardAt(int player, int slot, Card newCard);

    void setInformation(int newValue);

    // update the state
    @Deprecated
    void setKnownValue(int player, int slot, Integer value, CardColour colour);

    void setLives(int newValue);

    void setTableValue(CardColour c, int nextValue);

    void tick();
}