package com.fossgalaxy.games.fireworks.solver;

import java.util.List;
import java.util.Map;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.SimpleHand;

public interface HandSolver {

	public SimpleHand solve(List<Card> cards, Map<Integer, SlotContraint> contraits);
	
}
