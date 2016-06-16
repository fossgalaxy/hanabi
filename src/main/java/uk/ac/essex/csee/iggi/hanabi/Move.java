package uk.ac.essex.csee.iggi.hanabi;

/**
 * A move object - contains all the information needed to execute a move
 */
public class Move {
	public MoveType type;
	public CardColour colour;
	public int value;
	
	public MoveType getType() {
		return type;
	}
}
