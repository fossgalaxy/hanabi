package com.fossgalaxy.games.fireworks.state;

/**
 * A card for the Hanabi card game
 */
public class Card implements Comparable<Card> {
	public final CardValue value;
	public final CardColour colour;

	public Card(CardValue value, CardColour colour) {
		this(colour, value);
	}

	public Card(CardColour colour, CardValue value) {
		this.colour = colour;
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (colour != other.colour)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return colour + " " + value;
	}

	@Override
	public int compareTo(Card o) {
		if (colour.equals(o.colour)) {
			return value.compareTo(o.value);
		} else {
			return colour.compareTo(o.colour);
		}
	}
}
