package com.fossgalaxy.games.fireworks.state;

import java.util.Arrays;

/**
 * A hand which keeps track of information.
 * 
 * @author webpigeon
 *
 */
public class InfomationHand extends SimpleHand {

	private final CardInfomation[] info;
	
	public InfomationHand(int size) {
		super(size);
		this.info = new CardInfomation[size];
		
		for (int slot=0; slot<size; slot++) {
			info[slot] = new CardInfomation();
		}
	}
	
	public InfomationHand(InfomationHand soFar) {
		super(soFar);
		this.info = Arrays.copyOf(soFar.info, size);
	}

	@Override
	public void tell(CardColour colour, int[] slots){
		for (int slot : slots) {
			info[slot].colour = colour;
		}
	}
	
	@Override
	public void tell(Integer value, int[] slots) {
		for (int slot : slots) {
			info[slot].value = value;
		}
	}
	
	public boolean isPossible(int slot, Card card) {
		CardInfomation cardInfo = info[slot];
		return card.colour.equals(cardInfo.colour) && card.value.equals(cardInfo.value);
	}
	
	private class CardInfomation {
		CardColour colour;
		Integer value;
	}
	
}
