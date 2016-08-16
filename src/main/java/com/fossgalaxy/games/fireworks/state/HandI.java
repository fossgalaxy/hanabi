package com.fossgalaxy.games.fireworks.state;

public interface HandI {

	//belief/information based
	public boolean isPossible(int slot, Card card);
	public void tell(CardColour colour, int[] slots);
	public void tell(Integer value, int[] slots);
	
	//physical world based
	public void setCard(int slot, Card card);
	public boolean isCard(int slot, Card card);
	public boolean isPopulated(int slot);
	
}
