package com.fossgalaxy.games.fireworks.state;

import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public interface GameState extends Serializable {

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

    void setLives(int newValue);

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

    void setTableValue(CardColour c, int nextValue);

    /**
     * Update state turn information.
     *
     * This should be called when moves are made to ensure the game turn count and cards remaining remains in sync.
     */
    void tick();

    void addEvent(GameEvent event);

    LinkedList<GameEvent> getHistory();

    int getTurnNumber();

    void addAction(int playerID, Action action, List<GameEvent> eventList);
}