package com.fossgalaxy.games.fireworks.solver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.Hand;

public class SlotContraint {
	public List<CardColour> validColours;
	public List<Integer> validValues;
	
	public boolean isValid(Card card) {
		return validColours.contains(card.colour) && validValues.contains(card.value);
	}
	
	public static Map<Integer, SlotContraint> build(Hand hand) {
		
		Map<Integer, SlotContraint> map = new HashMap<Integer, SlotContraint>();
		for (int slot=0; slot<hand.getSize(); slot++) {
			SlotContraint c = new SlotContraint();
			c.validColours = hand.getPossibleColours(slot);
			c.validValues = hand.getPossibleValues(slot);
			map.put(slot, c);
		}
		
		return map;
	}
	
}