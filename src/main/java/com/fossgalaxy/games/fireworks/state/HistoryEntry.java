package com.fossgalaxy.games.fireworks.state;

import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

import java.util.List;
import java.util.Objects;

public class HistoryEntry {
    public final int playerID;
    public final Action action;
    public final List<GameEvent> history;

    public HistoryEntry(int playerID, Action action, List<GameEvent> history){
        this.playerID = playerID;
        this.action = action;
        this.history = history;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryEntry that = (HistoryEntry) o;
        return playerID == that.playerID &&
                Objects.equals(action, that.action) &&
                Objects.equals(history, that.history);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, action, history);
    }
}
