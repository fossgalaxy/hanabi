package com.fossgalaxy.games.fireworks.state;

import java.util.Arrays;

/**
 * The simplest possible represention of a hanabi hand
 * 
 * @author webpigeon
 *
 */
public class SimpleHand implements HandI {

	public final int size;
	private final Card[] slots;

	public SimpleHand(int size) {
		this.size = size;
		this.slots = new Card[size];
	}
	
	public SimpleHand(SimpleHand soFar) {
		this.slots = Arrays.copyOf(soFar.slots, soFar.size);
		this.size = soFar.size;
	}

	public boolean isSet(int slot) {
		return slots[slot] != null;
	}

	public void setSlot(int slot, Card card) {
		slots[slot] = card;
	}

	public boolean isComplete() {
		for (int i=0; i<size; i++){
			if (!isSet(i)) {
				return false;
			}
		}
		
		return true;
	}

	public String toString() {
		return Arrays.toString(slots);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + size;
		result = prime * result + Arrays.hashCode(slots);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleHand other = (SimpleHand) obj;
		if (size != other.size)
			return false;
		if (!Arrays.equals(slots, other.slots))
			return false;
		return true;
	}

	/**
	 * from the hand's perspective, do we know what card this is?
	 */
	@Override
	public boolean isPossible(int slot, Card card) {
		return true;
	}

	/**
	 * Ignore attempts to tell information - we don't track that.
	 */
	@Override
	public void tell(CardColour colour, int[] slots) {
	}

	/**
	 * Ignore attempts to tell information - we don't track that.
	 */
	@Override
	public void tell(Integer value, int[] slots) {
	}

	@Override
	public void setCard(int slot, Card card) {
		slots[slot] = card;
	}

	@Override
	public boolean isCard(int slot, Card card) {
		if (slots[slot] != null) {
			return card.equals(slots[slot]);
		}
		return false;
	}

	@Override
	public boolean isPopulated(int slot) {
		return slots[slot] != null;
	}
	
}
