package com.fossgalaxy.games.fireworks.state;

import java.util.*;

/**
 * Represents a deck of Hanabi cards.
 *
 */
public class Deck {
	private LinkedList<Card> cards;

	public Deck() {
		this.cards = new LinkedList<>();
	}

	/**
	 * Provides a clone of the given deck
	 *
	 * A shallow copy - but the cards themselves are immutable so no problem.
	 *
	 * @param deck
	 *            The given deck to be cloned
	 */
	public Deck(Deck deck) {
		this.cards = new LinkedList<>(deck.cards);
	}

	/**
	 * Add a new card to the deck.
	 *
	 * @param card
	 *            the card to add
	 */
	public void add(Card card) {
		cards.push(card);
	}

	/**
	 * Gets the number of cards left in the deck
	 *
	 * @return int number of cards left
	 */
	public int getCardsLeft() {
		return cards.size();
	}

	/**
	 * Gets and removes the top card from the deck
	 *
	 * @return The card that was on top of the deck
	 */
	public Card getTopCard() {
		return cards.pop();
	}

	/**
	 * Are there any cards left in the deck?
	 *
	 * @return boolean are there any cards left?
	 */
	public boolean hasCardsLeft() {
		return !cards.isEmpty();
	}

	/**
	 * Initialises the deck of cards with a complete set for the game
	 */
	public void init() {
		for (CardColour c : CardColour.values()) {
			for (CardValue v : CardValue.values()) {
				for (int i=0; i<v.getCount(); i++) {
					cards.add(new Card(v,c));
				}
			}
		}
	}

	/**
	 * shuffle this deck of cards.
	 *
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	/**
	 * shuffle this deck with a prefined seed
	 * 
	 */
	public void shuffle(long seed) {
		Collections.shuffle(cards, new Random(seed));
	}

	public void remove(Card card) {
		cards.remove(card);
	}
	
	public List<Card> toList() {
		return new LinkedList<>(cards);
	}

}
