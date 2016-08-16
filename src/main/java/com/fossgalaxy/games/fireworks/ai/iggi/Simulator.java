package com.fossgalaxy.games.fireworks.ai.iggi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fossgalaxy.games.fireworks.ai.rule.logic.DeckUtils;
import com.fossgalaxy.games.fireworks.solver.HandSolver;
import com.fossgalaxy.games.fireworks.solver.SimpleSolver;
import com.fossgalaxy.games.fireworks.solver.SlotContraint;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.Deck;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.SimpleHand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public class Simulator {

	
	public static void main(String[] args) {
		GameState state = new BasicState(5,3);
		state.init();
		
		Deck deck = state.getDeck();
		//deck.init();
		
		//fake telling player 0 about their first card
		Action tell = new TellColour(0, state.getCardAt(0, 0).colour);
		List<GameEvent> events = tell.apply(1, state);
		for (GameEvent event : events) {
			if (event.isVisibleTo(0)) {
				event.apply(state);
			}
		}
		
		Action tell2 = new TellValue(0, state.getCardAt(0, 0).value);
		List<GameEvent> events2 = tell2.apply(1, state);
		for (GameEvent event : events2) {
			if (event.isVisibleTo(0)) {
				event.apply(state);
			}
		}
		
		System.out.println(tell);
		System.out.println(tell2);
		System.out.println(state.getHandSize());
		
		//damn it, because we are server side, the deck already has our card removed
		List<Card> possibleCards = deck.toList();
		for (int slot=0; slot<state.getHand(0).getSize(); slot++) {
			possibleCards.add(state.getHand(0).getCard(slot));
		}
			
		HandSolver solver = new SimpleSolver();
		SimpleHand hand = solver.solve(possibleCards, SlotContraint.build(state.getHand(0)));
		
		System.out.println(hand);

		
		Map<Integer, List<Card>> possible = DeckUtils.bindCard(0, state.getHand(0), possibleCards);
		
		for (Map.Entry<Integer, List<Card>> entry : possible.entrySet()) {
			System.out.println(String.format("%s, %d %s", entry.getKey(), entry.getValue().size(), entry.getValue()));
			System.out.println(String.format("P(blue) %f", DeckUtils.getProbablity(entry.getValue(), CardColour.BLUE)));
			System.out.println(String.format("P(red) %f", DeckUtils.getProbablity(entry.getValue(), CardColour.RED)));
			System.out.println(String.format("P(5) %f", DeckUtils.getProbablity(entry.getValue(), 5)));
			System.out.println(String.format("P(1) %f", DeckUtils.getProbablity(entry.getValue(), 1)));
		}
		
		Simulator s = new Simulator();
		double score = s.simulate(0, state);
		System.out.println(score);
	}
	
	public double simulate(int startingPlayer, GameState state) {
		
		//shuffle the cards currently in the deck
		Deck deck = state.getDeck();
		deck.shuffle();
		
		//TODO assign 5 compatible cards to the hand.
		
		int player = startingPlayer;
		while (!state.isGameOver()) {
			Action action = selectAction(player, state);
			System.out.println(action);
			action.apply(player, state);
			
			player = (player + 1) % state.getPlayerCount();
		}
		
		return state.getScore();
	}
	
	public Action selectAction(int player, GameState state) {
		List<Action> actions = new ArrayList<>(Utils.generateActions(player, state));
		
		Random r = new Random();
		return actions.get(r.nextInt(actions.size()));
	}
	
}
