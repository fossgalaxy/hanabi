package com.fossgalaxy.games.fireworks.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.SimpleHand;

public class SimpleSolver implements HandSolver {

	@Override
	public SimpleHand solve(List<Card> cards, Map<Integer, SlotContraint> contraits) {
		Map<Integer, List<Card>> possibleCards = ground(cards, contraits);
		return solve(possibleCards, new SimpleHand(5));
	}
	
	public SimpleHand solve(Map<Integer, List<Card>> possibleCards, SimpleHand soFar) {
		
		if (soFar.isComplete()) {
			return soFar;
		}
		
		System.out.println(possibleCards);
		
		for (int slot=0; slot<soFar.size; slot++) {
			if (soFar.isSet(slot)) {
				continue;
			}
			
			List<Card> possibleForSlot = possibleCards.get(slot);
			if (possibleForSlot.isEmpty()) {
				//search failed
				return null;
			}
			
			for (Card possibleCard : possibleForSlot) {
				Map<Integer, List<Card>> childPossible = deepCopy(possibleCards);
				
				//remove a copy of the possible card from all locations
				for (List<Card> entry : childPossible.values()) {
					entry.remove(possibleCard);
				}
				
				SimpleHand childHand = new SimpleHand(soFar);
				childHand.setSlot(slot, possibleCard);
				SimpleHand result = solve(childPossible, childHand);
				if (result != null) {
					return result;
				}
			}
		}
		
		//no bindings worked
		return null;
	}

	public Map<Integer, List<Card>> ground(List<Card> cards, Map<Integer, SlotContraint> contraits) {
		Map<Integer, List<Card>> possible = new HashMap<>();
		
		for (Map.Entry<Integer, SlotContraint> contraintEntry : contraits.entrySet()) {
			Integer slotID = contraintEntry.getKey();
			SlotContraint rules = contraintEntry.getValue();
			
			possible.put(slotID, cards.stream().filter((Card c) -> rules.isValid(c)).collect(Collectors.toList()));
		}
		
		return possible;
	}
	
	
	private Map<Integer, List<Card>> deepCopy(Map<Integer, List<Card>> cards) {
		Map<Integer, List<Card>> clone = new HashMap<Integer, List<Card>>();
		for (Map.Entry<Integer, List<Card>> mapEntry : cards.entrySet()) {
			clone.put(mapEntry.getKey(), new ArrayList<Card>(mapEntry.getValue()));
		}
		return clone;
	}
}
